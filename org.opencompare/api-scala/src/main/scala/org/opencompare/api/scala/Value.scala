package org.opencompare.api.scala

import java.util.Date

trait Value

// Simple values
case class BooleanValue(value : Boolean) extends Value
case class IntegerValue(value : Int) extends Value
case class RealValue(value : Double) extends Value
case class StringValue(value : String) extends Value

case class NotApplicableValue() extends Value
case class NotAvailableValue() extends Value

case class DateValue(value : Date) extends Value
case class DimensionValue() extends Value
case class UnitValue() extends Value
case class VersionValue() extends Value

// Composite values
case class ConditionalValue(value : Value, condition : Value) extends Value
case class MultipleValue(values : List[Value]) extends Value
case class PartialValue(value : Value) extends Value





