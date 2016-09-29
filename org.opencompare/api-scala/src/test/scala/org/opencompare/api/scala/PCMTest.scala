package org.opencompare.api.scala

import org.opencompare.api.scala.metadata.Position
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}


class PCMTest extends OpenCompareTest {

  it should "create a cell" in {
    val cell = new Cell
    cell.content = "content"
    cell.rawContent = "raw content"
    cell.interpretation = Some(BooleanValue(true))
    cell.feature = new Feature
    cell.feature.name = "feature name"

    cell.content should be ("content")
    cell.rawContent should be ("raw content")
    cell.interpretation should be (Some(BooleanValue(true)))
    cell.feature.name should be ("feature name")


  }

  it should "create a product" in {
    val product = new Product
    product.cells.size should be (0)

    val cell = new Cell
    product.cells += cell
    product.cells.size should be (1)

    product.cells -= cell
    product.cells.size should be (0)
  }

  it should "create a feature" in {
    val feature = new Feature
    feature.name = "name"
    feature.name should be ("name")
  }

  it should "create a feature group" in {
    val featureGroup = new FeatureGroup

    featureGroup.subFeatures.size should be (0)

    featureGroup.name = "feature group name"
    featureGroup.name should be ("feature group name")

    val feature = new Feature
    featureGroup.subFeatures += feature
    featureGroup.subFeatures += new FeatureGroup
    featureGroup.subFeatures.size should be (2)

    featureGroup.subFeatures -= feature
    featureGroup.subFeatures.size should be (1)

    featureGroup.subFeatures.head shouldBe a [FeatureGroup]

  }

  it should "create a PCM" in {
    val pcm = new PCM

    pcm.name = "pcm"
    pcm.name should be ("pcm")

    // Create features
    val features = (for (i <- 0 until 10) yield {
      val feature = new Feature
      feature.name = "Feature " + i
      i -> feature
    }).toMap

    pcm.features = features.values.toSet

      // Create products
    pcm.products = (for (i <- 0 until 10) yield {
      val product = new Product with Position
      product.position = i

      product.cells = (for (j <- 0 until 10) yield {
        val cell = new Cell with Position
        cell.position = j
        cell.rawContent = "c" + i + j
        cell.content = cell.rawContent
        cell.feature = features(j)
        cell
      }).toSet

      product
    }).toSet


    pcm.productsKey = features.get(0)

    for (product <- pcm.products) {
      val productPosition = product.asInstanceOf[Product with Position].position
      product.key.map(_.name) should be (Some("Feature 0"))

      for (cell <- product.cells) {
        val cellPosition = cell.asInstanceOf[Cell with Position].position
        cell.rawContent should be ("c" + productPosition + cellPosition)
        cell.content should be ("c" + productPosition + cellPosition)
        cell.feature.name should be ("Feature " + cellPosition)
        cell.product should be (product)
      }
    }


  }

  it should "test PCM" in {
    val pcm = new PCM

    pcm.features.size should be (0)
    pcm.products.size should be (0)

    pcm.name = "pcm name"
    pcm.name should be ("pcm name")

    pcm.features += new Feature
    pcm.features += new FeatureGroup
    pcm.features.size should be (2)

    pcm.features -= pcm.features.head
    pcm.features.size should be (1)

    pcm.products += new Product
    pcm.products.size should be (1)

    pcm.products -= pcm.products.head
    pcm.products.size should be (0)

  }


  it should "test concrete features" in {
    val pcm = new PCM
    createFeature(pcm, "Top level feature")
    val group = createFeatureGroup(pcm, "FG")

    val subFeature1 = new Feature
    subFeature1.name = "Sub feature 1"
    group.subFeatures += subFeature1

    val subFeature2 = new Feature
    subFeature2.name = "Sub feature 2"
    group.subFeatures += subFeature2

    pcm.features.size should be (2) // number of top level abstract features
    pcm.concreteFeatures.size should be (3) // number of concrete features

  }

  it should "get cells from feature" in {
    val pcm = new PCM
    val feature = createFeature(pcm, "feature")

    val p1 = createProduct(pcm)
    val p2 = createProduct(pcm)

    createCell(p1, feature, "C1", None)
    createCell(p2, feature, "C2", None)

    feature.cells.size should be (2)

  }

  it should "test equals and hashCode" in {
    val pcm1 = new PCM
    val pcm2 = new PCM

    val f1 = createFeature(pcm1, "feature")
    val f2 = createFeature(pcm2, "feature")

    // TODO : feature groups

    val p1 = createProduct(pcm1)
    val p2 = createProduct(pcm2)

    val c1 = createCell(p1, f1, "cell", Some(BooleanValue(true)))
    val c2 = createCell(p2, f2, "cell", Some(BooleanValue(true)))

    c1 should be (c2)
    f1 should be (f2)
    p1 should be (p2)
    pcm1 should be (pcm2)

    c1.hashCode() should be (c2.hashCode())
    f1.hashCode() should be (f2.hashCode())
    p1.hashCode() should be (p2.hashCode())
    pcm1.hashCode() should be (pcm2.hashCode())

  }


//
//  @Test
//  public void testMerge() throws MergeConflictException {
//    // Create PCM 1
//    PCM pcm1 = factory.createPCM();
//
//    Feature productsFeature1 = createFeature(pcm1, "Products");
//    pcm1.setProductsKey(productsFeature1);
//
//    Feature commonFeature1 = createFeature(pcm1, "Common feature");
//    Feature feature1 = createFeature(pcm1, "Feature from PCM 1");
//
//    FeatureGroup commonFeatureGroup1 = createFeatureGroup(pcm1, "Common feature group");
//    Feature commonFeatureFG1 = factory.createFeature();
//    commonFeatureFG1.setName("Common feature group - common feature");
//    commonFeatureGroup1.addFeature(commonFeatureFG1);
//
//    FeatureGroup featureGroup1 = createFeatureGroup(pcm1, "Feature group from PCM 1");
//    Feature featureFG1 = factory.createFeature();
//    featureFG1.setName("Feature group from PCM 1 - feature");
//    featureGroup1.addFeature(featureFG1);
//
//    Product commonProduct1 = createProduct(pcm1);
//    createCell(commonProduct1, productsFeature1, "Common product", null);
//    createCell(commonProduct1, commonFeature1, "", null);
//    createCell(commonProduct1, feature1, "", null);
//    createCell(commonProduct1, commonFeatureFG1, "", null);
//    createCell(commonProduct1, featureFG1, "", null);
//
//    Product product1 = createProduct(pcm1);
//    createCell(product1, productsFeature1, "Product from PCM 1", null);
//    createCell(product1, commonFeature1, "", null);
//    createCell(product1, feature1, "", null);
//    createCell(product1, commonFeatureFG1, "", null);
//    createCell(product1, featureFG1, "", null);
//
//
//    // Create PCM 2
//    PCM pcm2 = factory.createPCM();
//
//    Feature productsFeature2 = createFeature(pcm2, "Products");
//    pcm2.setProductsKey(productsFeature2);
//
//    Feature commonFeature2 = createFeature(pcm2, "Common feature");
//    Feature feature2 = createFeature(pcm2, "Feature from PCM 2");
//
//    FeatureGroup commonFeatureGroup2 = createFeatureGroup(pcm2, "Common feature group");
//    Feature commonFeatureFG2 = factory.createFeature();
//    commonFeatureFG2.setName("Common feature group - common feature");
//    commonFeatureGroup2.addFeature(commonFeatureFG2);
//
//    FeatureGroup featureGroup2 = createFeatureGroup(pcm2, "Feature group from PCM 2");
//    Feature featureFG2 = factory.createFeature();
//    featureFG2.setName("Feature group from PCM 2 - feature");
//    featureGroup2.addFeature(featureFG2);
//
//    Product commonProduct2 = createProduct(pcm2);
//    createCell(commonProduct2, productsFeature2, "Common product", null);
//    createCell(commonProduct2, commonFeature2, "", null);
//    createCell(commonProduct2, feature2, "", null);
//    createCell(commonProduct2, commonFeatureFG2, "", null);
//    createCell(commonProduct2, featureFG2, "", null);
//
//    Product product2 = createProduct(pcm2);
//    createCell(product2, productsFeature2, "Product from PCM 2", null);
//    createCell(product2, commonFeature2, "", null);
//    createCell(product2, feature2, "", null);
//    createCell(product2, commonFeatureFG2, "", null);
//    createCell(product2, featureFG2, "", null);
//
//    // Merge PCM 1 and 2
//    pcm1.merge(pcm2, factory);
//
//    // Check resulting PCM
//    assertEquals("number of features", 7, pcm1.getFeatures().size());
//    assertEquals("number of concrete features", 7, pcm1.getFeatures().size());
//    assertEquals("number of products", 3, pcm1.getProducts().size());
//    for (Product product : pcm1.getProducts()) {
//      assertEquals("number of cells", 7, product.getCells().size());
//    }
//
//  }
//
//  @Test
//  public void testIsValid() {
//    PCM pcm = factory.createPCM();
//
//    Feature productsF = createFeature(pcm, "Products");
//    pcm.setProductsKey(productsF);
//
//    Feature f1 = createFeature(pcm, "F1");
//    Feature f2 = createFeature(pcm, "F2");
//    assertTrue(pcm.isValid());
//
//    Feature f3 = createFeature(pcm, "F2");
//    assertFalse(pcm.isValid()); // Duplicated feature
//
//    pcm.removeFeature(f3);
//    assertTrue(pcm.isValid());
//
//    Product p1 = createProduct(pcm);
//    createCell(p1, productsF, "P1", null);
//    createCell(p1, f1, "C11", null);
//    assertFalse(pcm.isValid()); // Missing cell
//
//    createCell(p1, f2, "C12", null);
//    assertTrue(pcm.isValid());
//
//    Product p2 = createProduct(pcm);
//    createCell(p2, productsF, "P2", null);
//    createCell(p2, f1, "C21", null);
//    assertFalse(pcm.isValid()); // Missing cell
//
//    createCell(p2, f2, "C22", null);
//    assertTrue(pcm.isValid());
//
//    Product p3 = createProduct(pcm);
//    createCell(p3, productsF, "P2", null);
//    createCell(p3, f1, "C21", null);
//    createCell(p3, f2, "C22", null);
//    assertFalse(pcm.isValid()); // Duplicated product
//
//    pcm.removeProduct(p3);
//    assertTrue(pcm.isValid());
//  }
//

//
//  @Test
//  public void testDiff() {
//    // Create PCM 1
//    PCM pcm1 = factory.createPCM();
//
//    Feature productsFeature1 = createFeature(pcm1, "Products");
//    pcm1.setProductsKey(productsFeature1);
//
//    Feature commonFeature1 = createFeature(pcm1, "Common feature");
//    Feature feature1 = createFeature(pcm1, "Feature from PCM 1");
//
//    Product commonProduct1 = createProduct(pcm1);
//    createCell(commonProduct1, productsFeature1, "Common product", null);
//    createCell(commonProduct1, commonFeature1, "common cell 1", null);
//    createCell(commonProduct1, feature1, "", null);
//
//    Product product1 = createProduct(pcm1);
//    createCell(product1, productsFeature1, "Product from PCM 1", null);
//    createCell(product1, commonFeature1, "", null);
//    createCell(product1, feature1, "", null);
//
//
//
//    // Create PCM 2
//    PCM pcm2 = factory.createPCM();
//
//    Feature productsFeature2 = createFeature(pcm2, "Products");
//    pcm2.setProductsKey(productsFeature2);
//
//    Feature commonFeature2 = createFeature(pcm2, "Common feature");
//    Feature feature2 = createFeature(pcm2, "Feature from PCM 2");
//
//    Product commonProduct2 = createProduct(pcm2);
//    createCell(commonProduct2, productsFeature2, "Common product", null);
//    createCell(commonProduct2, commonFeature2, "common cell 2", null);
//    createCell(commonProduct2, feature2, "", null);
//
//    Product product2 = createProduct(pcm2);
//    createCell(product2, productsFeature2, "Product from PCM 2", null);
//    createCell(product2, commonFeature2, "", null);
//    createCell(product2, feature2, "", null);
//
//
//    // Diff
//    DiffResult diffResult = pcm1.diff(pcm2, new SimplePCMElementComparator());
//
//
//    assertEquals("common features", 2, diffResult.getCommonFeatures().size());
//    assertEquals("features only in PCM 1", 1, diffResult.getFeaturesOnlyInPCM1().size());
//    assertEquals("features only in PCM 2", 1, diffResult.getFeaturesOnlyInPCM2().size());
//
//    assertEquals("common products", 1, diffResult.getCommonProducts().size());
//    assertEquals("products only in PCM 1", 1, diffResult.getProductsOnlyInPCM1().size());
//    assertEquals("products only in PCM 2", 1, diffResult.getProductsOnlyInPCM2().size());
//
//    assertEquals("differing cells", 1, diffResult.getDifferingCells().size());
//  }
//
//  @Test
//  public void testInvert() {
//    // Create PCM
//    PCM pcm = factory.createPCM();
//
//    Feature productsFeature = createFeature(pcm, "Products");
//    Feature f1 = createFeature(pcm, "F1");
//    Feature f2 = createFeature(pcm, "F2");
//
//    pcm.setProductsKey(productsFeature);
//
//    Product p1 = createProduct(pcm);
//    Product p2 = createProduct(pcm);
//
//    createCell(p1, productsFeature, "P1", null);
//    createCell(p1, f1, "CP1F1", null);
//    createCell(p1, f2, "CP1F2", null);
//
//    createCell(p2, productsFeature, "P2", null);
//    createCell(p2, f1, "CP2F1", null);
//    createCell(p2, f2, "CP2F2", null);
//
//    // Check inverted PCM
//    pcm.invert(factory);
//
//    assertEquals("#features", 3, pcm.getConcreteFeatures().size());
//    assertEquals("#products", 2, pcm.getProducts().size());
//
//    assertEquals("product key", pcm.getProductsKey().getName(), "Products");
//
//    for (Product product : pcm.getProducts()) {
//      assertTrue("new product is an original feature", product.getKeyContent().startsWith("F"));
//      assertEquals("number of cells", 3, product.getCells().size());
//
//      for (Cell cell : product.getCells()) {
//        if (!cell.getFeature().equals(pcm.getProductsKey())) {
//          assertTrue("new feature is an original product", cell.getFeature().getName().startsWith("P"));
//          assertEquals("cell name", "C" + cell.getFeature().getName() + product.getKeyContent(), cell.getContent());
//        }
//      }
//    }
//  }
//



}
