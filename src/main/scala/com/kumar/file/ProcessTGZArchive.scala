package com.kumar.file

import java.io._
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import scala.collection.mutable

class TarArchiveEntryIterator(val tarArchive: TarArchiveInputStream) extends Iterator[TarArchiveEntry] {
  private var nextTarEntry: Option[TarArchiveEntry] = None

  override def hasNext(): Boolean = {
    nextTarEntry = Option(tarArchive.getNextTarEntry)
    nextTarEntry.isDefined
  }

  override def next(): TarArchiveEntry = {
    nextTarEntry.get
  }
}

class TarFileEntry(val name: String, val lines: List[String])

object ProcessTGZArchive {
  implicit def toTarFileEntryLines(tarArchiveInputStream: TarArchiveInputStream): List[String] = {
    val bf = new BufferedReader(new InputStreamReader(tarArchiveInputStream))
    val lines: scala.collection.mutable.MutableList[String] = mutable.MutableList()
    while(bf.ready) {
      val line = bf.readLine
      //println(s"line: $line")
      lines += line
    }

    lines.toList
  }

  def main(args: Array[String]): Unit = {
    try {
      println("Hello ProcessTGZArchive!")

      val inputFileName = "D:/Scala-Workspace/Stuff.git/ProcessFiles.tar.gz"
      val outputFileName = "D:/Scala-Workspace/Stuff.git/output.txt"

      import resource._

      val tarInputIterator: ManagedResource[TarArchiveEntryIterator] = for {
        fis <- managed(new FileInputStream(inputFileName))
        gzcis <- managed(new GzipCompressorInputStream(fis))
        tais <- managed(new TarArchiveInputStream(gzcis))
      } yield new TarArchiveEntryIterator(tais)

      /*tarInputIterator.foreach{tii: TarArchiveEntryIterator => tii.filter(_.isFile).foreach{e: TarArchiveEntry  =>
        val tfe = new TarFileEntry(e.getName, tii.tarArchive)
        //println(s"File: ${tfe.name}, dateSent: ${tfe.dateSent}, sender: ${tfe.sender}, subject: ${tfe.subject}")

        val json = ("fileName" -> tfe.name) ~ ("dateSent" -> tfe.dateSent) ~ ("sender" -> tfe.sender) ~ ("subject" -> tfe.subject)
        println(compact(render(json)))

        json
      }}*/

      tarInputIterator acquireAndGet {
        tii: TarArchiveEntryIterator => {
          val allLines: Iterator[List[String]] = tii.filter(_.isFile).map { e: TarArchiveEntry =>
            val tfe = new TarFileEntry(e.getName, tii.tarArchive)
            //println(s"File: ${tfe.name}, dateSent: ${tfe.dateSent}, sender: ${tfe.sender}, subject: ${tfe.subject}")

            tfe.lines
          }

          for {
            outputFile <- managed(new FileWriter(outputFileName))
            lines <- allLines
          } {
            println(s"lines: $lines")
            lines.foreach(line => outputFile.write(s"$line\n"))
          }
        }
      }

      println("All Done!")
    } catch {
      case t: Throwable => {
        t.printStackTrace
        println(s"Exception: ${t.getMessage}")
      }
    }
  }
}
