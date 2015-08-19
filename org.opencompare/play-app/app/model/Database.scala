package model

import java.util.Base64

import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId
import org.opencompare.api.java.PCMContainer
import org.opencompare.api.java.impl.io.{KMFJSONExporter, KMFJSONLoader}

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

  def get(id : String) : DatabasePCM = {
    if (ObjectId.isValid(id)) {
      val searchById = MongoDBObject("_id" -> new ObjectId(id))
      val result = pcms.findOne(searchById)

      // TODO : DatabasePCM var = createDatabasePCMInstance(result);
      new DatabasePCM(None, None) // FIXME : remove when done
    } else {
      new DatabasePCM(None, None)
    }
  }


  def update(databasePCM: DatabasePCM) {
    // TODO
  }

  def create(pcmContainer: PCMContainer) : String = {
    // TODO
    ""
  }

  private def createDatabasePCMInstance(dbObject : DBObject) : DatabasePCM = {
    // TODO
    new DatabasePCM(None, None) // FIXME : remove when done
  }

  def exists(id : String) : Boolean = {
    // TODO
    false
  }

  def remove(id : String) {
    pcms.remove(MongoDBObject("_id" -> new ObjectId(id)))
  }

  def serializePCMContainer(pcmContainer: PCMContainer) : DBObject = {
    // TODO
    MongoDBObject()
  }

  def serializeDatabasePCM(dbPCM : DatabasePCM) : String = {
    // TODO
    ""
  }

  def serializePCMContainersToJSON(pcmContainers : List[PCMContainer]) : String = {
    // TODO
    ""
  }

}
