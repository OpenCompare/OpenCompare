package models.services

import javax.inject.Inject

import models.Feedback
import models.daos.FeedbackDAO
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

/**
 * Handles actions to feedbacks.
 *
 * @param feedbackDAO The user DAO implementation.
 */
class FeedbackServiceImpl @Inject() (feedbackDAO: FeedbackDAO) extends FeedbackService {

  /**
   * Saves a feedback.
   *
   * @param feedback The feedback to save.
   * @return The saved feedback.
   */
  def save(feedback: Feedback) = feedbackDAO.save(feedback)

}
