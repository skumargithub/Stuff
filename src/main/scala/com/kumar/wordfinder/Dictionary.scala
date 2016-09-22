package com.kumar.wordfinder

object Dictionary {
  val bs = scala.io.Source.fromFile("D:/Scala-Workspace/Stuff/words.txt")
  val set = scala.collection.mutable.HashSet[String]()
  for(line <- bs.getLines) {
    set.add(line.toLowerCase)
  }
  
  println(s"Finished reading words, total: ${set.size}")
  
  def contains(s: String): Boolean = set.contains(s.toLowerCase)
}