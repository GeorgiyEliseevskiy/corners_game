package com.company.Game;

import com.company.Model.Board;
import com.company.Model.Cell;
import com.company.Model.Player;
import java.util.ArrayList;
import java.util.List;
/**
 * Класс, ответственный за проверку возможности перемещения фигур в игре уголки.
 */
public class UgolkiPathChecker {

    private Board board;

    /**
     * Создает экземпляр класса UgolkiPathChecker.
     *
     * @param board Доска, на которой проводится игра.
     */
    public UgolkiPathChecker(Board board) {
        this.board = board;
    }

    /**
     * Проверяет возможность выполнения хода игроком из клетки `from` в клетку `to`.
     *
     * @param player Игрок, выполняющий ход.
     * @param from   Исходная клетка, откуда осуществляется ход.
     * @param to     Целевая клетка, куда осуществляется ход.
     * @throws IllegalArgumentException В случае невозможности выполнения хода выбрасывается исключение.
     */
    public void checkMovePossibility(Player player, Cell from, Cell to) throws IllegalArgumentException {
        quickCheck(player, from, to);

        List<Cell> neighbourCells = board.getNeighbourCells(from);
        List<Cell> possibleMoves = new ArrayList<Cell>();

        for (Cell cell : neighbourCells) {
            if (!cell.isEmpty()) {
                tryJumpOver(possibleMoves, new ArrayList<Cell>(), from, cell);
            } else {
                possibleMoves.add(cell);
            }
        }

        if (!possibleMoves.contains(to)) {
            throw new IllegalArgumentException("Нельзя переместить вашу фигуру туда!");
        }
    }

    /**
     * Быстрая проверка возможности перемещения фигуры.
     *
     * @param player Игрок, выполняющий ход.
     * @param from   Исходная клетка, откуда осуществляется ход.
     * @param to     Целевая клетка, куда осуществляется ход.
     * @throws IllegalArgumentException В случае невозможности выполнения хода выбрасывается исключение.
     */
    private void quickCheck(Player player, Cell from, Cell to) throws IllegalArgumentException {
        if (from.isEmpty()) {
            throw new IllegalArgumentException("Исходная клетка пуста!");
        }

        if (!to.isEmpty()) {
            throw new IllegalArgumentException("Целевая клетка уже занята!");
        }

        if (player.getColor() != from.getFigure().getColor()) {
            throw new IllegalArgumentException("Вы не можете перемещать фигуры противника!");
        }
    }

    /**
     * Попытка перепрыгнуть через фигуры и добавление клеток, через которые можно прыгнуть, в список `possibleMoves`.
     *
     * @param possibleMoves Список клеток, куда можно переместить фигуру.
     * @param jumpedOver    Список клеток, через которые уже было выполнено перепрыгивание.
     * @param from          Исходная клетка, откуда начинается попытка перепрыгнуть через фигуры.
     * @param takenCell     Клетка, через которую выполняется перепрыгивание.
     */
    private void tryJumpOver(List<Cell> possibleMoves, List<Cell> jumpedOver, Cell from, Cell takenCell) {
        Cell cellToJump = getCellToJump(from, takenCell);

        if (cellToJump != null && cellToJump.isEmpty()) {
            List<Cell> neighbourCells = board.getNeighbourCells(cellToJump);
            possibleMoves.add(cellToJump);
            jumpedOver.add(takenCell);

            for (Cell cell : neighbourCells) {
                if (!cell.isEmpty() && !jumpedOver.contains(cell)) {
                    tryJumpOver(possibleMoves, jumpedOver, cellToJump, cell);
                }
            }
        }
    }

    /**
     * Определение клетки, через которую можно выполнить перепрыгивание фигуры.
     *
     * @param from   Исходная клетка, откуда начинается перепрыгивание.
     * @param over   Клетка, через которую нужно перепрыгнуть.
     * @return Клетка, через которую можно перепрыгнуть, или `null`, если невозможно.
     */
    private Cell getCellToJump(Cell from, Cell over) {
        int newX = over.getX() + (over.getX() - from.getX());
        int newY = over.getY() + (over.getY() - from.getY());
        return board.getCellAt(newX, newY);
    }
}
