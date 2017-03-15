package controllers

import java.util.Date
import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import com.mongodb.casbah.Imports._
import models.{Database, Feedback, User}
import models.services.FeedbackService
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import forms.FeedbackForm

/**
 * Created by gbecan on 9/30/15.
 */
@Singleton
class FeedbackController @Inject() (
    val messagesApi: MessagesApi,
    val env: Environment[User, CookieAuthenticator],
    feedbackService: FeedbackService)
    extends BaseController {

    def sendFeedback = UserAwareAction { implicit request =>
        val json = Json.obj()
        FeedbackForm.form.bindFromRequest.fold(
            form => Ok(Json.obj("error" -> true)),
            data => {
                val feedback = Feedback(email=data.email, subject=data.subject, content=data.content, pcmid=data.pcmid, date=new Date())
                feedbackService.save(feedback)
                Ok(Json.obj("error" -> false))
            }
        )
    }

    def list(limit : Int, page : Int) = UserAwareAction { implicit request =>
        val cursor = Database.db("feedbacks").find()
            .sort(MongoDBObject("_id" -> 1))
            .skip(limit * (page-1))
            .limit(limit)
        val feedbacks = for (fb <- cursor) yield {
            val email = fb("email").toString
            val subject = fb("subject").toString
            val content = fb("content").toString
            val pcmid = fb.getAs[String]("pcmid").getOrElse("Unknown ID")
            val date = fb.getAs[Date]("date").getOrElse(new Date())
            new Feedback(email=email, subject=subject, content=content, pcmid=pcmid, date=date)
        }
        val count = Database.db("feedbacks").count().toInt
        var nbPages = count / limit
        if (count % limit != 0) {
            nbPages = nbPages + 1
        }
        Ok(views.html.listFeedbacks(feedbacks.toList, limit, page, nbPages))
    }

}
