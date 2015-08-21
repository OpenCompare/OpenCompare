package org.opencompare.io.wikipedia.io

import java.net.SocketTimeoutException

import play.api.libs.json._

import scala.collection.mutable.ListBuffer
import scalaj.http.Http

/**
 * Created by gbecan on 6/19/15.
 */
class MediaWikiAPI(
                  val protocol : String = "https",
                  val baseServer : String
                    ) {

  // Constructor for Java compatibility with optional parameters
  def this(initBaseServer : String) {
    this("https", initBaseServer)
  }

  private def apiEndPoint(language : String) : String = {
    protocol + "://" + language + "." + baseServer + "/w/api.php"
  }

  private def escapeTitle(title : String) : String = {
    title.replaceAll("\\s", "_")
  }

  def call(language: String, params: Map[String, String]): Option[String] = {
    val result = try {
      var paramedQuery = Http(apiEndPoint(language)).params(params)
      paramedQuery.asString.body
    } catch {
      case _ : SocketTimeoutException => {
        try {
          this.wait(800)
        } catch {
          case _ : Throwable =>
        }
        call(language, params).get
      }
    }
    Some(result)
  }

  def getWikitextFromTitle(language : String, title : String): String = {
    //Example: https://en.wikipedia.org/w/api.php?action=query&format=json&prop=revisions&titles=Comparison_of_AMD_processors&rvprop=content
    val params = Map(
      "action" -> "query",
      "format" -> "json",
      "prop" -> "revisions",
      "titles" -> escapeTitle(title),
      "rvprop" -> "content",
      "redirects" -> ""
    )
    val result = call(language, params)
    val wikitext = if (result.isDefined) {
      val jsonResult = Json.parse(result.get)
      val jsonWikitext = jsonResult \ "query" \ "pages" \\ "*"

      if (jsonWikitext.nonEmpty) {
        jsonWikitext.head.as[JsString].value
      } else {
        // TODO: Error
        ""
      }
    } else {
      ""
    }
    wikitext
  }

  def expandTemplate(language : String, template : String) : String = {
    val params = Map(
      "action" -> "expandtemplates",
      "format" -> "json",
      "prop" -> "wikitext",
      "text" -> template
    )

    val result = call(language, params)
    val expandedTemplate = if (result.isDefined) {
      val jsonResult = Json.parse(result.get)
      val jsonExpandedTemplate = (jsonResult \ "expandtemplates" \ "wikitext").toOption
      if (jsonExpandedTemplate.isDefined) {
        jsonExpandedTemplate.get match {
          case s: JsString => s.value
          case _ => ""
        }
      } else {
        ""
      }
    } else {
      ""
    }
    expandedTemplate
  }

  def getRevisionFromTitle(language : String, title : String, direction : String = "older"): List[JsObject] = {
    //Example: https://en.wikipedia.org/w/w/api.php?action=query&prop=revisions&format=json&rvprop=timestamp%7Cuser%7Ccontentmodel%7Ccontent&rvlimit=50&rvdir=older&titles=Comparison_(grammar)
    getCompleteRevisionHistory(language, title, direction, "").toList
  }

  private def getCompleteRevisionHistory(language : String, title : String, direction : String = "older", rvcontinue : String = ""): ListBuffer[JsObject] = {
    val revs = ListBuffer.empty[JsObject] // final result
    val baseParams = Map(
      "action" -> "query",
      "format" -> "json",
      "rvlimit" -> "50",
      "rvdir" -> direction,
      "rawcontinue" -> "",
      "prop" -> "revisions",
      "titles" -> escapeTitle(title),
      "rvprop" -> "ids|timestamp|user|comment|tags|flags",
      "redirects" -> ""
    )
    val params = if (rvcontinue != "") {
      baseParams + ("rvcontinue" -> rvcontinue)
    } else {
      baseParams
    }
    val result = call(language, params)
    if (result.isDefined) {
      val jsonResult = Json.parse(result.get)
      val page = jsonResult \ "query" \ "pages"
      (page \\ "revisions").foreach {
        case JsArray(revisionsSeq) =>
          revisionsSeq.foreach(rev => {
            revs.append(rev.asInstanceOf[JsObject])
          })
        case _ => println("failed")
      }

      // Recursive call only if not completed
      if (!(jsonResult \ "query-continue").isInstanceOf[JsUndefined]) {
        val rvcontinue = (jsonResult \ "query-continue" \ "revisions" \ "rvcontinue").get.toString().replaceAllLiterally("\"", "")
        val result = getCompleteRevisionHistory(language, title, direction, rvcontinue)
        result.foreach(rev => {
          revs.append(rev)
        })
      }
    }
    revs
  }

  def getContentFromRevision(language : String, id : Int): String = {

    val params = Map[String, String](
      "action" -> "query",
      "format" -> "json",
      "prop" -> "revisions",
      "revids" -> id.toString,
      "rvprop" -> "contentmodel|content",
      "redirects" -> ""
    )

    val result = call(language, params)

    val content = if (result.isDefined) {
      val jsonResult = Json.parse(result.get)
      val revisions = jsonResult \ "query" \ "pages" \\ "revisions"
      val revision = revisions.head(0)
      (revision \ "*").as[JsString].value
    } else {
      ""
    }

    content
  }

}
