package controllers

import model.{Database, PCMInfo}
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.mvc.{Controller, Action}

import collection.JavaConversions._

import javax.inject._

@Singleton
class Application @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {


    def index = Action {
        Ok(views.html.index())
    }

    def aboutProject = Action {
        Ok(views.html.aboutProject())
    }

    def aboutPrivacyPolicy = Action {
        Ok(views.html.aboutPrivacyPolicy())
    }


    def list(limit : Int, page : Int) = Action {
        val pcms = Database.INSTANCE.list(limit, page).toList
        val count = Database.INSTANCE.count().toInt
        var nbPages = count / limit
        if (count % limit != 0) {
            nbPages = nbPages + 1
        }
        Ok(views.html.list(pcms, limit, page, nbPages))
    }

    def search(request : String) = Action {

        // TODO : find PCMs named "request" or with a product named "request"
        val results = Database.INSTANCE.search(request).toList

        Ok(views.html.search(request, results))
    }


    def edit(id : String) = Action {
        val exists = Database.INSTANCE.exists(id)
        if (exists) {
            Ok(views.html.edit(id, null, null))
        } else {
            Ok(views.html.edit(null, null, null))
        }

    }

    def create = Action {
        Ok(views.html.create(null, null, null))
    }

    def importer(ext : String) = Action {
        ext match {
            case "csv" => Ok(views.html.edit(null, null, "CsvImport"))
            case "html" => Ok(views.html.edit(null, null, "HtmlImport"))
            case "wikipedia" => Ok(views.html.edit(null, null, "WikipediaImport"))
            case _ => NotFound
        }
    }

    def embedPCM(id : String) = Action {
        val exists = Database.INSTANCE.exists(id)
        if (exists) {
            Ok(views.html.embed(id, null, null))
        } else {
            Ok(views.html.embed(null, null, null))
        }

    }

    def embed = Action {
        Ok(views.html.embed(null, null, null))
    }

}
