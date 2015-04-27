package org.diverse.pcm.io.bestbuy.filters

import org.diverse.pcm.io.bestbuy.ProductInfo

/**
 * Created by gbecan on 4/7/15.
 */
trait CategoryFilter extends ProductFilter {

  abstract override def select(products : List[ProductInfo]) : List[ProductInfo] = {
    super.select(products).filter { product =>
      val productCategories = product.completeXMLDescription.\("categoryPath").\("category").\("name").map(_.text).toSet
      categories.forall(productCategories.contains(_))
    }
  }

  def categories : Set[String]


}
