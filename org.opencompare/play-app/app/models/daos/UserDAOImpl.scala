package models.daos

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import com.mongodb.casbah.Imports._
import models.daos.UserDAOImpl._
import models.{Database, User}
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
  def find(loginInfo: LoginInfo) = Future {
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
  def find(userID: UUID) = Future {
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
  def save(user: User) = Future {
      users.insert(convertToDB(user))
      user
  }


  private def convertToDB(user : User) : DBObject = {
    MongoDBObject(
      "userID" -> user.userID.toString,
      "providerID" -> user.loginInfo.providerID,
      "providerKey" -> user.loginInfo.providerKey,
      "firstName" -> user.firstName,
      "lastName" -> user.lastName,
      "fullName" -> user.fullName,
      "email" -> user.email,
      "avatarURL" -> user.avatarURL
    )
  }

  private def loadFromDB(dbObject: DBObject) : User = {
    val userID = UUID.fromString(dbObject.get("userID").asInstanceOf[String])
    val loginInfo = LoginInfo(dbObject.get("providerID").asInstanceOf[String], dbObject("providerKey").asInstanceOf[String])
    val firstName = Option(dbObject.get("firstName").asInstanceOf[String])
    val lastName = Option(dbObject.get("lastName").asInstanceOf[String])
    val fullName = Option(dbObject.get("fullName").asInstanceOf[String])
    val email = Option(dbObject.get("email").asInstanceOf[String])
    val avatarURL = Database.loadOptionalString(dbObject, "avatarURL")

    User(userID, loginInfo, firstName, lastName, fullName, email, avatarURL)
  }
}

object UserDAOImpl {

  val users = Database.db("users")
}
