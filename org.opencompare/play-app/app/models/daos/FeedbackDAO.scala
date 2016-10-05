package models.daos

import models.Feedback

import com.google.inject.ImplementedBy
import scala.concurrent.Future

/**
 * Give access to the feedback object.
 */
@ImplementedBy(classOf[FeedbackDAOImpl])
trait FeedbackDAO {

  /**
   * Saves a feedback.
   *
   * @param feedback The feedback to save.
   * @return The saved feedback.
   */
  def save(feedback: Feedback): Future[Feedback]
}
