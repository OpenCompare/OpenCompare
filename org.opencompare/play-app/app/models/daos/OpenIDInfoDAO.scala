package models.daos

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.impl.providers.OpenIDInfo
import com.mongodb.DBObject
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import models.Database

/**
 * The DAO to store the OpenID information.
 *
 */
class OpenIDInfoDAO extends DelegableAuthInfoDAO[OpenIDInfo] with AuthInfoDAO[OpenIDInfo] {

  override def data: MongoCollection = Database.db("openIDInfo")

  override def loadFromDB(dbAuthInfo: DBObject): OpenIDInfo = {
    OpenIDInfo(
      Database.loadString(dbAuthInfo, "id"),
      dbAuthInfo.getAs[DBObject]("attributes").get.map(e => e._1 -> e._2.toString).toMap
    )
  }

  override def convertToDB(loginInfo: LoginInfo, authInfo: OpenIDInfo): DBObject = {
    MongoDBObject(
      "providerID" -> loginInfo.providerID,
      "providerKey" -> loginInfo.providerKey,
      "id" -> authInfo.id,
      "attributes" -> authInfo.attributes
    )
  }
}
