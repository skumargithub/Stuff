package com.kumar.money

object InterestRates {
  def simpleInterest(initial: Double, years: Int, rate: Double): Double = {
    initial + years * rate;
  }

  def compoundInterest(initial: Double, years: Int, rate: Double): Double = {
    periodicCompoundInterest(initial, years, 1, rate)
  }

  def periodicCompoundInterest(initial: Double, years: Int, periodsInYear: Int = 1, rate: Double): Double = {
    Math.pow(initial + rate / periodsInYear, years * periodsInYear)
  }

  def main(args: Array[String]) = {
    println("Hi There!")

    val initial: Double = 1.0
    val years: Int = 3
    val rate: Double = 0.05

    println(s"Simple Interest: Capital after $years years = " + simpleInterest(initial, years, rate))
    println(s"Compound Interest: Capital after $years years = " + compoundInterest(initial, years, rate))

    val periodsInYear = 4
    println(s"Periodic Compound Interest: Capital after $years years = " + periodicCompoundInterest(initial, years, periodsInYear, rate))
  }
}
