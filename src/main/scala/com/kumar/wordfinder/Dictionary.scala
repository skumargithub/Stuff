package com.kumar.wordfinder

import java.io.FileInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream

object Dictionary {
  val bs = scala.io.Source.fromInputStream(new GzipCompressorInputStream(new FileInputStream("words.txt.gz")))
  val set = scala.collection.mutable.HashSet[String]()
  for(line <- bs.getLines) {
    set.add(line.toLowerCase)
  }
  bs.close
  
  println(s"Finished reading words, total: ${set.size}")
  
  def contains(s: String): Boolean = set.contains(s.toLowerCase)
}