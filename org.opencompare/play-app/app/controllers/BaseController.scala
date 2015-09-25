package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.{Environment, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.User
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.mvc.{Controller, RequestHeader}
import play.mvc.Http.Request

/**
 * Created by gbecan on 9/25/15.
 */
abstract class BaseController extends Controller with I18nSupport with Silhouette[User, CookieAuthenticator] {

  implicit def viewContext[R](implicit request: UserAwareRequest[R]): ViewContext = ViewContext(request.identity, request.request)


}

