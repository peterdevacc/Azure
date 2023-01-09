package com.peter.common

class Sudoku {

    fun createSudoku(gameLevel: Int): List<List<Int>> {
        val boardList = mutableListOf<MutableList<MutableList<Int>>>()
        for (n in 1..8) {
            val board = createNumBoard()
            if (verify(board)) {
                boardList.add(board)
            }
        }
        val board = boardList.random()

        val blankNumList = when (gameLevel) {
            0 -> listOf(4, 5)
            1 -> listOf(5, 6)
            else -> listOf(6, 7)
        }
        for (i in 0..8) {
            val front = mutableListOf(0, 1, 2)
            val middle = mutableListOf(3, 4, 5)
            val end = mutableListOf(6, 7, 8)
            val blankNum = blankNumList.random()
            for (j in 0 until blankNum) {
                val index: Int
                when (j) {
                    0, 3, 6 -> {
                        index = front.random()
                        front.remove(index)
                    }
                    1, 4, 7 -> {
                        index = middle.random()
                        middle.remove(index)
                    }
                    2, 5, 8 -> {
                        index = end.random()
                        end.remove(index)
                    }
                    else -> {
                        index = 0
                    }
                }
                board[i][index] = 0
            }
        }

        return board
    }

    fun verifySudokuAnswer(
        answer: List<List<Int>>
    ): Boolean {
        return verify(answer)
    }

    private fun verify(answer: List<List<Int>>): Boolean {
        var result = true
        val numList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

        var x = 0
        var y = 0
        var start = -1
        var end = -1
        for (i in 0..8) {
            val row = answer[i].toMutableList().sorted()
            if (row != numList) {
                result = false
            }

            val column = mutableListOf<Int>()
            for (j in 0..8) {
                column.add(answer[j][i])
            }
            column.sort()
            if (column != numList) {
                result = false
            }

            when (i) {
                in 0..2 -> {
                    start = 0
                    end = 2
                }
                in 3..5 -> {
                    start = 3
                    end = 5
                }
                in 6..8 -> {
                    start = 6
                    end = 8
                }
            }

            val block = mutableListOf<Int>()
            for (j in start..end) {
                block.add(answer[j][x])
                block.add(answer[j][x + 1])
                block.add(answer[j][x + 2])
                y += 3
            }
            block.sort()
            if (block != numList) {
                result = false
            }
            x += 3
            y = 0
            if (i == end) {
                x = 0
            }
        }

        return result
    }

    private fun createNumBoard(): MutableList<MutableList<Int>> {
        val board = mutableListOf<MutableList<Int>>()
        for (i in 0..8) {
            board.add(
                mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
            )
        }

        var numList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9).shuffled()
        var j = 0
        for (i in 0..8) {
            when (i) {
                0, 1, 2 -> {
                    board[i][0] = numList[j]
                    board[i][1] = numList[j + 1]
                    board[i][2] = numList[j + 2]
                    j += 3
                    if (i == 2) {
                        j = 0
                        numList = numList.shuffled()
                    }
                }
                3, 4, 5 -> {
                    board[i][3] = numList[j]
                    board[i][4] = numList[j + 1]
                    board[i][5] = numList[j + 2]
                    j += 3
                    if (i == 5) {
                        j = 0
                        numList = numList.shuffled()
                    }
                }
                else -> {
                    board[i][6] = numList[j]
                    board[i][7] = numList[j + 1]
                    board[i][8] = numList[j + 2]
                    j += 3
                }
            }
        }

        fillRemainCell(board)

        return board
    }

    private fun fillRemainCell(
        board: MutableList<MutableList<Int>>
    ): MutableList<MutableList<Int>> {
        val cellList = mutableListOf(
            Pair(0 to 3, 1) to Pair(0, 3)
        )

        while (cellList.isNotEmpty()) {
            val cell = cellList.first()
            cellList.removeFirst()

            val position = convertPosition(
                cell.first.first.first,
                cell.first.first.second
            )
            if (position.first == -1 && position.second == -1) {
                break
            }
            val y = position.first
            val x = position.second

            if (cell.first.second == 1) {
                for (num in 1..9) {
                    if (isCellValid(y, x, num, board)) {
                        for (i in 9 downTo num + 1) {
                            cellList.add(0, Pair(y to x, i) to Pair(y, x + 1))
                        }

                        board[y][x] = num
                        cellList.add(0, Pair(y to x + 1, 1) to Pair(y, x))
                        break
                    }
                }
            } else {
                val saved = convertPosition(
                    cell.second.first,
                    cell.second.second
                )
                board[saved.first][saved.second] = 0
                board[y][x] = 0
                if (isCellValid(y, x, cell.first.second, board)) {
                    board[y][x] = cell.first.second
                    cellList.add(0, Pair(y to x + 1, 1) to Pair(y, x))
                }
            }

        }

        return board
    }

    private fun convertPosition(r: Int, c: Int): Pair<Int, Int> {
        var y = r
        var x = c
        if (x >= 9 && y < 8) {
            y += 1
            x = 0
        }
        if (y >= 9 && x >= 9) {
            return -1 to -1
        }
        if (y < 3) {
            if (x < 3) {
                x = 3
            }
        } else if (y < 6) {
            if (x == (y / 3) * 3) {
                x += 3
            }
        } else {
            if (x == 6) {
                y += 1
                x = 0
                if (y >= 9) {
                    return -1 to -1
                }
            }
        }

        return y to x
    }

    private fun isCellValid(y: Int, x: Int, num: Int, board: List<List<Int>>): Boolean {
        val firstCellY = y - y % 3
        val firstCellX = x - x % 3
        val blockList = mutableListOf<Int>()
        for (i in 0..2) {
            blockList.add(board[i + firstCellY][firstCellX])
            blockList.add(board[i + firstCellY][firstCellX + 1])
            blockList.add(board[i + firstCellY][firstCellX + 2])
        }

        for (i in 0..8) {
            if (board[y][i] == num) {
                return false
            }
            if (board[i][x] == num) {
                return false
            }
            if (blockList[i] == num) {
                return false
            }
        }

        return true
    }

}