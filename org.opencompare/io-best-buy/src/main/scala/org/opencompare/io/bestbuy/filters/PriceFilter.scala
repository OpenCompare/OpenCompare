package org.opencompare.io.bestbuy.filters

import org.opencompare.io.bestbuy.ProductInfo

/**
 * Created by gbecan on 4/7/15.
 */
trait PriceFilter extends ProductFilter {

  abstract override def select(products : List[ProductInfo]) : List[ProductInfo] = {
    super.select(products).filter { product =>
      val price = product.completeXMLDescription.\("salePrice").text.toDouble
      price >= minPrice && price <= maxPrice
    }
  }

  def minPrice : Double
  def maxPrice : Double


}
