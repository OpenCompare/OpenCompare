package controllers.io

import java.net.{MalformedURLException, URL, URLDecoder}
import java.nio.charset.StandardCharsets
import javax.inject.Inject

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import controllers.{ResultFormat, ViewContext}
import models.{PCMAPIUtils, User}
import org.opencompare.api.java.PCMFactory
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.io.wikipedia.io.{MediaWikiAPI, WikiTextExporter, WikiTextLoader, WikiTextTemplateProcessor}
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc._

import scala.collection.JavaConversions._

/**
 * Created by gbecan on 8/18/15.
 */
class MediaWikiCtrl @Inject() (
                                val messagesApi: MessagesApi,
                                val env: Environment[User, CookieAuthenticator],
                                val pcmAPIUtils : PCMAPIUtils) extends IOCtrl("mediawiki") {

  val inputParametersForm = Form(
    mapping(
      "url" -> nonEmptyText
    )(MediaWikiImportParameters.apply)(MediaWikiImportParameters.unapply)
  )

  val outputParametersForm = Form(
    mapping(
      "productAsLines" -> boolean,
      "file" -> text
    )(MediaWikiExportParameters.apply)(MediaWikiExportParameters.unapply)
  )
  private val pcmFactory: PCMFactory = new PCMFactoryImpl
  private val mediaWikiAPI: MediaWikiAPI = new MediaWikiAPI("wikipedia.org")
  private val wikitextTemplateProcessor: WikiTextTemplateProcessor = new WikiTextTemplateProcessor(mediaWikiAPI)
  private val miner: WikiTextLoader = new WikiTextLoader(wikitextTemplateProcessor)

  private val wikiExporter: WikiTextExporter = new WikiTextExporter(true)

  override def importPCMs(format : ResultFormat)(implicit request: Request[AnyContent], viewContext: ViewContext) : Result = {

    val parameters = inputParametersForm.bindFromRequest.get
    val url = parameters.url

    try {
      val pageURL = new URL(url)
      val host = pageURL.getHost
      val language = host.substring(0, host.indexOf('.'))
      var file = URLDecoder.decode(pageURL.getFile, StandardCharsets.UTF_8.name)
      if (file.endsWith("/")) {
        file = file.substring(0, file.length - 1)
      }
      val title = file.substring(file.lastIndexOf('/') + 1)


      // Parse article from Wikipedia
      val code = mediaWikiAPI.getWikitextFromTitle(language, title)

      val pcmContainers = miner.mine(language, code, title).toList
      for (pcmContainer <- pcmContainers) {
        pcmContainer.getMetadata.setSource(url)
        if (url.contains("wikipedia.org")) {
          pcmContainer.getMetadata.setLicense("Creative Commons Attribution-ShareAlike 3.0 Unported")
        }
      }


      if (pcmContainers.isEmpty) {
        NotFound("No matrices were found in this Wikipedia page")
      } else {

        val jsonResult = postprocessContainers(pcmContainers)

        Ok(jsonResult)
      }
    }
    catch {
      case e: MalformedURLException => NotFound("URL is not a valid Wikipedia page")
      case e: Exception => NotFound("The page has not been found.") // TODO: manage the different kind of exceptions
    }

  }

  override def exportPCM(implicit request : Request[AnyContent]) : Result = {
    val parameters = outputParametersForm.bindFromRequest.get
    val pcmJSON = Json.parse(parameters.pcm)

    val container = pcmAPIUtils.parsePCMContainers(pcmJSON).head
    container.getMetadata.setProductAsLines(parameters.productAsLines)

    val wikitext = wikiExporter.export(container)
    
    Ok(wikitext)
  }

}


case class MediaWikiImportParameters(
                                url : String
                                )

case class MediaWikiExportParameters(
                                productAsLines : Boolean,
                                pcm : String
                                )