package models.daos

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.impl.providers.OAuth1Info
import com.mongodb.casbah.Imports
import com.mongodb.casbah.Imports._
import models.Database

/**
 * The DAO to store the OAuth1 information.
 *
 */
class OAuth1InfoDAO extends DelegableAuthInfoDAO[OAuth1Info] with AuthInfoDAO[OAuth1Info] {

  override def data: Imports.MongoCollection = Database.db("oAuth1Info")

  override def convertToDB(loginInfo: LoginInfo, authInfo: OAuth1Info) : DBObject = {
    MongoDBObject(
      "providerID" -> loginInfo.providerID,
      "providerKey" -> loginInfo.providerKey,
      "token" -> authInfo.token,
      "secret" -> authInfo.secret
    )
  }

  override def loadFromDB(dbAuthInfo : DBObject) : OAuth1Info = {
    OAuth1Info(
      Database.loadString(dbAuthInfo, "token"),
      Database.loadString(dbAuthInfo, "secret")
    )
  }
}
