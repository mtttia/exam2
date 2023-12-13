package a02b.e2;

import java.util.Map;

public interface Controller {
    public void move();
    public Map<Pair<Integer, Integer>, Cell> getMap();
    public Map<Pair<Integer, Integer>, Cell> loadMap();
}
