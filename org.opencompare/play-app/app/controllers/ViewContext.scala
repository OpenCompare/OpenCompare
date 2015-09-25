package controllers

import models.User
import play.api.mvc.RequestHeader

case class ViewContext(user: Option[User], request: RequestHeader)
