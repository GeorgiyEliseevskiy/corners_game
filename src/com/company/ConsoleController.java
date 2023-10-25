package com.company;

import com.company.Game.InterfaceDrawer;
import com.company.Game.UgolkiGame;
import com.company.Game.WinCheckResponse;

import java.util.Scanner;

/**
 * Консольный контроллер считывает команды из командной строки и передает их в игру.
 *
 * Этот класс представляет собой контроллер для консольного пользовательского интерфейса. Он считывает
 * команды игрока из командной строки и взаимодействует с моделью игры и интерфейсом для обработки
 * игровой логики и отображения.
 *
 * @author Savva Kodeikin
 */
public class ConsoleController {

    private InterfaceDrawer interfaceDrawer;
    private UgolkiGame game;

    /**
     * Создает новый объект ConsoleController с указанным интерфейсом и моделью игры.
     *
     * @param drawer Интерфейс для отрисовки игры.
     * @param game   Модель игры, управляемая контроллером.
     */
    public ConsoleController(InterfaceDrawer drawer, UgolkiGame game) {
        this.interfaceDrawer = drawer;
        this.game = game;
    }

    /**
     * Запускает игру и обрабатывает ходы игроков через ввод с консоли.
     */
    public void startGame() {
        game.startGame();
        WinCheckResponse gameStatus = game.getStatus();
        Scanner scanner = new Scanner(System.in);

        while (gameStatus.getStatus() == WinCheckResponse.Status.JUST_STARTED ||
                gameStatus.getStatus() == WinCheckResponse.Status.IN_PROGRESS) {
            interfaceDrawer.draw(game);

            System.out.println("Введите ход, например, B3 B4");
            String[] command = scanner.nextLine().toUpperCase().split(" ");

            try {
                game.move(command[0], command[1]);
            } catch (IllegalArgumentException e) {
                System.out.println("Исключение: " + e.getMessage());
            }
            gameStatus = game.getStatus();
        }
        proclaimResults(gameStatus);
    }

    private void proclaimResults(WinCheckResponse gameStatus) {
        if (gameStatus.getStatus() == WinCheckResponse.Status.FINISHED) {
            interfaceDrawer.proclaimWinner(gameStatus.getWinner());
        } else {
            interfaceDrawer.proclaimDeadHeat();
        }
    }
}
