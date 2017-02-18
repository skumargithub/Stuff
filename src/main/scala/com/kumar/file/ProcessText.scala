package com.kumar.file

import scala.io._

// Print out all lines in a file
object ProcessText {
  def main(args: Array[String]): Unit = {
    try {
      println("Hello ProcessText!")

      val lines: List[String] = Source.fromFile("D:/Scala-Workspace/Stuff.git/ProcessText.txt").getLines.toList
      lines.foreach(println)

      println("All Done!")
    } catch {
      case t: Throwable => {
        t.printStackTrace
        println(s"Exception: $t")
      }
    }
  }
}
