package org.opencompare.io.wikipedia.io

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

  def getWikitextFromTitle(language : String, title : String): String = {
    //Example: https://en.wikipedia.org/w/api.php?action=query&format=json&prop=revisions&titles=Comparison_of_AMD_processors&rvprop=content
    val result = Http(apiEndPoint(language)).params(
      "action" -> "query",
      "format" -> "json",
      "prop" -> "revisions",
      "titles" -> escapeTitle(title),
      "rvprop" -> "content"
    ).asString.body

    val jsonResult = Json.parse(result)
    val jsonWikitext = jsonResult \ "query" \ "pages" \\ "*"

    if (jsonWikitext.nonEmpty) {
      jsonWikitext.head.as[JsString].value
//      Json.stringify(jsonWikitext.head)
//        .replaceAll(Matcher.quoteReplacement("\\n"), "\n")
//        .replaceAll(Matcher.quoteReplacement("\\\""), "\"")
    } else {
      // TODO: Error
      ""
    }
  }

  def expandTemplate(language : String, template : String) : String = {
    val result = Http(apiEndPoint(language)).params(
      "action" -> "expandtemplates",
      "format" -> "json",
      "prop" -> "wikitext",
      "text" -> template
    ).asString.body

    val jsonResult = Json.parse(result)
    val jsonExpandedTemplate = (jsonResult \ "expandtemplates" \ "wikitext").toOption
    val expandedTemplate = if (jsonExpandedTemplate.isDefined) {
      jsonExpandedTemplate.get match {
        case s : JsString => s.value
        case _ => ""
      }
    } else {
      ""
    }

    expandedTemplate
  }

  def getRevisionFromTitle(language : String, title : String, limit : Integer = 50): List[JsObject] = {
    //Example: https://en.wikipedia.org/w/w/api.php?action=query&prop=revisions&format=json&rvprop=timestamp%7Cuser%7Ccontentmodel%7Ccontent&rvlimit=50&rvdir=older&rawcontinue=&titles=Comparison_(grammar)
    val revs = ListBuffer.empty[JsObject] // final result
    var rvcontinue = "" // Mandatory to paginate
    val query = Http(apiEndPoint(language))
    val baseParams = Map(
      "action" -> "query",
      "format" -> "json",
      "rvlimit" -> "50",
      "rvdir" -> "older",
      "rawcontinue" -> "",
      "prop" -> "revisions",
      "titles" -> escapeTitle(title),
      "rvprop" -> "ids|timestamp|user|contentmodel|content"
    )
    do {
      val params = if (rvcontinue != "") {
        baseParams + ("rvcontinue" -> rvcontinue)
      } else {
        baseParams
      }
      val paramedQuery = query.params(params)
      val result = paramedQuery.asString.body

      val jsonResult = Json.parse(result)
      val page = jsonResult \ "query" \ "pages"

      (page \\ "revisions").foreach { revisions =>
        revisions match {
          case JsArray(revisionsSeq) => {
            revisionsSeq.foreach(rev => {
              revs += rev.asInstanceOf[JsObject]
            })
          }
          case _ => println("failed")
        }
      }

      val queryContinue = jsonResult \ "query-continue"
      if (queryContinue.isInstanceOf[JsUndefined]) {
        rvcontinue = ""
      } else {
        rvcontinue = (queryContinue \ "revisions" \ "rvcontinue").get.toString()
      }
    } while (rvcontinue != "")
    revs.toList
  }


}
