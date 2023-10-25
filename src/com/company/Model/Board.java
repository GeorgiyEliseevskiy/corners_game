package com.company.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    public static final char[] CHARACTERS = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' };
    private static final int DEFAULT_WIDTH = 8;
    private static final int DEFAULT_HEIGHT = 8;
    private Map<Point, Cell> cells;
    private Map<String, Cell> cellsByName;
    private int width;
    private int height;

    public Board() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new HashMap<Point, Cell>();
        cellsByName = new HashMap<String, Cell>();
        createCells();
    }

    /**
     * Метод добавление фигуры в указнную точку
     *
     * @param figure Фигура
     * @param point Точка на доске
     */
    public void addFigure(Figure figure, Point point) {
        this.cells.get(point).setFigure(figure);
    }

    public void cleanCells() {
        for (Cell cell : this.cells.values()) {
            cell.removeFigure();
        }
    }

    /**
     * Метод для получения ячейки в указанных координатах
     *
     * @param x X - координата
     * @param y Y -координата
     * @return Ячейку с заданными координатами
     */
    public Cell getCellAt(int x, int y) {
        return this.cells.get(new Point(x, y));
    }


    /**
     * Получить список координат точек, на которых находятся фигуры указанного цвета.
     *
     * @param color Цвет фигур, которые необходимо найти.
     * @return Список точек с координатами фигур указанного цвета.
     */
    public List<Point> getFiguresPoints(Figure.Color color) {
        List<Point> points = new ArrayList<Point>();
        // Перебираем все клетки на доске
        for (Map.Entry<Point, Cell> e : cells.entrySet()) {
            // Проверяем, что клетка не пуста и цвет фигуры в клетке совпадает с указанным цветом
            if (!e.getValue().isEmpty() && e.getValue().getFigure().getColor() == color) {
                points.add(e.getKey());
            }
        }

        return points;
    }

    /**
     * Метод для получения списка соседних клеток относительно заданной клетки.
     *
     * @param cell Клетка, относительно которой ищутся соседние клетки.
     * @return Список соседних клеток.
     */
    public List<Cell> getNeighbourCells(Cell cell) {
        List<Cell> cells = new ArrayList<Cell>();
        // Добавляем соседние клетки с учетом смещения по горизонтали и вертикали
        addNeighbourCell(cells, cell, 1, 0); // Вправо
        addNeighbourCell(cells, cell, -1, 0); // Влево
        addNeighbourCell(cells, cell, 0, 1); // Вниз
        addNeighbourCell(cells, cell, 0, -1); // Вверх

        return cells;
    }

    /**
     * Получить клетку по текстовому адресу, например, "A1" или "H8".
     *
     * @param address Текстовый адрес клетки.
     * @throws IllegalArgumentException Если адрес не найден.
     * @return Объект клетки.
     */
    public Cell getCellAt(String address) {
        Cell cell = cellsByName.get(address);

        if (cell == null) {
            // Если клетка с указанным адресом не найдена, выбрасываем исключение
            throw new IllegalArgumentException("Клетка с адресом " + address + " не найдена.");
        }

        return cell;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Создание и инициализация клеток (ячеек) на игровой доске.
     */
    private void createCells() {
        // Перебираем координаты по горизонтали (x)
        for (int x = 0; x < getWidth(); x++) {
            // Перебираем координаты по вертикали (y)
            for (int y = 0; y < getHeight(); y++) {
                // Формируем имя клетки, используя буквенные координаты и числовую координату
                String name = CHARACTERS[x] + String.valueOf(y + 1);
                // Инвертируем координату по вертикали для корректного представления на доске
                int inversedY = getWidth() - (y + 1);
                // Создаем объект клетки с заданным именем и координатами
                Cell cell = new Cell(name, x, inversedY);
                // Создаем объект точки, используя координаты x и инвертированную y
                Point point = new Point(x, inversedY);
                // Добавляем созданный объект клетки в карту cells с использованием точки в качестве ключа
                this.cells.put(point, cell);
                // Добавляем созданный объект клетки в карту cellsByName с использованием имени в качестве ключа
                this.cellsByName.put(name, cell);
            }
        }
    }

    /**
     * Добавление соседней клетки в список соседних клеток относительно заданной клетки.
     *
     * @param cells Список соседних клеток.
     * @param cell Заданная клетка, относительно которой ищется сосед.
     * @param offsetX Смещение по горизонтали.
     * @param offsetY Смещение по вертикали.
     */
    private void addNeighbourCell(List<Cell> cells, Cell cell, int offsetX, int offsetY) {
        // Получаем соседнюю клетку, добавляя смещение к координатам заданной клетки
        Cell neighbourCell = getCellAt(cell.getX() + offsetX, cell.getY() + offsetY);

        // Проверяем, что соседняя клетка существует (не равна null)
        if (neighbourCell != null) {
            // Если соседняя клетка существует, добавляем её в список соседних клеток
            cells.add(neighbourCell);
        }
    }
}

