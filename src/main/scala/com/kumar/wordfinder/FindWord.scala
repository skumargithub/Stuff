package com.kumar.wordfinder

object FindWord {
  class Letter(val x: Int, val y: Int, val letter: Char, var used: Boolean = false) {
    override def toString = s"($x, $y) -> $letter"
  }
  
  object Board {
    def apply(board: Board): Board = {
      val b = new Board(board.width, board.height)
      
      for(i <- 0 to board.width - 1) {
        for(j <- 0 to board.height - 1) {
          val letter = board.letters(i)(j)
          val letterClone = new Letter(i, j, letter.letter, letter.used)
          b.add(letterClone)
        }
      }
      
      b
    }
  }
  
  class Board(val width: Int, val height: Int) {
    val letters = Array.ofDim[Letter](width, height)
    
    def add(letter: Letter) = {
      letters(letter.x)(letter.y) = letter
    }
    
    def getUnusedNeighbors(x: Int, y: Int): List[Letter] = {
      var result: List[Letter] = List()
      
      if(x > 0 && y > 0) result = letters(x - 1)(y - 1) :: result                       // top left
      if(y > 0) result = letters(x)(y - 1) :: result                                    // top
      if(x < width - 1 && y > 0) result = letters(x + 1)(y - 1) :: result               // top right
      if(x < width - 1) result = letters(x + 1)(y) :: result                            // right
      if(x < width - 1 && y < height - 1) result = letters(x + 1)(y + 1) :: result      // bottom right
      if(y < height - 1) result = letters(x)(y + 1) :: result                           // bottom
      if(x > 0 && y < height - 1) result = letters(x - 1)(y + 1) :: result              // bottom left
      if(x > 0) result = letters(x - 1)(y) :: result                                    // left
      
      result.filter(!_.used)
    }
    
    def getUnusedNeighbors(letter: Letter): List[Letter] = {
      getUnusedNeighbors(letter.x, letter.y)
    }
    
    def apply(x: Int)(y: Int): Letter = letters(x)(y)
  }
  
  def makeString(in: List[Letter]): String = {
    in.reverse.map(_.letter).mkString
  }

  def inDict(letters: List[Letter]): Boolean = {
    Dictionary.contains(makeString(letters))
  }

  def buildLetter(depth: Int, board: Board, currentList: List[Letter]): List[List[Letter]] = {
    depth == currentList.size match {
      case true => if(inDict(currentList)) List(currentList) else List()
      case false => {
        val startLetter = currentList.head
        
        board.getUnusedNeighbors(startLetter).flatMap(n => {
          val newCurrentList = n :: currentList
          val newBoard = Board(board)
          newBoard.letters(n.x)(n.y).used = true

          buildLetter(depth, newBoard, newCurrentList)
        })
      }
    }
  }
  
  def main(args: Array[String]) = {
    println("Hi There!")
    
    val b: Board = new Board(4, 4)
    b.add(new Letter(0, 0, 'L'))
    b.add(new Letter(1, 0, 'O', true))
    b.add(new Letter(2, 0, 'O', true))
    b.add(new Letter(3, 0, 'R', true))
  
    b.add(new Letter(0, 1, 'I'))
    b.add(new Letter(1, 1, 'H'))
    b.add(new Letter(2, 1, 'P', true))
    b.add(new Letter(3, 1, 'O', true))

    b.add(new Letter(0, 2, 'C'))
    b.add(new Letter(1, 2, 'P'))
    b.add(new Letter(2, 2, 'A'))
    b.add(new Letter(3, 2, 'E'))

    b.add(new Letter(0, 3, 'N'))
    b.add(new Letter(1, 3, 'E'))
    b.add(new Letter(2, 3, 'T'))
    b.add(new Letter(3, 3, 'R'))
    
    val startLetter = b.letters(1)(2)
    startLetter.used = true
    val results = buildLetter(6, b, List(startLetter))
   
    println(results.map(makeString(_)).toSet.mkString("\n"))
  }
}