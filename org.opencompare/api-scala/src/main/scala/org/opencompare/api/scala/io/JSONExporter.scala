package org.opencompare.api.scala.io
import org.opencompare.api.scala._
import org.opencompare.api.scala.metadata.{Orientation, Positions}
import play.api.libs.json.{JsObject, JsString, JsValue, Json}

class JSONExporter extends PCMExporter {
  /**
    * Export a PCM into a specific format
    *
    * @param pcm PCM to export
    * @return string representing the PCM
    */
  override def export(pcm: PCM with Positions with Orientation): String = {

    val json = Json.obj(
      "name" -> pcm.name,
      "features" -> exportFeatures(pcm.features),
      "products" -> exportProducts(pcm.products),
      "productsKey" -> pcm.productsKey.map(_.hashCode()),
      "featurePositions" -> pcm.featurePositions.map(pos => pos._1.hashCode().toString -> pos._2),
      "productPositions" -> pcm.productPositions.map(pos => pos._1.hashCode().toString -> pos._2),
      "orientation" -> pcm.orientation.toString
     )

    Json.prettyPrint(json)
  }

  private def exportFeatures(features : Set[AbstractFeature]) : JsValue = {
    Json.toJson(
      features.map {
        case feature : Feature => Json.obj(
          "name" -> feature.name,
          "id" -> feature.hashCode()
        )
        case featureGroup : FeatureGroup => Json.obj(
          "name" -> featureGroup.name,
          "subFeatures" -> exportFeatures(featureGroup.subFeatures)
        )
      }
    )
  }

   private def exportProducts(products : Set[Product]) : JsValue = {
     Json.toJson(
        products.map { product =>
          Json.obj(
            "id" -> product.hashCode(),
            "cells" -> exportCells(product.cells)
          )
        }
     )
   }

  private def exportCells(cells : Set[Cell]) : JsValue = {
    Json.toJson (
      cells.map { cell =>
        Json.obj(
          "content" -> cell.content,
          "rawContent" -> cell.rawContent,
          "interpretation" -> cell.interpretation.map(_.toString),
          "feature" -> cell.feature.hashCode()
        )
      }
    )
  }
}
