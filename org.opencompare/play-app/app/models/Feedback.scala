package models

import java.util.UUID

/**
 * The feedback object.
 *
 * @param email
 * @param subject
 * @param content
 */
case class Feedback(
  email: String,
  subject : String,
  content: String)
