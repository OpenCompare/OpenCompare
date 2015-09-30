package controllers

import javax.inject.{Singleton, Inject}

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.User
import play.api.i18n.MessagesApi

/**
 * Created by gbecan on 9/30/15.
 */
@Singleton
class ProfileController @Inject() (val messagesApi: MessagesApi, val env: Environment[User, CookieAuthenticator]) extends BaseController {

  def profile = SecuredAction { implicit request =>
    Ok(views.html.profile())
  }

}
