package com.company;

import com.company.Game.InterfaceDrawer;
import com.company.Game.UgolkiGame;
import com.company.Game.UgolkiPathChecker;
import com.company.Game.UgolkiWinCriteria;
import com.company.Model.Board;
import com.company.Model.Player;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        InterfaceDrawer drawer = new InterfaceDrawer();
        UgolkiPathChecker pathChecker = new UgolkiPathChecker(board);
        Player player1 = new Player("Dummy Player");
        Player player2 = new Player("Test Player");
        UgolkiWinCriteria ugolkiWinCriteria = new UgolkiWinCriteria();
        UgolkiGame game = new UgolkiGame(board, player1, player2, pathChecker, ugolkiWinCriteria);
        ConsoleController controller = new ConsoleController(drawer, game);
        controller.startGame();
    }
}
