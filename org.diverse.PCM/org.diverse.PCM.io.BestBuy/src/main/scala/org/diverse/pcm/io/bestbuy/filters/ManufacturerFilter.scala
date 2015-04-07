package org.diverse.pcm.io.bestbuy.filters

import org.diverse.pcm.io.bestbuy.ProductInfo

/**
 * Created by gbecan on 4/7/15.
 */
trait ManufacturerFilter extends ProductFilter {

  abstract override def select(products : List[ProductInfo]) : List[ProductInfo] = {
    super.select(products).filter { product =>
      manufacturers.contains((product.completeXMLDescription \\ "manufacturer").text)
    }
  }

  def manufacturers : Set[String]

}
