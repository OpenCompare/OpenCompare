package models.daos

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import com.mongodb.casbah.Imports
import com.mongodb.casbah.Imports._
import models.Database

/**
 * The DAO to store the password information.
 */
class PasswordInfoDAO extends DelegableAuthInfoDAO[PasswordInfo] with AuthInfoDAO[PasswordInfo] {



  override def convertToDB(loginInfo: LoginInfo, authInfo: PasswordInfo) : DBObject = {
    MongoDBObject(
      "providerID" -> loginInfo.providerID,
      "providerKey" -> loginInfo.providerKey,
      "hasher" -> authInfo.hasher,
      "password" -> authInfo.password,
      "salt" -> authInfo.salt
    )
  }

  override def loadFromDB(dbAuthInfo : DBObject) : PasswordInfo = {
    PasswordInfo(
      Database.loadString(dbAuthInfo, "hasher"),
      Database.loadString(dbAuthInfo, "password"),
      Database.loadOptionalString(dbAuthInfo, "salt")
    )
  }
  override def data: Imports.MongoCollection = Database.db("authInfo")

}
