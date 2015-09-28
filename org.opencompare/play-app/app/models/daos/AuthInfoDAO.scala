package models.daos

import com.mohiva.play.silhouette.api.LoginInfo
import com.mongodb.casbah.Imports._
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

/**
 * Created by gbecan on 9/28/15.
 */
trait AuthInfoDAO[T] {

  def data : MongoCollection

  def convertToDB(loginInfo: LoginInfo, authInfo: T) : DBObject

  def loadFromDB(dbAuthInfo : DBObject) : T

  /**
   * Finds the auth info which is linked with the specified login info.
   *
   * @param loginInfo The linked login info.
   * @return The retrieved auth info or None if no auth info could be retrieved for the given login info.
   */
  def find(loginInfo: LoginInfo): Future[Option[T]] = Future {

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
  def add(loginInfo: LoginInfo, authInfo: T): Future[T] = Future {
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
  def update(loginInfo: LoginInfo, authInfo: T): Future[T] = Future {
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
  def save(loginInfo: LoginInfo, authInfo: T): Future[T] = {
    Future {
      val request = MongoDBObject(
        "providerID" -> loginInfo.providerID,
        "providerKey" -> loginInfo.providerKey
      )
      data.findOne(request)
    }.flatMap{ dbAuthInfo =>
      if (dbAuthInfo.isDefined) {
        update(loginInfo, authInfo)
      } else {
        add(loginInfo, authInfo)
      }
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




}
