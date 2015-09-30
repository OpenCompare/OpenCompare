package controllers

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.User
import play.api.i18n.I18nSupport
import play.api.mvc.Controller

/**
 * Created by gbecan on 9/25/15.
 */
abstract class BaseController extends Controller with I18nSupport with Silhouette[User, CookieAuthenticator] {

  implicit def userAwareRequestToViewContext[R](implicit request: UserAwareRequest[R]): ViewContext = ViewContext(request.identity, request.request)
  implicit def securedRequestToViewContext[R](implicit request: SecuredRequest[R]): ViewContext = ViewContext(Some(request.identity), request.request)


}

