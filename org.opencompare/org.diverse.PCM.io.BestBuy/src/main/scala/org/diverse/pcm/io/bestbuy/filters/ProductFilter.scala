package org.diverse.pcm.io.bestbuy.filters

import org.diverse.pcm.io.bestbuy.ProductInfo

/**
 * Created by gbecan on 4/7/15.
 */
class ProductFilter {

  /**
   * Select a subset of a list of products
   * @param products
   * @return a subset of 'products'
   */
  def select(products : List[ProductInfo]) : List[ProductInfo] = {
    products
  }


}
