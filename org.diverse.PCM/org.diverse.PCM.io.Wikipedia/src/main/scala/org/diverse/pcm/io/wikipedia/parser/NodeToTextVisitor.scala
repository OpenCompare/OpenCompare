package parser

import de.fau.cs.osr.ptk.common.AstVisitor
import de.fau.cs.osr.ptk.common.ast.NodeList
import de.fau.cs.osr.ptk.common.ast.Text
import org.sweble.wikitext.`lazy`.parser.InternalLink
import org.sweble.wikitext.`lazy`.parser.LinkTitle

class NodeToTextVisitor extends AstVisitor {
  
	private val builder = new StringBuilder
  
	def getText() : String = {
		builder.toString
	}
	
	
	def visit(e : NodeList) {
		iterate(e)
	}
	
	def visit(e : Text) {
		builder ++= e.getContent()
	}
	
	def visit(e : InternalLink) {
		if (e.getTitle().getContent().isEmpty()) {
			builder ++= e.getTarget()
		} else if (!e.getTarget().endsWith(".png")){
			dispatch(e.getTitle())
		}
	}

	def visit(e : LinkTitle) {
		iterate(e)
	}
	
}