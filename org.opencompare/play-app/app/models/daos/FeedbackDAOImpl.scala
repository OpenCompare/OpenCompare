package models.daos

import com.mongodb.casbah.Imports._
import models.daos.FeedbackDAOImpl._
import models.{Database, Feedback}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/**
 * Give access to the feedback object.
 */
class FeedbackDAOImpl extends FeedbackDAO {

  /**
   * Saves a feedback.
   *
   * @param feedback The feedback to save.
   * @return The saved feedback.
   */
  override def save(feedback: Feedback) = Future {
      feedbacks.insert(convertToDB(feedback))
      feedback
  }

  private def convertToDB(feedback : Feedback) : DBObject = {
    MongoDBObject(
      "email" -> feedback.email,
      "subject" -> feedback.subject,
      "content" -> feedback.content,
      "pcmid" -> feedback.pcmid,
      "date" -> feedback.date
    )
  }
}

object FeedbackDAOImpl {

  val feedbacks = Database.db("feedbacks")
}
