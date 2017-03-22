package models

import java.util.Date
import java.util.UUID

/**
 * The feedback object.
 *
 * @param email
 * @param subject
 * @param content
 */
case class Feedback(
  email: Option[String],
  subject : Option[String],
  content: String,
  pcmid: String,
  date : Date)
