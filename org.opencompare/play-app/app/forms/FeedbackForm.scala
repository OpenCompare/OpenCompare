package forms

import play.api.data.Form
import play.api.data.Forms._

/**
 * The form which handles the submission of a feedback.
 */
object FeedbackForm {

  /**
   * A play framework form.
   */
  val form = Form(
    mapping(
      "email" -> optional(email),
      "subject" -> optional(text),
      "content" -> nonEmptyText,
      "pcmid" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )

  /**
   * The form data.
   *
   * @param email The email of the user.
   * @param password The password of the user.
   * @param rememberMe Indicates if the user should stay logged in on the next visit.
    * @param pcmid PCM identifier (a feedback applies to a PCM)
   */
  case class Data(
    email: Option[String],
    subject: Option[String],
    content: String,
    pcmid: String)
}
