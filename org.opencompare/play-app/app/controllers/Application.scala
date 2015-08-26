package controllers

import model.{Database, PCMInfo}
import play.api.i18n.{Messages, Lang, MessagesApi, I18nSupport}
import play.api.mvc.{Controller, Action}

import collection.JavaConversions._

import javax.inject._

@Singleton
class Application @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {


    def index = Action { implicit request =>
      Ok(views.html.index())
    }

    def aboutProject = Action { implicit request =>
        Ok(views.html.aboutProject())
    }

    def aboutPrivacyPolicy = Action { implicit request =>
        Ok(views.html.aboutPrivacyPolicy())
    }


    def list(limit : Int, page : Int) = Action { implicit request =>
        val pcms = Database.list(limit, page).toList
        val count = Database.count().toInt
        var nbPages = count / limit
        if (count % limit != 0) {
            nbPages = nbPages + 1
        }
        Ok(views.html.list(pcms, limit, page, nbPages))
    }

    def search(searchedString : String) = Action { implicit request =>

        // TODO : find PCMs named "request" or with a product named "request"
        val results = Database.search(searchedString).toList

        Ok(views.html.search(searchedString, results))
    }


    def edit(id : String) = Action { implicit request =>
        val exists = Database.exists(id)
        if (exists) {
            Ok(views.html.edit(id, null, null))
        } else {
            Ok(views.html.edit(null, null, null))
        }

    }

    def create = Action { implicit request =>
        Ok(views.html.create(null, null, null))
    }

    def importer(ext : String) = Action { implicit request =>
        ext match {
            case "csv" => Ok(views.html.edit(null, null, "CsvImport"))
            case "html" => Ok(views.html.edit(null, null, "HtmlImport"))
            case "wikipedia" => Ok(views.html.edit(null, null, "WikipediaImport"))
            case _ => NotFound
        }
    }

    def embedPCM(id : String) = Action { implicit request =>
        val exists = Database.exists(id)
        if (exists) {
            Ok(views.html.embed(id, null, null))
        } else {
            Ok(views.html.embed(null, null, null))
        }

    }

    def embed = Action { implicit request =>
        Ok(views.html.embed(null, null, null))
    }

}
