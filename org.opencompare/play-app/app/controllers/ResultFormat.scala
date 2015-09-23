package controllers

/**
 * Created by gbecan on 9/23/15.
 */
abstract class ResultFormat

case class JsonFormat() extends ResultFormat
case class PageFormat() extends ResultFormat
case class EmbedFormat() extends ResultFormat
