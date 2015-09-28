package models.daos

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.impl.providers.OpenIDInfo
import com.mongodb.casbah.Imports
import models.Database

/**
 * The DAO to store the OpenID information.
 *
 * Note: Not thread safe, demo only.
 */
class OpenIDInfoDAO extends DelegableAuthInfoDAO[OpenIDInfo] with AuthInfoDAO[OpenIDInfo] {

  override def data: Imports.MongoCollection = Database.db("openIDInfo")

  override def loadFromDB(dbAuthInfo: Imports.DBObject): OpenIDInfo = ???

  override def convertToDB(loginInfo: LoginInfo, authInfo: OpenIDInfo): Imports.DBObject = ???
}
