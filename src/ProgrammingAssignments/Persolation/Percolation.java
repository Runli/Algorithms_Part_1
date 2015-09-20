package ProgrammingAssignments.Persolation;
import Weak_1.WeightedQuickUnionUF;

/**
 * Created by ilnurgazizov on 20.09.15.
 *
 * Full description of PA-1 Percolation in web site
 * Полное описание PA-1 Percolation на сайте
 *
 * http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *
 * Percolation class represent a N*N grid of cells that can be in two state: open or closed
 */
public class Percolation {

    // Длина одной стороны сетки
    private int N;

    // boolean массив с открытыми клетками
    private boolean[] openCells;

    // Алгоритм Weighted Quick Union
    private WeightedQuickUnionUF unionAlgo;

    // Индекс верхней виртуальной клетки
    private int virtualTopCell;

    // Индекс нижней виртуальной клетки
    private int virtualBottomCell;

    // Конструктор, может выдать Исключение при N <= 0
    public Percolation(int N) {               // create N-by-N grid, with all sites blocked
        if (N <= 0){ throw new IllegalArgumentException("Trying to create grid with negative or zero amounts of cells!");}
        this.N = N;
        this.virtualTopCell = 0;
        this.virtualBottomCell = N * N + 1;

        this.openCells = new boolean[virtualBottomCell + 1];
        this.unionAlgo = new WeightedQuickUnionUF(virtualBottomCell + 1);

        // Виртуальные клетки должны быть открытыми
        openCells[virtualTopCell] = true;
        openCells[virtualBottomCell] = true;
    }

    // Метод open открывает некоторые клетки, если соответсвующий нашей клетке
    // (1, 1) верхняя левая клетка
    public void open(int i, int j) {          // open site (row i, column j) if it is not open already

        int index = getIndexFromCoords(i, j);

        openCells[index] = true;

        // проверяем клетку сверху и объединяем если открыта
        if (j == 1){
            unionAlgo.union(index, virtualTopCell);
        } else {
            if (openCells[index - N]){
                unionAlgo.union(index, index - N);
            }
        }

        // проверяем клетку снизу и объединяем если открыта
        if (j == N){
            unionAlgo.union(index, virtualBottomCell);
        } else {
            if (openCells[index + N]){
                unionAlgo.union(index, index + N);
            }
        }

        // проверяем клетку слева и объединяем если открыта
        if (i > 1){
            if (openCells[index - 1]) {
                unionAlgo.union(index, index - 1);
            }
        }

        // проверяем клетку справа и объединяем если открыта
        if (i < N){
            if (openCells[index + 1]) {
                unionAlgo.union(index, index + 1);
            }
        }
    }

    // метод isOpen возвращает true если клетка с координатами (i, j) открыта, или false если закрыта
    public boolean isOpen(int i, int j) {     // is site (row i, column j) open?
        int index = getIndexFromCoords(i, j);
        return openCells[index];
    }

    // метод isFull возвращает true если клетка (i, j) соединена с виртуальныой клеткой сверху, и false если нет
    public boolean isFull(int i, int j) {     // is site (row i, column j) full?
        int index = getIndexFromCoords(i, j);
        return unionAlgo.connected(index, virtualTopCell);
    }

    // возвращает true если сетка может быть percolated(если виртуальные клетки сверху и снизу) или фалс
    public boolean percolates() {             // does the system percolate?
        return unionAlgo.connected(virtualTopCell, virtualBottomCell);
    }

    // Проверка что (i, j) внутри сетки
    private void checkIndex(int i, int j){
        if (i <= 0 || j <= 0 || i > N || j > N){ throw new IndexOutOfBoundsException("A cell out of the grid");}
    }

    // Конвертация 2D координаты (i, j) в 1-мерный
    private int getIndexFromCoords(int i, int j){
        checkIndex(i, j);
        return (j - 1) * N + i;
    }
}
