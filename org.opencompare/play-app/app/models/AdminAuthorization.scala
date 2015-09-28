package models

import com.mohiva.play.silhouette.api.Authorization
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import play.api.i18n.Messages
import play.api.mvc.Request
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future

case class AdminAuthorization() extends Authorization[User, CookieAuthenticator] {
  override def isAuthorized[B](identity: User, authenticator: CookieAuthenticator)(implicit request: Request[B], messages: Messages): Future[Boolean] = Future {
    identity.role match {
      case AdminRole() => true
      case _ => false
    }
  }
}
