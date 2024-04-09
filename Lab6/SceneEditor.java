package Lab6;

// Rectangle is car
// Oval is tree
// Square is house
// Circle is cloud

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// SceneFrame class
class SceneFrame extends JFrame {
    private SceneComponent scene;
    private JComboBox<String> shapeChooser;

    public SceneFrame() {
        scene = new SceneComponent();
        add(scene, BorderLayout.CENTER);

        shapeChooser = new JComboBox<>(new String[]{"Car", "House", "Tree", "Cloud"});
        JButton addShapeButton = new JButton("Add Shape");
        addShapeButton.addActionListener(e -> {
            String selectedShape = (String) shapeChooser.getSelectedItem();
            switch (selectedShape) {
                case "Car":
                    scene.addShape(new CarShape(20, 20, 50));
                    break;
                case "House":
                    scene.addShape(new HouseShape(100, 100, 50));
                    break;
                case "Tree":
                    scene.addShape(new TreeShape(150, 150, 50));
                    break;
                case "Cloud":
                    scene.addShape(new CloudShape(200, 50, 60));
                    break;
            }
        });

        JButton removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(e -> scene.removeSelected());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(shapeChooser);
        buttonsPanel.add(addShapeButton);
        buttonsPanel.add(removeButton);

        add(buttonsPanel, BorderLayout.SOUTH);
        pack();
    }
}

// SceneEditor main class
public class SceneEditor {
    public static void main(String[] args) {
        JFrame frame = new SceneFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

// SceneComponent class
class SceneComponent extends JComponent {
    private ArrayList<SceneShape> shapes;
    private SceneShape selectedShape;

    public SceneComponent() {
        shapes = new ArrayList<>();
        MouseAdapter mouseHandler = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                for (SceneShape shape : shapes) {
                    if (shape.getBounds().contains(e.getPoint())) {
                        selectedShape = shape;
                        System.out.println("Shape selected");
                        repaint();
                        return;
                    }
                }
                selectedShape = null;
                repaint();
            }

            public void mouseDragged(MouseEvent e) {
                if (selectedShape != null) {
                    int dx = e.getX() - selectedShape.getBounds().x;
                    int dy = e.getY() - selectedShape.getBounds().y;
                    selectedShape.moveBy(dx, dy);
                    repaint();
                }
            }
        };
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    public void addShape(SceneShape shape) {
        shapes.add(shape);
        repaint();
    }

    public void removeSelected() {
        if (selectedShape != null) {
            System.out.println("Removing shape");
            shapes.remove(selectedShape);
            selectedShape = null;
            repaint();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (SceneShape shape : shapes) {
            shape.draw(g);
            if (shape == selectedShape) {
                shape.drawSelection(g);
            }
        }
    }
}

// SceneShape interface
interface SceneShape {
    void draw(Graphics g);
    void drawSelection(Graphics g);
    Rectangle getBounds();
    void moveBy(int dx, int dy);
}

// SelectableShape class
abstract class SelectableShape implements SceneShape {
    public void drawSelection(Graphics g) {
        Rectangle bounds = getBounds();
        final int GRABBER_SIZE = 4;
        int x1 = bounds.x - GRABBER_SIZE / 2;
        int y1 = bounds.y - GRABBER_SIZE / 2;
        int x2 = bounds.x + bounds.width - GRABBER_SIZE / 2;
        int y2 = bounds.y + bounds.height - GRABBER_SIZE / 2;

        g.fillRect(x1, y1, GRABBER_SIZE, GRABBER_SIZE);
        g.fillRect(x2, y1, GRABBER_SIZE, GRABBER_SIZE);
        g.fillRect(x1, y2, GRABBER_SIZE, GRABBER_SIZE);
        g.fillRect(x2, y2, GRABBER_SIZE, GRABBER_SIZE);
    }
}

// new CarShape class
class CarShape extends SelectableShape {
    private int x, y, width;

    public CarShape(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public void draw(Graphics g) {
        // Draw the main rectangle (car body)
        g.drawRect(x, y, width, width / 2);

        // Draw the top rectangle (car cabin)
        int cabinWidth = width / 2;
        int cabinHeight = width / 4;
        int cabinX = x + width / 4;
        int cabinY = y - cabinHeight / 2;
        g.drawRect(cabinX, cabinY, cabinWidth, cabinHeight);
    }

    public Rectangle getBounds() {
        // Adjust the bounds to include the entire car (body and cabin)
        return new Rectangle(x, y - width / 4, width, width / 2 + width / 4);
    }

    public void moveBy(int dx, int dy) {
        x += dx;
        y += dy;
    }
}



// new HouseShape class
class HouseShape extends SelectableShape {
    private int x, y, width;

    public HouseShape(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public void draw(Graphics g) {
        // Draw the square base of the house
        g.drawRect(x, y, width, width);

        // Draw the triangular roof
        int[] xPoints = {x, x + width / 2, x + width};
        int[] yPoints = {y, y - width / 2, y};
        g.drawPolygon(xPoints, yPoints, 3);
    }

    public Rectangle getBounds() {
        // Adjust the bounds to include the roof
        return new Rectangle(x, y - width / 2, width, width + width / 2);
    }

    public void moveBy(int dx, int dy) {
        x += dx;
        y += dy;
    }
}


// TreeShape class
class TreeShape extends SelectableShape {
    private int x, y, width;

    public TreeShape(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public void draw(Graphics g) {
        g.drawRect(x, y - width / 2, width, width / 2); // Trunk
        g.drawOval(x - width / 2, y - width, width * 2, width); // Foliage
    }

    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y - width, width * 2, width + width / 2);
    }

    public void moveBy(int dx, int dy) {
        x += dx;
        y += dy;
    }
}

// CloudShape class
class CloudShape extends SelectableShape {
    private int x, y, width;

    public CloudShape(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public void draw(Graphics g) {
        g.drawOval(x, y, width / 2, width / 2);
        g.drawOval(x + width / 4, y - width / 4, width / 2, width / 2);
        g.drawOval(x + width / 2, y, width / 2, width / 2);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y - width / 4, width, width / 2);
    }

    public void moveBy(int dx, int dy) {
        x += dx;
        y += dy;
    }
}
