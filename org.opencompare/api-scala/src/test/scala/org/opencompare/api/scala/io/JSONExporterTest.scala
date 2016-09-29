package org.opencompare.api.scala.io

import java.nio.file.{Files, Path, Paths}

import org.opencompare.api.scala.metadata.{Orientation, Positions}
import org.opencompare.api.scala.{OpenCompareTest, PCM, Product, StringValue}
import org.scalatest.{FlatSpec, Matchers}

class JSONExporterTest extends OpenCompareTest {

  it should "export a simple PCM" in {
    val pcm = new PCM with Positions with Orientation

    pcm.name = "Json export"

    val f1 = createFeature(pcm, "f1")
    pcm.featurePositions += f1 -> 1
    val fg = createFeatureGroup(pcm, "fg")
    val f2 = createFeature(fg, "f2")
    pcm.featurePositions += f2 -> 2
    val f3 = createFeature(fg, "f3")
    pcm.featurePositions += f3 -> 3

    pcm.productsKey = Some(f1)

    for (index <- 0 until 3) {
      val product = new Product
      for (feature <- pcm.concreteFeatures) {
        val content = "c" + index + feature.name
        createCell(product, feature, content, Some(StringValue(content)))
      }
      pcm.products += product
      pcm.productPositions += product -> index
    }

    val jsonExporter = new JSONExporter
    val json = jsonExporter.export(pcm)

    json should not be empty

    Files.write(Paths.get("/tmp", "json-test.json"), json.getBytes)

  }

}
