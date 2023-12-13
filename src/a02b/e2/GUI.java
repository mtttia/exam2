package a02b.e2;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

    private static final long serialVersionUID = -6218820567019985015L;
    private final Map<Pair<Integer, Integer>, JButton> cells = new HashMap<>();

    private Controller controller;

    public GUI(int size) {
        controller = new ControllerImpl(this, size);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(50 * size, 50 * size);

        JPanel panel = new JPanel(new GridLayout(size, size));
        this.getContentPane().add(panel);

        ActionListener al = e -> {
            controller.move();
            controller.getMap().entrySet().stream().forEach((i) -> updateButton(i.getKey(), i.getValue()));
        };

        var map = controller.loadMap();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                var pair = new Pair<Integer, Integer>(x, y);
                createButton(pair, map.get(pair), al, panel);
            }
        }
        this.setVisible(true);
    }

    private void updateButton(Pair<Integer, Integer> pos, Cell cell) {
        var button = cells.get(pos);
        button.setText(getChar(cell));
    }

    private void createButton(Pair<Integer, Integer> pos, Cell cell, ActionListener al, JPanel panel) {
        final JButton jb = new JButton(getChar(cell));
        this.cells.put(pos, jb);
        jb.addActionListener(al);
        panel.add(jb);
    }

    private String getChar(Cell cell) {
        switch (cell) {
            case Left:
                return "L";
            case Right:
                return "R";
            case Pointer:
                return "*";
            case Empty:
            default:
                return "";
        }
    }

    public void close() {
        this.dispose();
    }
}
