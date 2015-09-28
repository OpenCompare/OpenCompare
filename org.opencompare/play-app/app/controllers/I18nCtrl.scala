package controllers

import javax.inject.{Inject, Singleton}

import jsmessages.JsMessagesFactory
import play.api.i18n.{I18nSupport, Lang, MessagesApi}
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{Action, Controller}

/**
 * Created by gbecan on 8/19/15.
 */

@Singleton
class I18nCtrl @Inject() (val messagesApi : MessagesApi, jsMessagesFactory: JsMessagesFactory) extends Controller with I18nSupport {

//  protected val HOME_URL = "/"

  val jsMessages = jsMessagesFactory.all

  def setLang(language : String) = Action { implicit request =>
//    val referrer = request.headers.get(REFERER).getOrElse(HOME_URL)

    if (jsMessages.allMessages.contains(language)) {
//      Redirect(referrer).withSession("lang" -> language)
      Ok(language).withLang(Lang.get(language).get)
    } else {
//      Redirect(referrer).withSession("lang" -> "en")
      Ok(I18nCtrl.defaultLanguage).clearingLang
    }

  }

  def i18n = Action { implicit request =>
    val language = messagesApi.preferred(request).lang.code
    val messages = jsMessages.allMessagesJson.as[JsObject].value(language)
    Ok(Json.stringify(messages))
  }

}

object I18nCtrl {
  val defaultLanguage = "en"
}
