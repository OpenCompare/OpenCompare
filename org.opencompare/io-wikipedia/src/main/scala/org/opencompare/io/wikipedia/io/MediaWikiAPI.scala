package org.opencompare.io.wikipedia.io

import play.api.libs.json.{JsString, JsValue, Json}

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

  def getRevisionFromTitle(language : String, title : String, limit : Integer = 10000): List[JsValue] = {
    //Example: https://en.wikipedia.org/w/w/api.php?action=query&prop=revisions&format=json&rvprop=timestamp%7Cuser%7Ccontentmodel%7Ccontent&rvlimit=50&rvdir=older&rawcontinue=&titles=Comparison_(grammar)
    var revs: List[JsValue] = List[JsValue]() // final result
    var currentRevs = List.empty[JsValue] // transitionnal result
    var rvcontinue = ""; // Mandatory to paginate
    do {
      val result = Http(apiEndPoint(language)).params(
        "action" -> "query",
        "format" -> "json",
        "limit" -> "50",
        "rvcontinue" -> rvcontinue,
        "prop" -> "revisions",
        "titles" -> escapeTitle(title),
        "rvprop" -> "ids|timestamp|user|contentmodel|content"
      ).asString.body

      val jsonResult = Json.parse(result)
      val pages = jsonResult \ "query" \ "pages"
      currentRevs = (pages \\ "revisions").toList
      revs = revs ::: currentRevs
      rvcontinue = (jsonResult \ "continue" \ "rvcontinue").toString
    } while (currentRevs.nonEmpty)
    revs
  }

}
