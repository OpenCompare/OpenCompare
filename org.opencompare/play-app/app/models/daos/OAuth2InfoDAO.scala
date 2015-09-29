package models.daos

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.impl.providers.OAuth2Info
import com.mongodb.casbah.Imports
import com.mongodb.casbah.Imports._
import models.Database

/**
 * The DAO to store the OAuth2 information.
 *
 */
class OAuth2InfoDAO extends DelegableAuthInfoDAO[OAuth2Info] with AuthInfoDAO[OAuth2Info] {

  override def data: Imports.MongoCollection = Database.db("oAuth2Info")

  override def loadFromDB(dbAuthInfo: DBObject): OAuth2Info = {
    val params = dbAuthInfo.getAs[DBObject]("params").map(_.map(e => e._1 -> e._2.toString).toMap)
    OAuth2Info(
      Database.loadString(dbAuthInfo, "accessToken"),
      Database.loadOptionalString(dbAuthInfo, "tokenType"),
      Database.loadOptionalString(dbAuthInfo, "expiresIn").map(_.toInt),
      Database.loadOptionalString(dbAuthInfo, "refreshToken"),
      params
    )
  }

  override def convertToDB(loginInfo: LoginInfo, authInfo: OAuth2Info): DBObject = {

    MongoDBObject(
      "providerID" -> loginInfo.providerID,
      "providerKey" -> loginInfo.providerKey,
      "accessToken" -> authInfo.accessToken,
      "tokenType" -> authInfo.tokenType,
      "expiresIn" -> authInfo.expiresIn,
      "refreshToken" -> authInfo.refreshToken,
      "params" -> authInfo.params
    )
  }
}
