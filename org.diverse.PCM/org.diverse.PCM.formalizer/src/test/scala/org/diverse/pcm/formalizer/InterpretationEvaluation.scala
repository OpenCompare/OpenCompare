package org.diverse.pcm.formalizer

import pcmmm.Constraint
import pcmmm.Cell

case class InterpretationEvaluation(cell : Cell, constraint : Constraint, evaluation : Evaluation) {
  
}

abstract class Evaluation
case class NotEvaluated extends Evaluation
case class Incoherent(evaluations : List[Evaluation]) extends Evaluation
case class Valid extends Evaluation
case class DontKnow extends Evaluation
case class NoInterpretation extends Evaluation
case class CorrectedInMM(concept : String) extends Evaluation
case class NewConcept(concept : String, firstName : String, lastName : String, email : String) extends Evaluation

