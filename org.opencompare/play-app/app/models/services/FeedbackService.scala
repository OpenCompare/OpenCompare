package models.services

import models.Feedback

import com.google.inject.ImplementedBy
import scala.concurrent.Future

/**
 * Handles actions to feedbacks.
 */
@ImplementedBy(classOf[FeedbackServiceImpl])
trait FeedbackService {

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(feedback: Feedback): Future[Feedback]
}
