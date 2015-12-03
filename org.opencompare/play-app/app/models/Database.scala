package models

import java.util.Base64

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient
import com.mongodb.util.JSON
import org.bson.types.ObjectId
import org.opencompare.api.java.PCMContainer
import org.opencompare.api.java.impl.io.{KMFJSONExporter, KMFJSONLoader}
import play.api.mvc.{Request, AnyContent}

import scala.collection.JavaConversions._

/**
 * Created by gbecan on 8/19/15.
 */
object Database {

  val kmfLoader: KMFJSONLoader = new KMFJSONLoader
  val kmfSerializer: KMFJSONExporter = new KMFJSONExporter

  val base64Decoder = Base64.getDecoder

  // Connect to DB
  val mongoClient = MongoClient("localhost", 27017)
  val db = mongoClient("opencompare")
  val pcms = db("pcms")

  // Initialize index
  val indexInitialized = pcms.indexInfo.exists(indexInfo => indexInfo("name") == "pcm.name_text")
  if (!indexInitialized) {
    pcms.createIndex(MongoDBObject("pcm.name" -> "text"))
  }


  /**
   * Search a PCM by name
   * @param request : string searched in PCM names
   * @return
   */
  def search(request : String) : List[PCMInfo] = {
    val cursor = pcms.find(MongoDBObject(), MongoDBObject("pcm.name" -> "1"))

    val results = for (result <- cursor) yield {
      val id = result("_id").toString
      val pcm = result("pcm").asInstanceOf[DBObject]
      val encodedName = pcm("name").toString
      val name = new String(base64Decoder.decode(encodedName.getBytes()))
      if (name.toLowerCase().contains(request.toLowerCase())) {
        Some(new PCMInfo(id, name))
      } else {
        None
      }
    }

    results.toList.flatten
  }

  /**
   * Count the number of PCMs
   * @return number of PCMs in the database
   */
  def count() : Long = {
    pcms.count()
  }

  /**
   * List PCMs in database
   * @param limit
   * @param page
   * @return
   */
  def list(limit : Int, page : Int) : List[PCMInfo] = {
    val skipped = (page - 1) * limit
    val cursor = pcms.find(MongoDBObject(), MongoDBObject("pcm.name" -> "1"))
      .sort(MongoDBObject("_id" -> 1))
      .skip(skipped)
      .limit(limit)

    val results = for (result <- cursor) yield {
      val dbPCM = Option(result("pcm").asInstanceOf[DBObject])
      val dbID = Option(result("_id"))

      if (dbID.isDefined && dbPCM.isDefined) {
        val id = dbID.get.toString
        val name = dbPCM.get("name").toString
        val decodedName = new String(base64Decoder.decode(name.getBytes())) // Decode Base64 characters
        val info = new PCMInfo(id, decodedName)
        Some(info)
      } else {
        None
      }
    }

    results.toList.flatten
  }

  def update(databasePCM: DatabasePCM) {
    val dbPCMContainer = serializePCMContainer(databasePCM.pcmContainer.get)
    pcms.update(MongoDBObject("_id" -> new ObjectId(databasePCM.id.get)), dbPCMContainer)
  }

  def create(pcmContainer: PCMContainer) : String = {
    val newPCM = serializePCMContainer(pcmContainer)
    val result = pcms.insert(newPCM)
    val id = newPCM("_id").toString
    id
  }

  private def createDatabasePCMInstance(dbObject : DBObject) : DatabasePCM = {

    if (Option(dbObject).isDefined) {

      val id = dbObject("_id").toString
      val json = JSON.serialize(dbObject("pcm"))

      val pcmContainers = kmfLoader.load(json)

      if (pcmContainers.size == 1) {
        val pcmContainer = pcmContainers.head
        val metadata = pcmContainer.getMetadata

        // Load metadata
        val dbMetadata = dbObject("metadata").asInstanceOf[DBObject]
        metadata.setSource(dbMetadata.getOrElse("source", "").toString)
        metadata.setLicense(dbMetadata.getOrElse("license", "").toString)
        metadata.setCreator(dbMetadata.getOrElse("creator", "").toString)

        // Load product positions
        val dbProductPositions = dbMetadata("productPositions").asInstanceOf[BasicDBList]
        for (dbProductPosition <- dbProductPositions) {
          val dbProductPositionCast = dbProductPosition.asInstanceOf[DBObject]
          val productName = dbProductPositionCast("product").toString
          val product = pcmContainer.getPcm.getProducts.find(_.getKeyContent == productName).get
          val position = dbProductPositionCast("position").toString.toInt
          metadata.setProductPosition(product, position)
        }

        // Load feature positions
        val dbFeaturePositions = dbMetadata("featurePositions").asInstanceOf[BasicDBList]
        for (dbFeaturePosition <- dbFeaturePositions) {
          val dbFeaturePositionCast = dbFeaturePosition.asInstanceOf[DBObject]
          val featureName = dbFeaturePositionCast("feature").toString
          val feature = pcmContainer.getPcm.getConcreteFeatures.find(_.getName == featureName).get
          val position = dbFeaturePositionCast("position").toString.toInt
          metadata.setFeaturePosition(feature, position)
        }

        new DatabasePCM(Some(id), Some(pcmContainer))

      } else {
        new DatabasePCM(None, None)
      }
    } else {
      new DatabasePCM(None, None)
    }


  }

  def exists(id : String) : Boolean = {
    if (ObjectId.isValid(id)) {
      val searchById = MongoDBObject("_id" -> new ObjectId(id))
      val result = pcms.findOne(searchById)
      result.isDefined
    } else {
      false
    }
  }

  def remove(id : String) {
    pcms.remove(MongoDBObject("_id" -> new ObjectId(id)))
  }

  def serializePCMContainer(pcmContainer: PCMContainer) : DBObject = {
    // Serialize PCM
    val jsonContainer = kmfSerializer.export(pcmContainer)
    val dbContainer = JSON.parse(jsonContainer).asInstanceOf[DBObject]

    dbContainer
  }

  def serializeDatabasePCM(dbPCM : DatabasePCM) : String = {
    val dbContainer = serializePCMContainer(dbPCM.pcmContainer.get)
    JSON.serialize(dbContainer)
  }

  def serializePCMContainersToJSON(pcmContainers : List[PCMContainer]) : String = {
    val dbContainers = pcmContainers.map(serializePCMContainer(_))
    JSON.serialize(dbContainers)
  }

  def serializePCMContainerToJSON(pcmContainer : PCMContainer) : String = {
    val dbContainer = serializePCMContainer(pcmContainer)
    JSON.serialize(dbContainer)
  }

  def loadString(dbObject : DBObject, key : String) : String = {
    dbObject.getAs[String](key).get
  }

  def loadOptionalString(dbObject : DBObject, key : String) : Option[String] = {
    dbObject.getAs[String](key)
  }


  def createError(source : String, request: Request[AnyContent]) {
    db("errors").insert(MongoDBObject(
      "source" -> source,
      "host" -> request.uri,
      "body" -> request.body.toString
    ))
  }

  def incrementUsage(name : String) {
    val statsDB = db("usageStats")

    val query = MongoDBObject("name" -> name)
    val existingStats = statsDB.findOne()
    existingStats match {
      case Some(stats) =>
        val value = stats.getAs[Int]("value").get
        val update = $set("value" -> (value + 1))
        statsDB.update(query, update)
      case None =>
        statsDB.insert(MongoDBObject(
          "name" -> name,
          "value" -> 1
        ))
    }
  }

  def addHTMLSource(source : String) {
    val htmlSourceDB = db("htmlSources")
    val query = MongoDBObject("source" -> source)
    if (!htmlSourceDB.findOne(query).isDefined) {
      htmlSourceDB.insert(query)
    }
  }

  def getLastHTMLSources(limit : Int): List[String] = {
    val htmlSourceDB = db("htmlSources")
    val cursor = htmlSourceDB
      .find(MongoDBObject())
      .sort(MongoDBObject("_id" -> -1))
      .limit(limit)

    val lastSources = cursor.flatMap { result =>
      result.getAs[String]("source")
    }

    lastSources.toList
  }
}
