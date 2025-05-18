package com.phyohtet.game.ui

import com.phyohtet.game.ScoreStorage
import com.phyohtet.game.model.Cell
import com.phyohtet.game.model.Direction
import kotlin.random.Random

class Game2048(
    private val size: Int,
    private val storage: ScoreStorage? = null
) {

    private lateinit var _chart: Array<Array<Cell>>

    private var _cellsMap = mutableMapOf<String, Cell>()

    private var _score = 0

    private var _bestScore = storage?.getScore() ?: 0

    val cells: List<Cell>
        get() = _cellsMap.values.toList()

    val score: Int
        get() = _score

    val bestScore: Int
        get() = _bestScore

    fun initialize() {
        _chart = Array(size) { x ->
            Array(size) { y ->
                Cell(x, y)
            }
        }
        _cellsMap = mutableMapOf()
        _score = 0

        addNew()
        addNew()
    }

    fun move(direction: Direction): Boolean {
        var moved = false
        for (i in 0..<size) {
            moved = when (direction) {
                Direction.UP -> {
                    moveValueInColumn(getColumn(i), false) || moved
                }

                Direction.DOWN -> {
                    moveValueInColumn(getColumn(i), true) || moved
                }

                Direction.LEFT -> {
                    moveValueInRow(getRow(i), false) || moved
                }

                Direction.RIGHT -> {
                    moveValueInRow(getRow(i), true) || moved
                }
            }
        }

        if (moved) {
            addNew()
            updateBestScore()
        }

        return moved
    }
    private fun updateBestScore() {
        if (_score <= _bestScore) {
            return
        }

        _bestScore = _score
        storage?.save(_score)
    }

    private fun addNew() {
        val emptyCells = mutableListOf<Cell>()

        for (x in 0..<size) {
            for (y in 0..<size) {
                val cell = _chart[x][y]
                if (cell.value > 0) {
                    continue
                }
                emptyCells.add(cell)
            }
        }

        if (emptyCells.size == 0) {
            return
        }

        val chosenCell = emptyCells[Random.nextInt(emptyCells.size)]
        chosenCell.value = if (Random.nextDouble() < 0.9) 2 else 4

        val newCell = Cell(chosenCell.x, chosenCell.y).apply {
            value = chosenCell.value
        }
        _cellsMap[newCell.id] = newCell

//        println(newCell.id)
//        println("new => x: ${newCell.x}, y: ${newCell.y}")
    }

    private fun moveValueInRow(cells: Array<Cell>, reversed: Boolean): Boolean {
        if (reversed) {
            cells.reverse()
        }

        if (!shouldMove(cells)) {
            return false;
        }

        val moveableCells = cells.filter { cell -> cell.value > 0 }
        val len = moveableCells.size

        val mergedMap = mutableMapOf<String, Cell>()

        var v = 0
        for (i in 0..<len) {
            val cell = moveableCells[i]
            val my = if (!reversed) v else this.size - 1 - v
            if (!reversed && my < cell.y) {
                this._chart[cell.x][cell.y].value = 0
                cell.y = my
                this._chart[cell.x][cell.y].value = cell.value
            }

            if (reversed && my > cell.y) {
                this._chart[cell.x][cell.y].value = 0
                cell.y = my
                this._chart[cell.x][cell.y].value = cell.value
            }

            if (i == 0) {
                v += 1
                continue
            }

            val prevCell = moveableCells[i - 1]

            if (cell.value == prevCell.value && !mergedMap.contains(prevCell.id)) {
                cell.value += prevCell.value

                this._chart[cell.x][cell.y].value = 0
                mergedMap[cell.id] = prevCell

                cell.y = prevCell.y

                this._score += cell.value

                this._chart[cell.x][cell.y].value = cell.value

                this._cellsMap.remove(prevCell.id)

                v = if (reversed) v - 1 else cell.y
            }

            v += 1
        }

        return true
    }

    private fun moveValueInColumn(cells: Array<Cell>, reversed: Boolean): Boolean {
        if (reversed) {
            cells.reverse()
        }
        if (!shouldMove(cells)) {
            return false;
        }

        val moveableCells = cells.filter { cell -> cell.value > 0 }
        val len = moveableCells.size

        val mergedMap = mutableMapOf<String, Cell>()

        var v = 0
        for (i in 0..<len) {
            val cell = moveableCells[i]
            val mx = if (!reversed) v else this.size - 1 - v
            if (!reversed && mx < cell.x) {
                this._chart[cell.x][cell.y].value = 0
                cell.x = mx
                this._chart[cell.x][cell.y].value = cell.value
            }

            if (reversed && mx > cell.x) {
                this._chart[cell.x][cell.y].value = 0
                cell.x = mx
                this._chart[cell.x][cell.y].value = cell.value
            }

            if (i == 0) {
                v += 1
                continue
            }

            val prevCell = moveableCells[i - 1]

            if (cell.value == prevCell.value && !mergedMap.contains(prevCell.id)) {
                cell.value += prevCell.value

                this._chart[cell.x][cell.y].value = 0
                mergedMap[cell.id] = prevCell

                cell.x = prevCell.x

                this._score += cell.value

                this._chart[cell.x][cell.y].value = cell.value

                this._cellsMap.remove(prevCell.id)

                v = if (reversed) v - 1 else cell.x
            }

            v += 1
        }

        return true
    }

    private fun shouldMove(rowOrColumn: Array<Cell>): Boolean {
        val values = rowOrColumn.map { it.value }
        val len = values.size;
        return values.withIndex().any { (index, value) ->
            val cond1 = (value != 0 && index < len - 1 && value == values[index + 1])
            val cond2 = (value == 0 && values.slice(index + 1 until len).any { it > 0 })
            return@any cond1 || cond2
        }
    }

    private fun getRow(x: Int): Array<Cell> {
        return Array(size) { y ->
            cells.firstOrNull { c -> c.x == x && c.y == y } ?: Cell(x, y)
        }
    }

    private fun getColumn(y: Int): Array<Cell> {
        return Array(size) { x ->
            cells.firstOrNull { c -> c.x == x && c.y == y } ?: Cell(x, y)
        }
    }

}