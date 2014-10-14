package org.diverse.pcm.io.wikipedia

import java.io.{File, FileWriter, PrintWriter, StringWriter}
import java.util.concurrent.Executors

import org.diverse.pcm.api.java.export.PCMtoHTML
import org.diverse.pcm.io.wikipedia.parser.WikipediaPCMParser
import org.diverse.pcm.io.wikipedia.pcm.{Page, WikipediaPageMiner}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, _}
import scala.io.Source
import scala.xml.PrettyPrinter

class ParserTest extends FlatSpec with Matchers with BeforeAndAfterAll {
  
  val executionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(20))

  override def beforeAll() {

    new File("input/").mkdirs()
    new File("output/csv/").mkdirs()
    new File("output/html/").mkdirs()
    new File("output/dump/").mkdirs()
    new File("output/model/").mkdirs()
  }

  def parsePCMFromFile(file : String) : Page= {
    val reader= Source.fromFile(file)
    val code = reader.mkString
    reader.close
    val parser = new WikipediaPCMParser
    parser.preprocessAndParse(code)
  }
  
  def parseFromTitle(title : String) : Page = {
    (new WikipediaPCMParser).parseOnlineArticle(title)
  }
  
  def parseFromOfflineCode(title : String) : Page = {
    val parser = new WikipediaPCMParser
    val code = Source.fromFile("input/" + title.replaceAll(" ", "_") + ".txt").getLines.mkString("\n")
    parser.parse(code)
  }
  
  def testArticle(title : String) : Page = {
    val pcm = parseFromOfflineCode(title)
    writeToHTML(title, pcm)
    dumpCellsInFile(title, pcm)
    writeToCSV(title, pcm)
    writeToPCM(title, pcm)
    pcm
  }
  
  def writeToHTML(title : String, pcm : Page) {
    val writer = new FileWriter("output/html/" + title.replaceAll(" ", "_") + ".html")
    writer.write((new PrettyPrinter(80,2)).format(pcm.toHTML))
    writer.close()
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
    val writer = new FileWriter("output/csv/" + title.replaceAll(" ", "_") + ".csv")
    writer.write(pcm.toCSV)
    writer.close()
  }

  def writeToPCM(title : String, page : Page) {
    val writer = new FileWriter("output/model/" + title.replaceAll(" ", "_") + ".pcm")
    val miner = new WikipediaPageMiner
    val pcm = miner.toPCM(page)
    val serializer = new PCMtoHTML
    writer.write(serializer.toHTML(pcm))
    writer.close()
  }
  
  "The PCM parser" should "parse the example of tables from Wikipedia" in {
    val pcm = parsePCMFromFile("resources/example.pcm")
//    println(pcm)
    pcm.getMatrices.size should be (1)
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
            val parser = new WikipediaPCMParser
            val preprocessedCode = parser.preprocessOnlineArticle(article)
            val writer = new FileWriter("input/" + article.replaceAll(" ", "_") + ".txt")
            writer.write(preprocessedCode)
            writer.close()
          } catch {
            //	    		 case e : UnknownHostException => retry = true
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

  ignore should "parse every available PCM in Wikipedia" in {
    val wikipediaPCMsFile = Source.fromFile("resources/list_of_PCMs.txt")
    val wikipediaPCMs = wikipediaPCMsFile.getLines.toList
    wikipediaPCMsFile.close

    for(article <- wikipediaPCMs) {
      if (article.startsWith("//")) {
        println("IGNORED : " + article)
      } else {
        println(article)
        try {
          val pcms = testArticle(article)
        } catch {
          case e : Throwable => e.printStackTrace()
        }
      }
    }

  }

//   it should "parse the same PCM from a URL and from a file containing the code" in {
//     val fromFile = parsePCMFromFile("resources/amd.pcm")
//     val fromURL = parseFromTitle("Comparison_of_AMD_processors")
//
//     fromFile.getMatrices.size should be (1)
//     fromURL.getMatrices.size should be (1)
//     fromFile.getMatrices(0).toString should be (fromURL.getMatrices(0).toString)
//
//   }



   it should "parse these PCMs" in {
	   val wikipediaPCMs = Source.fromFile("resources/pcms_to_test.txt").getLines.toList
	   for(article <- wikipediaPCMs) yield {
       println(article)
	     testArticle(article)
     }
   }
   
   


//   "Scalaj-http" should "download the code of a wikipedia page" in {
//	   val xmlPage = Http("http://en.wikipedia.org/w/index.php?title=Comparison_of_AMD_processors&action=edit")
//	   .option(HttpOptions.connTimeout(1000))
//	   .option(HttpOptions.readTimeout(10000))
//	   .asXml
//	   //XML.load("http://en.wikipedia.org/w/index.php?title=Comparison_of_AMD_processors&action=edit")
//	   val code = (xmlPage \\ "textarea").text
//
//	   val expectedCode = Source.fromFile("resources/amd.pcm").mkString
//	   code should be (expectedCode)
//
//   }
//
//   it should "download the code of a wikipedia template" in {
//     val xmlPage = Http.post("https://en.wikipedia.org/wiki/Special:ExpandTemplates")
//     .params("wpInput" -> "{{yes}}")
//     .asXml
//
//     val code = (xmlPage \\ "textarea").filter(_.attribute("id") exists (_.text == "output")).text
//     code should be ("style=\"background: #90ff90; color: black; vertical-align: middle; text-align: center; \" class=\"table-yes\"|Yes")
//   }
}
