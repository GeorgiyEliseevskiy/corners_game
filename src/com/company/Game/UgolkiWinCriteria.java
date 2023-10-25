package com.company.Game;

import com.company.Model.Board;
import com.company.Model.Figure;
import com.company.Model.Player;
import com.company.Model.Point;
import java.util.List;

public class UgolkiWinCriteria{
    private List<Point> blacksWinPoints = UgolkiGame.CheckersParams.getWhitesStartPoints();
    private List<Point> whitesWinPoints = UgolkiGame.CheckersParams.getBlacksStartPoints();
    private Player whitesPlayer;
    private boolean fightForDeadHeat = false;

    /**
     * Проверяет статус игры. Если все черные фигуры находятся на позициях старта для белых фигур,
     * активный игрок выигрывает. Однако, если все белые фигуры находятся на позициях старта для черных фигур,
     * игрок, играющий черными, получает еще один ход для завершения игры, так как игрок, играющий белыми, начал первым.
     * В случае, когда игрок, играющий черными, размещает все свои фигуры на позициях старта для белых фигур,
     * это ничейная ситуация.
     *
     * @param board        Игровая доска
     * @param activePlayer Текущий игрок
     * @return Ответ о статусе игры, содержащий статус игры
     */
    public WinCheckResponse checkForWinner(Board board, Player activePlayer) {
        List<Point> playerFiguresPoints = board.getFiguresPoints(activePlayer.getColor());
        List<Point> winPositions = getWinPositions(activePlayer);
        WinCheckResponse response = new WinCheckResponse(WinCheckResponse.Status.IN_PROGRESS);

        if (activePlayer.getColor() == Figure.Color.BLACK) {
            if (fightForDeadHeat) {
                if (playerFiguresPoints.containsAll(winPositions)) {
                    response.setStatus(WinCheckResponse.Status.DEAD_HEAT);
                } else {
                    response.setStatus(WinCheckResponse.Status.FINISHED);
                    response.setWinner(whitesPlayer);
                }
            } else {
                if (playerFiguresPoints.containsAll(winPositions)) {
                    response.setStatus(WinCheckResponse.Status.FINISHED);
                    response.setWinner(activePlayer);
                }
            }
        } else {
            if (playerFiguresPoints.containsAll(winPositions)) {
                whitesPlayer = activePlayer;
                fightForDeadHeat = true;
            }
        }

        return response;
    }

    private List<Point> getWinPositions(Player activePlayer) {
        return activePlayer.getColor() == Figure.Color.WHITE
                ? whitesWinPoints : blacksWinPoints;
    }
}
