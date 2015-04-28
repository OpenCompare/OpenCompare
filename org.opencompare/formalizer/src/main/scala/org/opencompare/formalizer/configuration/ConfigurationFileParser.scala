//package org.opencompare.formalizer.configuration
//
//import scala.io.Source
//import java.util.regex.Pattern
//import scala.collection.mutable.ListBuffer
//import scala.collection.mutable.ListBuffer
//import scala.collection.mutable.ListBuffer
//import scala.collection.mutable.ListBuffer
//import org.opencompare.formalizer.interpreters.BooleanPatternInterpreter
//import org.opencompare.formalizer.interpreters.VariabilityConceptRefPatternInterpreter
//import org.opencompare.formalizer.interpreters.PartialPatternInterpreter
//import org.opencompare.formalizer.interpreters.MultiplePatternInterpreter
//import org.opencompare.formalizer.interpreters.UnknownPatternInterpreter
//import org.opencompare.formalizer.interpreters.EmptyPatternInterpreter
//import org.opencompare.formalizer.interpreters.InconsistentPatternInterpreter
//
//class ConfigurationFileParser {
//
//  private var pcmConfig : PCMConfiguration = _
//  private var matrixConfig : MatrixConfiguration = _
//
//  def parse(path : String) : PCMConfiguration = {
//    pcmConfig = new PCMConfiguration
//    matrixConfig = pcmConfig.defaultConfiguration
//
//    val configFile = Source.fromFile(path)
//    for (line <- configFile.getLines) {
//      parseLine(line)
//    }
//
//    pcmConfig
//  }
//
//
//  def parseLine(line : String) : Boolean = {
//		var ok : Boolean = false
//		ok = ok || parseContext(line)
//		ok = ok || parseSimpleParameter(line)
//		ok = ok || parseComplexParameter(line)
//    	ok = ok || parseRule(line)
//    	ok
//  }
//
//  def parseContext(s : String) : Boolean = {
//    	val matrixID = "\".*?\"(?:\\[\\d+\\])?"
//		val pattern = Pattern
//				.compile("for\\s+(" + matrixID + "(?:\\s*,\\s*" + matrixID + ")*)\\s*:\\s*");
//		val matcher = pattern.matcher(s);
//		if (matcher.matches()) {
//		  matrixConfig = new MatrixConfiguration
//		  for (value <- matcher.group(1).split("(?<![^\"\\]])\\s*,\\s*")) {
//		    val firstQuote = value.indexOf("\"")
//		    val lastQuote = value.lastIndexOf("\"")
//		    val firstBracket = value.lastIndexOf("[")
//		    val lastBracket = value.lastIndexOf("]")
//
//		    val matrix = value.substring(firstQuote+1, lastQuote)
//		    val index = if (firstBracket != -1) {
//		    	Integer.parseInt(value.substring(firstBracket+1, lastBracket))
//		    } else {
//		    	-1
//		    }
//
//		    pcmConfig.matrixConfigurations += ((matrix, index) -> matrixConfig)
//		  }
//
//		  true
//		} else {
//		  false
//		}
//  }
//
//  /**
//   * Parse a pattern configuration
//   */
//  def parseRule(s : String) : Boolean = {
//		val rulePattern = Pattern
//				.compile("(\".*\")*(\\s|\\t)*(\\w+)(\\s|\\t)*(\".*\")(\\s|\\t)*(\\{.*\\})*");
//		val ruleMatcher = rulePattern.matcher(s);
//		if (ruleMatcher.matches()) {
//			// managing headers
//			val headers = ruleMatcher.group(1);
//			val h : ListBuffer[String] = ListBuffer()
//			if (Option(headers).isDefined) {
//				val temp = headers.split("\\s(?=\")|(?<=\")\\s");
//				for (t <- temp) {
//					h += t.replace("\"", "")
//				}
//			}
//			// getting the rule name
//			val ruleName = ruleMatcher.group(3);
//
//			// getting the rule expression
//			var ruleExp = ruleMatcher.group(5);
//			var size = ruleExp.length();
//			ruleExp = ruleExp.substring(1, size - 1);
//
//			// gettings the rule parameters
//			var params = ruleMatcher.group(7);
//			var p : ListBuffer[String] = ListBuffer()
//			if (Option(params).isDefined) {
//				size = params.length();
//				params = params.substring(1, size - 1);
//				val temp = params.split(" ");
//				for (t <- temp) {
//					p += t;
//				}
//			}
//
//			val validHeaders = h.toList
//			val parameters = p.toList
//
//			val patternInterpreter = ruleName match {
//		      case "Boolean" => Some(new BooleanPatternInterpreter(validHeaders, ruleExp, parameters, true))
//		      case "Simple" => Some(new VariabilityConceptRefPatternInterpreter(validHeaders, ruleExp, parameters, true))
//		      case "Partial" => Some(new PartialPatternInterpreter(validHeaders, ruleExp, parameters, true))
//		      case "Multiple" => Some(new MultiplePatternInterpreter(validHeaders, ruleExp, parameters, true))
//		      case "Unknown" => Some(new UnknownPatternInterpreter(validHeaders, ruleExp, parameters, true))
//		      case "Empty" => Some(new EmptyPatternInterpreter(validHeaders, ruleExp, parameters, true))
//		      case "Inconsistent" => Some(new InconsistentPatternInterpreter(validHeaders, ruleExp, parameters, true))
//		      case _ => None
//		    }
//
//			if (patternInterpreter.isDefined) {
//				matrixConfig.addPattern(patternInterpreter.get)
//			}
//
//			true
//		} else {
//			false
//		}
//	}
//
//
//  	def parseSimpleParameter(s : String) : Boolean = {
//		val pattern = Pattern.compile("\\s*(.*?)\\s*=\\s*(\\w+)\\s*");
//		val matcher = pattern.matcher(s);
//		if (matcher.matches()) {
//			val key = matcher.group(1);
//			val value = matcher.group(2);
//			key match {
//			  case "header-rows" => matrixConfig.headerRows = convertToInt(value)
//			  case "header-columns" => matrixConfig.headerColumns = convertToInt(value)
//			  case "ignored" => matrixConfig.ignored = convertToBoolean(value)
//			  case "inverted" => matrixConfig.inverted = convertToBoolean(value)
//			  case "filter-domain-values" => pcmConfig.filterDomainValues = convertToBoolean(value)
//			  case _ =>
//			}
//			true;
//		} else {
//			false;
//		}
//	}
//
//	def parseComplexParameter(s : String) : Boolean = {
//		val configPattern = Pattern.compile("\\s*(.*?)\\s*=\\s*\\{(\".*?\"(,\".*?\")*)?\\}\\s*")
//		val configMatcher = configPattern.matcher(s)
//		if (configMatcher.matches()) {
//			val key = configMatcher.group(1)
//			val parameters : ListBuffer[String] = ListBuffer()
//			for (i <- 2 until configMatcher.groupCount) {
//				val group = configMatcher.group(i)
//				if (Option(group).isDefined) {
//					val values = group.split(",")
//					for (value <- values) {
//						parameters += (value.substring(value.indexOf("\"")+1,value.lastIndexOf("\"")))
//					}
//				}
//			}
//
//			key match {
//			  case "ignore-rows" => matrixConfig.ignoreRows = convertToListOfInt(parameters.toList)
//			  case "ignore-columns" => matrixConfig.ignoreColumns = convertToListOfInt(parameters.toList)
//			  case _ =>
//			}
//			true
//		} else   {
//			false
//		}
//	}
//
//	private def convertToListOfInt(strings : List[String]) : List[Int] = {
//	  	val integers : ListBuffer[Int] = ListBuffer()
//	  	for (string <- strings) {
//	  		try {
//				val integer = Integer.parseInt(string)
//				integers += integer
//			} catch {
//			  case e : NumberFormatException =>
//			}
//	  	}
//	  	integers.toList
//	}
//
//	private def convertToInt(string : String) : Int = {
//		try {
//			string.toInt
//		} catch {
//			case e : NumberFormatException => -1
//		}
//	}
//
//	private def convertToBoolean(string : String) : Boolean = {
//		try {
//			string.toBoolean
//		} catch {
//			case e : IllegalArgumentException => false
//		}
//	}
//}