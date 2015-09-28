package models.daos

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.impl.providers.OAuth2Info
import com.mongodb.casbah.Imports
import models.Database

/**
 * The DAO to store the OAuth2 information.
 *
 * Note: Not thread safe, demo only.
 */
class OAuth2InfoDAO extends DelegableAuthInfoDAO[OAuth2Info] with AuthInfoDAO[OAuth2Info] {

  override def data: Imports.MongoCollection = Database.db("oAuth2Info")

  override def loadFromDB(dbAuthInfo: Imports.DBObject): OAuth2Info = ???

  override def convertToDB(loginInfo: LoginInfo, authInfo: OAuth2Info): Imports.DBObject = ???
}
