package org.opencompare.experimental.io.wikipedia

import org.sweble.wikitext.engine.config.ParserConfigImpl
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp
import org.sweble.wikitext.parser.WikitextPreprocessor

/**
 * Created by gbecan on 5/20/15.
 */
class Sweble2Parser {

  def parse(code : String, title : String): Unit = {

    val config = new ParserConfigImpl(DefaultConfigEnWp.generate())
    val preprocessor = new WikitextPreprocessor(config)
    val preprocessVisitor = new PreprocessVisitor

    val preproAST = preprocessor.parseArticle(code, title)
    preprocessVisitor.go(preproAST)
  }

}
