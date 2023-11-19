package com.company.Game;

import com.company.Model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Игра "Уголки" - это игра для двух игроков, обычно играемая на доске размером 8x8 клеток.
 */
public class UgolkiGame {
    private Board board;
    private Player player1;
    private Player player2;
    private Player activePlayer;
    private WinCheckResponse lastGameStatus;
    private UgolkiPathChecker pathChecker;
    private UgolkiWinCriteria winCriteria;

    public UgolkiGame(Board board, Player player1, Player player2,
                      UgolkiPathChecker pathChecker, UgolkiWinCriteria winCriteria) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.pathChecker = pathChecker;
        this.winCriteria = winCriteria;
        initPlayers();
    }

    public void startGame() {
        board.cleanCells();
        initWhiteFigures();
        initBlackFigures();
        initPlayers();
        lastGameStatus = new WinCheckResponse(WinCheckResponse.Status.JUST_STARTED);
    }
    /**
     * Выполняет ход игрока, перемещая фигуру с одной клетки на другую.
     *
     * @param from Начальная клетка, откуда перемещается фигура.
     * @param to   Целевая клетка, куда перемещается фигура.
     * @throws IllegalArgumentException Если ход недопустим из-за ошибки в игре.
     */
    public void move(String from, String to) throws IllegalArgumentException {
        // Получаем объект начальной и целевой клеток из доски.
        Cell cellFrom = board.getCellAt(from);
        Cell cellTo = board.getCellAt(to);

        try {
            // Проверяем возможность выполнения хода с помощью проверки пути.
            pathChecker.checkMovePossibility(activePlayer, cellFrom, cellTo);

            // Перемещаем фигуру из начальной клетки в целевую и очищаем начальную клетку.
            cellTo.setFigure(cellFrom.getFigure());
            cellFrom.removeFigure();

            // Проверяем, не завершена ли игра в результате текущего хода.
            lastGameStatus = winCriteria.checkForWinner(board, activePlayer);

            // Вычисляем оценку доски после этого хода
            int boardScore = evaluation(board, activePlayer);
            System.out.println("Текущая оценка: " + boardScore);
            // Теперь у вас есть оценка доски после этого хода, и вы можете использовать ее, сохранить или анализировать ее.

            // Передаем ход другому игроку.
            changeActivePlayer();
        } catch (IllegalArgumentException e) {
            // Если проверка пути выявила ошибку, выбрасываем исключение.
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public WinCheckResponse getStatus() {
        return lastGameStatus;
    }

    public Board getBoard() {
        return board;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    /**
     * Инициализирует игроков и назначает им цвета фигур.
     */
    private void initPlayers() {
        // Назначаем цвета фигур игрокам случайным образом.
        assignFigureColors();
        // Определяем, какой из игроков играет белыми фигурами и назначаем его активным игроком.
        activePlayer = getPlayerWithWhites();
    }

    /**
     * Расставляет черные фигуры на начальные позиции.
     */
    private void initBlackFigures() {
        for (Point point : CheckersParams.getWhitesStartPoints()) {
            board.addFigure(new Figure(point, Figure.Color.BLACK), point);
        }
    }

    /**
     * Расставляет белые фигуры на начальные позиции.
     */
    private void initWhiteFigures() {
        for (Point point : CheckersParams.getBlacksStartPoints()) {
            board.addFigure(new Figure(point, Figure.Color.WHITE), point);
        }
    }

    /**
     * Назначает случайным образом цвета фигур игрокам.
     */
    private void assignFigureColors() {
        Random random = new Random();

        if (random.nextBoolean()) {
            player1.setColor(Figure.Color.WHITE);
            player2.setColor(Figure.Color.BLACK);
        } else {
            player1.setColor(Figure.Color.BLACK);
            player2.setColor(Figure.Color.WHITE);
        }
    }

    /**
     * Возвращает игрока, у которого фигуры белого цвета.
     *
     * @return Игрок с белыми фигурами.
     */
    private Player getPlayerWithWhites() {
        return player1.getColor() == Figure.Color.WHITE ? player1 : player2;
    }

    /**
     * Меняет активного игрока на противоположного.
     */
    private void changeActivePlayer() {
        activePlayer = activePlayer == player1 ? player2 : player1;
    }

    /**
     * Внутренний класс, содержащий параметры для игры в уголки.
     */
    public static class CheckersParams {
        private static List<Point> whitesStartPoints = new ArrayList<Point>();
        private static List<Point> blacksStartPoints = new ArrayList<Point>();

        static {
            whitesStartPoints.add(new Point(0, 7));
            whitesStartPoints.add(new Point(1, 7));
            whitesStartPoints.add(new Point(2, 7));
            whitesStartPoints.add(new Point(3, 7));
            whitesStartPoints.add(new Point(0, 6));
            whitesStartPoints.add(new Point(1, 6));
            whitesStartPoints.add(new Point(2, 6));
            whitesStartPoints.add(new Point(3, 6));
            whitesStartPoints.add(new Point(0, 5));
            whitesStartPoints.add(new Point(1, 5));
            whitesStartPoints.add(new Point(2, 5));
            whitesStartPoints.add(new Point(3, 5));

            blacksStartPoints.add(new Point(4, 0));
            blacksStartPoints.add(new Point(5, 0));
            blacksStartPoints.add(new Point(6, 0));
            blacksStartPoints.add(new Point(7, 0));
            blacksStartPoints.add(new Point(4, 1));
            blacksStartPoints.add(new Point(5, 1));
            blacksStartPoints.add(new Point(6, 1));
            blacksStartPoints.add(new Point(7, 1));
            blacksStartPoints.add(new Point(4, 2));
            blacksStartPoints.add(new Point(5, 2));
            blacksStartPoints.add(new Point(6, 2));
            blacksStartPoints.add(new Point(7, 2));
        }

        /**
         * Возвращает начальные позиции для белых фигур.
         *
         * @return Список точек с начальными позициями белых фигур.
         */
        public static List<Point> getWhitesStartPoints() {
            return whitesStartPoints;
        }

        /**
         * Возвращает начальные позиции для черных фигур.
         *
         * @return Список точек с начальными позициями черных фигур.
         */
        public static List<Point> getBlacksStartPoints() {
            return blacksStartPoints;
        }
    }

    /**
     * @param board - Игровая доска
     * @param currentPlayer - Текущий игрок
     * @return
     */
    public int evaluation(Board board, Player currentPlayer) {
        int score = 0;

        // Оценка за количество захваченных углов
        int currentPlayerCornerCount = countPlayerCorners(currentPlayer);
        int opponentCornerCount = countPlayerCorners(getOpponent(currentPlayer));

        score = currentPlayerCornerCount - opponentCornerCount;

        // Оценка за количество фигур на доске
        int currentPlayerPieceCount = countPlayerPieces(currentPlayer);
        int opponentPieceCount = countPlayerPieces(getOpponent(currentPlayer));

        score += (currentPlayerPieceCount - opponentPieceCount);

        return score;
    }

    /**
     * Метод для подсчета количества фигур игрока на доске
     */
    public int countPlayerPieces(Player player) {
        int pieceCount = 0;
        // Перебираем все клетки на доске
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Cell cell = board.getCellAt(x, y);
                // Если клетка не пуста и фигура на ней принадлежит текущему игроку
                if (!cell.isEmpty() && cell.getFigure().getColor() == player.getColor()) {
                    pieceCount++;
                }
            }
        }

        return pieceCount;
    }
    // Метод для определения противника текущего игрока
    public Player getOpponent(Player currentPlayer) {
        if (currentPlayer == player1) {
            return player2; // Если текущий игрок - player1, то противник - player2
        } else {
            return player1; // В противном случае, противник - player1
        }
    }


    /**
     * Метод для подсчета количества захваченных углов игроком
     */
    public int countPlayerCorners(Player player) {
        int cornerCount = 0;

        // Проверяем верхний левый угол
        if (board.getCellAt(0, 0).getFigure() != null && board.getCellAt(0, 0).getFigure().getColor() == player.getColor()) {
            cornerCount++;
        }

        // Проверяем верхний правый угол
        if (board.getCellAt(0, 7).getFigure() != null && board.getCellAt(0, 7).getFigure().getColor() == player.getColor()) {
            cornerCount++;
        }

        // Проверяем нижний левый угол
        if (board.getCellAt(7, 0).getFigure() != null && board.getCellAt(7, 0).getFigure().getColor() == player.getColor()) {
            cornerCount++;
        }

        // Проверяем нижний правый угол
        if (board.getCellAt(7, 7).getFigure() != null && board.getCellAt(7, 7).getFigure().getColor() == player.getColor()) {
            cornerCount++;
        }

        return cornerCount;
    }



}