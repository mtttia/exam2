package a02b.e2;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ControllerImpl implements Controller {

    enum Direction {
        Up, Left, Rigth
    }

    private Map<Pair<Integer, Integer>, Cell> grid;
    private Cell lastPlayerCell = null;
    private GUI gui;
    private Direction direction;
    private int size;
    private Pair<Integer, Integer> pos;

    public ControllerImpl(GUI gui, int size) {
        this.gui = gui;
        direction = Direction.Up;
        this.size = size;
    }

    @Override
    public void move() {
        getNextPoint();
        if (isOut(pos)) {
            gameOver();
        }
    }

    private void gameOver() {
        this.gui.close();
        this.gui = new GUI(size);
    }

    private Pair<Integer, Integer> getNextPoint() {
        Cell last;
        if (lastPlayerCell != null) {
            last = lastPlayerCell;
        } else {
            last = Cell.Empty;
        }
        grid.put(pos, last);

        switch (this.direction) {
            case Up:
                pos = new Pair<Integer, Integer>(pos.getX() - 1, pos.getY());
                break;
            case Left:
                pos = new Pair<Integer, Integer>(pos.getX(), pos.getY() - 1);
                break;
            case Rigth:
                pos = new Pair<Integer, Integer>(pos.getX(), pos.getY() + 1);
                break;
        }

        if (!isOut(pos)) {
            lastPlayerCell = grid.get(pos);
            if (lastPlayerCell == Cell.Left) {
                direction = Direction.Left;
            } else if (lastPlayerCell == Cell.Right) {
                direction = Direction.Rigth;
            }
            grid.put(pos, Cell.Pointer);
        }

        return null;
    }

    public boolean isOut(Pair<Integer, Integer> pos) {
        return pos.getX() < 0 ||
                pos.getY() < 0 ||
                pos.getX() >= size ||
                pos.getY() >= size;
    }

    private void loadEmptyMap() {
        grid = new HashMap<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                var pair = new Pair<Integer, Integer>(i, j);
                grid.put(pair, Cell.Empty);
            }
        }
    }

    public Map<Pair<Integer, Integer>, Cell> loadMap() {
        loadEmptyMap();
        Random r = new Random();
        do {
            pos = new Pair<Integer, Integer>(size - 1, r.nextInt(0, size));
            grid.put(pos, Cell.Pointer);
        } while (!grid.containsKey(pos));

        var NUM_CHARS = 20;
        boolean isLeft = true;
        for (int i = 0; i < NUM_CHARS; i++) {
            Pair<Integer, Integer> p;
            do {
                var x = r.nextInt(0, size);
                var y = r.nextInt(0, size);
                p = new Pair<Integer, Integer>(x, y);
            } while (p.equals(pos) || !grid.containsKey(p));

            if (isLeft) {
                grid.put(p, Cell.Right);
            } else {
                grid.put(p, Cell.Left);
            }
            isLeft = !isLeft;
        }

        return grid;
    }

    @Override
    public Map<Pair<Integer, Integer>, Cell> getMap() {
        return grid;
    }

}
