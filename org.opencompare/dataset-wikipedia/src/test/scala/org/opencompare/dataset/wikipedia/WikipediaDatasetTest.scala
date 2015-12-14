package org.opencompare.dataset.wikipedia

import java.io._
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors

import com.github.tototoshi.csv.{CSVReader, CSVWriter}
import org.opencompare.api.java.exception.MergeConflictException
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.impl.io.{KMFJSONExporter, KMFJSONLoader}
import org.opencompare.api.java.io.CSVExporter
import org.opencompare.formalizer.extractor.CellContentInterpreter
import org.opencompare.io.wikipedia.export.PCMModelExporter
import org.opencompare.io.wikipedia.io.{MediaWikiAPI, WikiTextTemplateProcessor, WikiTextExporter, WikiTextLoader}
import org.opencompare.io.wikipedia.pcm.Page
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.collection.JavaConversions._
import scala.concurrent._
import scala.concurrent.duration._
import scala.io.Source
import scala.xml.PrettyPrinter

/**
 * Created by gbecan on 4/27/15.
 */
class WikipediaDatasetTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val executionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(20))
  var templateProcessor : WikiTextTemplateProcessor = _
  var miner : WikiTextLoader = _
  val mediaWikiAPI = new MediaWikiAPI("wikipedia.org")
  val language = "en"
  val templateCacheFile = new File("resources/template-cache.csv")


  override def beforeAll() {
    super.beforeAll()

    new File("input/").mkdirs()
    new File("output/csv/").mkdirs()
    new File("output/html/").mkdirs()
    new File("output/dump/").mkdirs()
    new File("output/model/").mkdirs()
    new File("output/wikitext/").mkdirs()

    // Load cache for templates
    if (templateCacheFile.exists()) {
      val csvLoader = CSVReader.open(templateCacheFile)

      val initialCache = (for (line : Seq[String] <- csvLoader.iterator if line.size == 2) yield {
          val template = line(0)
          val expandedTemplate = line(1)
          template -> expandedTemplate
      }).toMap

      csvLoader.close()

      templateProcessor = new WikiTextTemplateProcessor(mediaWikiAPI, initialCache)

    } else {
      templateProcessor = new WikiTextTemplateProcessor(mediaWikiAPI)
    }

    miner = new WikiTextLoader(templateProcessor)
  }


  override protected def afterAll(): Unit = {
    super.afterAll()

    val csvWriter = CSVWriter.open(templateCacheFile)
    for (line <- templateProcessor.templateCache) {
      csvWriter.writeRow(Seq(line._1, line._2))
    }

    csvWriter.close()
  }

  def parsePCMFromFile(file : String) : Page= {
    val reader= Source.fromFile(file)
    val code = reader.mkString
    reader.close
    miner.mineInternalRepresentation(language, code, file)
  }

  def parseFromTitle(title : String) : Page = {
    val code = mediaWikiAPI.getWikitextFromTitle("en", title)
    miner.mineInternalRepresentation(language, code, title)
  }

  def parseFromOfflineCode(title : String) : Page = {
    val code = Source.fromFile("input/" + title.replaceAll(" ", "_") + ".txt").getLines.mkString("\n")
    miner.mineInternalRepresentation(language, code, title)
  }

  def testArticle(title : String) : Page = {
    val pcm = parseFromOfflineCode(title)
    writeToHTML(title, pcm)
    dumpCellsInFile(title, pcm)
    writeToCSV(title, pcm)
    writeToPCM(title, pcm)
    writeToWikiText(title, pcm)
    pcm
  }

  def writeToFile(path : String, content: String): Unit = {
    val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))
    writer.write(content)
    writer.close()
  }

  def writeToHTML(title : String, pcm : Page) {
    val path = "output/html/" + title.replaceAll(" ", "_") + ".html"
    val content = (new PrettyPrinter(80,2)).format(pcm.toHTML)
    writeToFile(path, content)
  }

  def dumpCellsInFile(title : String, pcm : Page) {
    val writer = new FileWriter("output/dump/" + title.replaceAll(" ", "_") + ".txt")
    for(matrix <- pcm.getMatrices;
        row <- 0 until matrix.getNumberOfRows;
        column <- 0 until matrix.getNumberOfColumns) {
      val cell = matrix.getCell(row, column)
      if (cell.isDefined) {
        val content = cell.get.content
        val words = for (word <- content.split("\\s") if !word.isEmpty()) yield word
        val formattedContent = words.mkString("", " ", "").toLowerCase()
        writer.write(formattedContent + "\n")
      }
    }
    writer.close()
  }

  def writeToCSV(title : String, pcm : Page) {
    val path = "output/csv/" + title.replaceAll(" ", "_") + ".csv"
    val content = pcm.toCSV
    writeToFile(path, content)
  }

  def writeToPCM(title : String, page : Page) {
    val exporter = new PCMModelExporter
    val pcms = exporter.export(page)
    //    val serializer = new PCMtoHTML
    //    writer.write(serializer.toHTML(pcm))
    val serializer = new KMFJSONExporter
    val loader = new KMFJSONLoader
    for ((pcm, index) <- pcms.zipWithIndex) {
      val path = "output/model/" + title.replaceAll(" ", "_") + "_" + index + ".pcm"
      val content = serializer.export(pcm)
      writeToFile(path, content)
      loader.load(new File(path))
    }

  }

  def writeToWikiText(title : String, page : Page) {
    val exporter = new PCMModelExporter
    val pcms = exporter.export(page)

    val serializer = new WikiTextExporter

    for ((pcm, index) <- pcms.zipWithIndex) {
      val wikitext = serializer.export(pcm)
      val path = "output/wikitext/" + title.replaceAll(" ", "_") +  "_" + index + ".txt"
      writeToFile(path, wikitext)
    }

  }

  ignore should "download every available Wikipedia PCM" in {
    val wikipediaPCMsFile = Source.fromFile("resources/list_of_PCMs.txt")
    val wikipediaPCMs = wikipediaPCMsFile.getLines.toList
    wikipediaPCMsFile.close

    val tasks : Seq[Future[String]] = for(article <- wikipediaPCMs) yield future {
      var result = new StringBuilder
      if (article.startsWith("//")) {
        result ++= "IGNORED : " + article
      } else {
        result ++= article
        var retry = false
        do {
          try {

            // Download Wikipedia page code
            val code = mediaWikiAPI.getWikitextFromTitle("en", article)

            // Save code
            writeToFile("input/" + article.replaceAll(" ", "_") + ".txt", code)

          } catch {
            // case e : UnknownHostException => retry = true
            case e : Throwable =>
              val sw = new StringWriter();
              val pw = new PrintWriter(sw);
              e.printStackTrace(pw);
              result ++= sw.toString();
          }
        } while (retry)
      }
      result.toString
    } (executionContext)

    for (task <- tasks) {
      val result = Await.result(task, 10.minutes)
      //      println(result)
    }
  }

  it should "parse every available PCM in Wikipedia" in {
    val wikipediaPCMsFile = Source.fromFile("resources/list_of_PCMs.txt")
    val wikipediaPCMs = wikipediaPCMsFile.getLines.toList
    wikipediaPCMsFile.close

    for(article <- wikipediaPCMs) {
      if (article.startsWith("//")) {
        println("IGNORED : " + article)
      } else {
        println(article)
        try {

          // Parse preprocessed Wikipedia page
          val pcms = testArticle(article)

        } catch {
          case e : Throwable => e.printStackTrace()
        }
      }
    }

  }

  it should "interpret the cell of all the Wikipedia PCMs" in {

    val interpreter = new CellContentInterpreter
    val loader = new KMFJSONLoader
    val exporter = new KMFJSONExporter
    val csvExporter = new CSVExporter
    val factory = new PCMFactoryImpl

    val files = new File("output/model").listFiles(new FilenameFilter {
      override def accept(dir: File, name: String): Boolean = name.endsWith(".pcm")
    })

    // Create output directory
    new File("output/formalized/model").mkdirs()
    new File("output/formalized/csv").mkdirs()


    // Interpret cells for every PCM
    for (file <- files) {

      println(file.getName)

      // Interpret cells
      val pcmContainer = loader.load(file)(0)
      val pcm = pcmContainer.getPcm
      if (pcm.isValid) {
        pcm.normalize(factory)
        interpreter.interpretCells(pcm)
        val json = exporter.export(pcmContainer)

        val csv = csvExporter.export(pcmContainer)

        // Write modified PCM
        writeToFile("output/formalized/model/" + file.getName, json)
        writeToFile("output/formalized/csv/" + file.getName.replaceAll(".pcm", ".csv"), csv)
      }

    }

    // Interpret and merge PCMs
//    val groupedFiles = files.groupBy(f => f.getName.substring(0, f.getName.size - 6))
//    for (group <- groupedFiles) {
//      val mergedPCM = factory.createPCM();
//      mergedPCM.setName(group._1)
//
//      var error = false
//
//      for (file <- group._2) {
//        val pcm = loader.load(file)(0).getPcm
//        if (pcm.isValid) {
//          interpreter.interpretCells(pcm)
//          try {
//            mergedPCM.merge(pcm, factory)
//          } catch {
//            case e : MergeConflictException => error = true
//          }
//
//        } else {
//          error = true
//        }
//      }

//      if (!error) {
//        val json = exporter.export(mergedPCM)
//        val writer = new FileWriter("output/model/" + mergedPCM.getName)
//        writer.write(json)
//        writer.close()
//      }

//    }
  }

}
