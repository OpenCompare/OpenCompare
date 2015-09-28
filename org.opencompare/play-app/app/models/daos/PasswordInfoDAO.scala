package models.daos

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import com.mongodb.casbah.Imports._
import models.Database
import models.daos.PasswordInfoDAO._
import play.api.libs.concurrent.Execution.Implicits._

import scala.collection.mutable
import scala.concurrent.Future

/**
 * The DAO to store the password information.
 */
class PasswordInfoDAO extends DelegableAuthInfoDAO[PasswordInfo] {

  /**
   * Finds the auth info which is linked with the specified login info.
   *
   * @param loginInfo The linked login info.
   * @return The retrieved auth info or None if no auth info could be retrieved for the given login info.
   */
  def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = Future {

    val request = MongoDBObject(
      "providerID" -> loginInfo.providerID,
      "providerKey" -> loginInfo.providerKey
    )
    val dbAuthInfo = data.findOne(request)
    if (dbAuthInfo.isDefined) {
      Some(loadFromDB(dbAuthInfo.get))
    } else {
      None
    }
  }

  /**
   * Adds new auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be added.
   * @param authInfo The auth info to add.
   * @return The added auth info.
   */
  def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = Future {
    data.insert(convertToDB(loginInfo, authInfo))
    authInfo
  }

  /**
   * Updates the auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be updated.
   * @param authInfo The auth info to update.
   * @return The updated auth info.
   */
  def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = Future {
    data.update(MongoDBObject(
      "providerID" -> loginInfo.providerID,
      "providerKey" -> loginInfo.providerKey
    ), convertToDB(loginInfo, authInfo))
    authInfo
  }

  /**
   * Saves the auth info for the given login info.
   *
   * This method either adds the auth info if it doesn't exists or it updates the auth info
   * if it already exists.
   *
   * @param loginInfo The login info for which the auth info should be saved.
   * @param authInfo The auth info to save.
   * @return The saved auth info.
   */
  def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    val request = MongoDBObject(
      "providerID" -> loginInfo.providerID,
      "providerKey" -> loginInfo.providerKey
    )
    val dbAuthInfo = data.findOne(request)
    if (dbAuthInfo.isDefined) {
      update(loginInfo, authInfo)
    } else {
      add(loginInfo, authInfo)
    }
  }

  /**
   * Removes the auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be removed.
   * @return A future to wait for the process to be completed.
   */
  def remove(loginInfo: LoginInfo): Future[Unit] = Future {
    val request = MongoDBObject(
      "providerID" -> loginInfo.providerID,
      "providerKey" -> loginInfo.providerKey
    )
    data.remove(request)
  }


  private def convertToDB(loginInfo: LoginInfo, authInfo: PasswordInfo) : DBObject = {
    MongoDBObject(
      "providerID" -> loginInfo.providerID,
      "providerKey" -> loginInfo.providerKey,
      "hasher" -> authInfo.hasher,
      "password" -> authInfo.password,
      "salt" -> authInfo.salt
    )
  }

  private def loadFromDB(dbAuthInfo : DBObject) : PasswordInfo = {
    PasswordInfo(
      Database.loadString(dbAuthInfo, "hasher"),
      Database.loadString(dbAuthInfo, "password"),
      Database.loadOptionalString(dbAuthInfo, "salt")
    )
  }


}

/**
 * The companion object.
 */
object PasswordInfoDAO {

  /**
   * The data store for the password info.
   */
  var data = Database.db("authInfo")
}
