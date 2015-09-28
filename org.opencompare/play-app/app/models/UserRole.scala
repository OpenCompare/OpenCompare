package models

trait UserRole {
  def name : String
}

case class AdminRole() extends UserRole {
  override def name: String = "admin"
}
case class DefaultRole() extends UserRole {
  override def name: String = "default"
}