package models.daos

import models.Feedback

import scala.concurrent.Future

/**
 * Give access to the feedback object.
 */
trait FeedbackDAO {

  /**
   * Saves a feedback.
   *
   * @param feedback The feedback to save.
   * @return The saved feedback.
   */
  def save(feedback: Feedback): Future[Feedback]
}
