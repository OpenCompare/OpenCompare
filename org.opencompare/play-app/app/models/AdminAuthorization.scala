package models

import com.mohiva.play.silhouette.api.Authorization
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import play.api.i18n.Messages
import play.api.mvc.Request

import scala.concurrent.Future

case class AdminAuthorization() extends Authorization[User, CookieAuthenticator] {
  override def isAuthorized[B](identity: User, authenticator: CookieAuthenticator)(implicit request: Request[B], messages: Messages): Future[Boolean] = {
    Future.successful(identity.email.getOrElse("") == "admin@opencompare.org")
  }
}
