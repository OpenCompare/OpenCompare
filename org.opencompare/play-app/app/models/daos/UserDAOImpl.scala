package models.daos

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import com.mongodb.casbah.Imports._
import models.daos.UserDAOImpl._
import models.{DefaultRole, AdminRole, Database, User}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/**
 * Give access to the user object.
 */
class UserDAOImpl extends UserDAO {

  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  override def find(loginInfo: LoginInfo) = Future {
//    users.find { case (id, user) => user.loginInfo == loginInfo }.map(_._2)
    val request = MongoDBObject(
      "providerID" -> loginInfo.providerID,
      "providerKey" -> loginInfo.providerKey
    )
    val dbUser = users.findOne(request)
    if (dbUser.isDefined) {
      Some(loadFromDB(dbUser.get))
    } else {
      None
    }
  }


  /**
   * Finds a user by its user ID.
   *
   * @param userID The ID of the user to find.
   * @return The found user or None if no user for the given ID could be found.
   */
  override def find(userID: UUID) = Future {
    val request = MongoDBObject("userID" -> userID.toString)
    val dbUser = users.findOne(request)
    if (dbUser.isDefined) {
      Some(loadFromDB(dbUser.get))
    } else {
      None
    }
  }

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  override def save(user: User) = Future {
      users.insert(convertToDB(user))
      user
  }


  private def convertToDB(user : User) : DBObject = {
    MongoDBObject(
      "userID" -> user.userID.toString,
      "providerID" -> user.loginInfo.providerID,
      "providerKey" -> user.loginInfo.providerKey,
      "role" -> user.role.name,
      "firstName" -> user.firstName,
      "lastName" -> user.lastName,
      "fullName" -> user.fullName,
      "email" -> user.email,
      "avatarURL" -> user.avatarURL
    )
  }

  private def loadFromDB(dBObject: DBObject) : User = {
    val userID = UUID.fromString(dBObject.get("userID").asInstanceOf[String])
    val role = Database.loadString(dBObject, "role") match {
      case "admin" => AdminRole()
      case "default" => DefaultRole()
    }
    val loginInfo = LoginInfo(dBObject.get("providerID").asInstanceOf[String], dBObject("providerKey").asInstanceOf[String])
    val firstName = Option(dBObject.get("firstName").asInstanceOf[String])
    val lastName = Option(dBObject.get("lastName").asInstanceOf[String])
    val fullName = Option(dBObject.get("fullName").asInstanceOf[String])
    val email = Option(dBObject.get("email").asInstanceOf[String])
    val avatarURL = Database.loadOptionalString(dBObject, "avatarURL")

    User(userID, loginInfo, role, firstName, lastName, fullName, email, avatarURL)
  }
}

object UserDAOImpl {

  val users = Database.db("users")
}
