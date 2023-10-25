package com.company.Game;

import com.company.Model.Player;

/**
 * Класс, представляющий ответ, который содержит статус игры и информацию о победителе, если он есть.
 */
public class WinCheckResponse {

    private Status status;  // Статус игры
    private Player winner;  // Победитель, если есть

    public WinCheckResponse() {}

    public WinCheckResponse(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public enum Status {
        /**
         * Новая игра.
         */
        JUST_STARTED,
        /**
         * Игра продолжается.
         */
        IN_PROGRESS,

        /**
         * Игра завершена, и есть победитель.
         */
        FINISHED,
        /**
         * Игра завершена.
         * Оба игрока имеют одинаковое количество очков.
         */
        DEAD_HEAT
    }
}
