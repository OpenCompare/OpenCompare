package org.opencompare.dataset.wikipedia

import java.io._
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors

import org.opencompare.api.java.exception.MergeConflictException
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.impl.io.{KMFJSONExporter, KMFJSONLoader}
import org.opencompare.formalizer.extractor.CellContentInterpreter
import org.opencompare.io.wikipedia.WikipediaPageMiner
import org.opencompare.io.wikipedia.export.{WikiTextExporter, PCMModelExporter}
import org.opencompare.io.wikipedia.pcm.Page
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.concurrent._
import scala.concurrent.duration._
import scala.io.Source
import scala.xml.PrettyPrinter

/**
 * Created by gbecan on 4/27/15.
 */
class WikipediaDatasetTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val executionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(20))
  val miner = new WikipediaPageMiner

  override def beforeAll() {

    new File("input/").mkdirs()
    new File("output/csv/").mkdirs()
    new File("output/html/").mkdirs()
    new File("output/dump/").mkdirs()
    new File("output/model/").mkdirs()
    new File("output/wikitext/").mkdirs()
  }

  def parsePCMFromFile(file : String) : Page= {
    val reader= Source.fromFile(file)
    val code = reader.mkString
    reader.close
    val preprocessedCode = miner.preprocess(code)
    miner.parse(preprocessedCode, file)
  }

  def parseFromTitle(title : String) : Page = {
    val code = miner.getPageCodeFromWikipedia(title)
    val preprocessedCode = miner.preprocess(code)
    miner.parse(preprocessedCode, title)
  }

  def parseFromOfflineCode(title : String) : Page = {
    val code = Source.fromFile("input/" + title.replaceAll(" ", "_") + ".txt").getLines.mkString("\n")
    miner.parse(code, title)
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
      val content = serializer.toJson(pcm)
      writeToFile(path, content)
      loader.load(new File(path))
    }

  }

  def writeToWikiText(title : String, page : Page) {
    val exporter = new PCMModelExporter
    val pcms = exporter.export(page)

    val serializer = new WikiTextExporter

    for ((pcm, index) <- pcms.zipWithIndex) {
      val wikitext = serializer.toWikiText(pcm)
      val path = "output/wikitext/" + title.replaceAll(" ", "_") +  "_" + index + ".txt"
      writeToFile(path, wikitext)
    }

  }

  ignore should "preprocess every available Wikipedia PCM" in {
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

            // Preprocess Wikipedia page
            val code = miner.getPageCodeFromWikipedia(article)
            val preprocessedCode = miner.preprocess(code)

            // Save preprocessed page
            writeToFile("input/" + article.replaceAll(" ", "_") + ".txt", preprocessedCode)

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

  "Wikipedia IO" should "parse every available PCM in Wikipedia" in {
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



  "Formalizer" should "interpret the cell of all the Wikipedia PCMs" in {

    val interpreter = new CellContentInterpreter
    val loader = new KMFJSONLoader
    val exporter = new KMFJSONExporter
    val factory = new PCMFactoryImpl

    val files = new File("output/model").listFiles(new FilenameFilter {
      override def accept(dir: File, name: String): Boolean = name.endsWith(".pcm")
    })

    // Create output directory
    new File("output/formalized/model").mkdirs()


    // Interpret cells for every PCM
    for (file <- files) {
      // Interpret cells
      val pcm = loader.load(file)
      if (pcm.isValid) {
        pcm.normalize(factory)
        interpreter.interpretCells(pcm)
        val json = exporter.export(pcm)

        // Write modified PCM
        writeToFile("output/formalized/model/" + file.getName, json)
      }

    }

    // Interpret and merge PCMs
    val groupedFiles = files.groupBy(f => f.getName.substring(0, f.getName.size - 6))
    for (group <- groupedFiles) {
      val mergedPCM = factory.createPCM();
      mergedPCM.setName(group._1)

      var error = false

      for (file <- group._2) {
        val pcm = loader.load(file)
        if (pcm.isValid) {
          interpreter.interpretCells(pcm)
          try {
            mergedPCM.merge(pcm, factory)
          } catch {
            case e : MergeConflictException => error = true
          }

        } else {
          error = true
        }
      }

      //      if (!error) {
      //        val json = exporter.export(mergedPCM)
      //        val writer = new FileWriter("output/model/" + mergedPCM.getName)
      //        writer.write(json)
      //        writer.close()
      //      }

    }
  }

}
