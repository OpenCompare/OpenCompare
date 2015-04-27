package org.diverse.pcm.io.bestbuy.filters

import org.diverse.pcm.io.bestbuy.ProductInfo

/**
 * Created by gbecan on 4/7/15.
 */
trait MarketPlaceFilter extends ProductFilter {

  abstract override def select(products : List[ProductInfo]) : List[ProductInfo] = {
    super.select(products).filter { product =>
      product.completeXMLDescription.\("marketplace").text == "false"
    }
  }

}
