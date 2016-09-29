package org.opencompare.api.scala.io
import java.io.File

import org.opencompare.api.scala.metadata.{Orientation, Positions}
import org.opencompare.api.scala._
import play.api.libs.json.{JsArray, JsObject, JsValue, Json}

import scala.io.Source

class JSONLoader extends PCMLoader {

  /**
    * Return a list of PCMs from a string representation
    *
    * @param pcms : string representation of a PCM
    * @return the PCM represented by pcm
    */
  override def load(pcms: String): List[PCM] = {
    load(Json.parse(pcms))
  }

  /**
    * Return a list of PCMs from a file
    *
    * @param file file to load
    * @return loaded PCM
    */
  override def load(file: File): List[PCM] = {
    load(Json.parse(Source.fromFile(file).mkString))
  }

  def load(json: JsValue) : List[PCM] = {
    val pcm = new PCM with Positions with Orientation

    pcm.name = (json \ "name").as[String]

    val (features, idToFeature) = loadFeatures((json \ "features").as[Seq[JsValue]])
    pcm.features = features

    pcm.productsKey = idToFeature.get((json \ "productsKey").as[Int])

    val (products, idToProduct) = loadProducts((json \ "products").as[Seq[JsValue]], idToFeature)
    pcm.products = products

    pcm.featurePositions = (json \ "featurePositions").as[JsObject].fields.map(e => idToFeature(e._1.toInt) -> e._2.as[Int]).toMap
    pcm.productPositions = (json \ "productPositions").as[JsObject].fields.map(e => idToProduct(e._1.toInt) -> e._2.as[Int]).toMap

    List(pcm)
  }

  def loadFeatures(json : Seq[JsValue]) : (Set[AbstractFeature], Map[Int, Feature]) = {
    var idToFeature = Map.empty[Int, Feature]

    val features = for (jsValue <- json) yield {
      val name = (jsValue \ "name").as[String]
      val jsonSubFeatures = (jsValue \ "subFeatures").asOpt[Seq[JsValue]]

      if (jsonSubFeatures.isDefined) { // Feature group
        val featureGroup = new FeatureGroup

        featureGroup.name = name

        val (subFeatures, subIdToFeature) = loadFeatures(jsonSubFeatures.get)
        featureGroup.subFeatures = subFeatures

        idToFeature = idToFeature ++ subIdToFeature

        featureGroup
      } else { // Feature
        val feature = new Feature
        feature.name = name

        val id = (jsValue \ "id").as[Int]
        idToFeature += id -> feature

        feature
      }
    }

    (features.toSet, idToFeature)
  }

  def loadProducts(json : Seq[JsValue], idToFeature : Map[Int, Feature]) : (Set[Product], Map[Int, Product]) = {

    var idToProduct = Map.empty[Int, Product]

    val products = for (jsValue <- json) yield  {
      val product = new Product

      val jsonCells = (jsValue \ "cells").as[Seq[JsValue]]
      product.cells = loadCells(jsonCells, idToFeature)

      val id = (jsValue \ "id").as[Int]
      idToProduct += id -> product

      product
    }
    (products.toSet, idToProduct)
  }

  def loadCells(json : Seq[JsValue], idToFeature : Map[Int, Feature]) : Set[Cell] = {

    (for (jsValue <- json) yield {
      val cell = new Cell

      cell.content = (jsValue \ "content").as[String]
      cell.rawContent = (jsValue \ "rawContent").as[String]
      cell.interpretation = None // TODO
      cell.feature = idToFeature((jsValue \ "feature").as[Int])


      cell
    }).toSet
  }
}
