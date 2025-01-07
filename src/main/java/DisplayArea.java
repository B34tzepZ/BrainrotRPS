import org.realityforge.vecmath.Vector2d;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DisplayArea extends JPanel implements Normalizer {
    private static final int PADDING = 10;
    Shape shape;
    BufferedImage rockImage;
    BufferedImage paperImage;
    BufferedImage scissorsImage;

    private final List<GameListener> listeners = new ArrayList<>();

    private List<MovingObject> movingObjects = new ArrayList<>();

    public int getPadding() {
        return PADDING;
    }

    /*
    private static final int POINT_SIZE = 10;
    private final List<Point> points = new LinkedList<>();
    private Point currentMousePosition = new Point();*/
    DisplayArea(final int width, final int height, Shape shape) throws IOException {
        super();
        this.setPreferredSize(new Dimension(width, height));
        this.shape = shape;
        this.setBackground(Color.darkGray);
        this.addMouseListener(new ClickListener());
        this.addMouseMotionListener(new MotionListener());

        Timer timer = new Timer(16, e -> {
            for (int i = 0; i < listeners.get(0).gameSpeed(); i++) {
                movingObjects.forEach(MovingObject::updateMovement);
                movingObjects = MovingObject.eliminateObjects(movingObjects, 20);
                SwingUtilities.invokeLater(this::repaint);
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    public static JFrame newWindow(Shape shape, int startingCapacity, int maxCapacity, BufferedImage rockImage, BufferedImage paperImage, BufferedImage scissorsImage) throws IOException {
        final JFrame frame = new JFrame();
        //final JPanel panel = new JPanel();
        final DisplayArea shapeArea = new DisplayArea(1920, 1080, shape);
        final InfoPanel capacityArea = new InfoPanel(200, shapeArea.getHeight(), startingCapacity, maxCapacity);
        shapeArea.rockImage = rockImage;
        shapeArea.paperImage = paperImage;
        shapeArea.scissorsImage = scissorsImage;
        shapeArea.addListener(capacityArea);

        //panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        //panel.add(shapeArea);
        //panel.add(capacityArea);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(shapeArea, BorderLayout.CENTER);
        frame.getContentPane().add(capacityArea, BorderLayout.EAST);
        //frame.setLayout(new BorderLayout());
        //frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }

    void addListener(GameListener listener) {
        listeners.add(listener);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        final Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setColor(Color.white);
        this.shape.drawFunction.draw(g2D, this);
        movingObjects.forEach(movingObject -> {
            switch (movingObject.tool) {
                case ROCK -> g2D.drawImage(rockImage, movingObject.location.x, movingObject.location.y, null);
                case PAPER -> g2D.drawImage(paperImage, movingObject.location.x, movingObject.location.y, null);
                case SCISSORS -> g2D.drawImage(scissorsImage, movingObject.location.x, movingObject.location.y, null);
            }
        });
    }

    @Override
    public NormalizedPoint normalize(Point point) {

        final double x = ((double) point.x) / (this.getWidth() - 2 * PADDING);
        final double y = ((double) point.y) / (this.getHeight() - 2 * PADDING);

        return new NormalizedPoint(x, y);
    }

    @Override
    public Point denormalize(NormalizedPoint normalizedPoint) {

        final int x = PADDING + (int) (normalizedPoint.x() * (this.getWidth() - 2 * PADDING));
        final int y = PADDING + (int) (normalizedPoint.y() * (this.getHeight() - 2 * PADDING));

        return new Point(x, y);
    }

    public boolean withinShape(Point point) {
        switch (shape) {
            case TRIANGLE -> {
                NormalizedPoint normalizedPoint = normalize(point);
                if (normalizedPoint.y() < 1) {
                    if (normalizedPoint.x() < 0.5 + normalizedPoint.y() / 2 && normalizedPoint.x() > 0.5 - normalizedPoint.y() / 2) {
                        return true;
                    }
                }
            }
            case SQUARE -> {
                if (point.x > PADDING && point.y > PADDING) {
                    if (point.x < getWidth() - PADDING && point.y < getHeight() - PADDING) {
                        return true;
                    }
                }
            }
            case CIRCLE -> {
                NormalizedPoint normalizedPoint = normalize(point);
                NormalizedPoint shiftedPoint = new NormalizedPoint(normalizedPoint.x() - 0.5, normalizedPoint.y() - 0.5);
                if (((shiftedPoint.x() * shiftedPoint.x()) / (0.5 * 0.5) + (shiftedPoint.y() * shiftedPoint.y()) / (0.5 * 0.5)) < 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public Tool getSelectedTool(Point point) {
        NormalizedPoint normalizedPoint = normalize(new Point(point.x - PADDING, point.y - PADDING));
        if (normalizedPoint.x() < 0.5 && (normalizedPoint.x() + normalizedPoint.y()) < 1) {
            return Tool.ROCK;
        } else if (normalizedPoint.x() > 0.5 && normalizedPoint.x() > normalizedPoint.y()) {
            return Tool.SCISSORS;
        } else return Tool.PAPER;
    }

    class ClickListener implements MouseListener {
        private Point pressPoint;
        private Point releasePoint;

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            pressPoint = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            releasePoint = e.getPoint();
            if (listeners.get(0).invertedControls()) {
                releasePoint.x = pressPoint.x + (pressPoint.x - releasePoint.x);
                releasePoint.y = pressPoint.y + (pressPoint.y - releasePoint.y);
            }
            if (withinShape(pressPoint)) {
                Vector2d motion = new Vector2d(releasePoint.getX() - pressPoint.getX(), releasePoint.getY() - pressPoint.getY());
                if (motion.x == 0 && motion.y == 0) {
                    motion.set(Math.random() - 0.5, Math.random() - 0.5);
                }
                motion.normalize();
                motion.mul(10);

                /*Tool selectedTool = getSelectedTool(pressPoint);
                String tool = selectedTool.toString();
                if (listeners.get(0).hasCapacity(selectedTool)) {
                    listeners.forEach(listener -> listener.onAdded(selectedTool));
                }
                SwingUtilities.invokeLater(() -> {
                    Graphics g = getGraphics();
                    BufferedImage image = null;
                    try {
                        image = ImageIO.read(new File("src/main/resources/" + tool.toLowerCase() + ".jpg"));
                    } catch (IOException ex) {
                        System.err.println("Image not found");
                    }
                    g.drawImage(image, pressPoint.x, pressPoint.y, null);
                });//*/

                BufferedImage image = null;
                switch (getSelectedTool(pressPoint)) {
                    case ROCK -> image = listeners.stream()
                            .filter(listener -> listener.hasCapacity(Tool.ROCK))
                            .map(listener -> {
                                listener.onAdded(Tool.ROCK);
                                movingObjects.add(new MovingObject(Tool.ROCK, pressPoint, motion));
                                return rockImage;
                            }).findFirst().orElse(null);

                    case SCISSORS -> image = listeners.stream()
                            .filter(listener -> listener.hasCapacity(Tool.SCISSORS))
                            .map(listener -> {
                                listener.onAdded(Tool.SCISSORS);
                                movingObjects.add(new MovingObject(Tool.SCISSORS, pressPoint, motion));
                                return scissorsImage;
                            }).findFirst().orElse(null);

                    case PAPER -> image = listeners.stream()
                            .filter(listener -> listener.hasCapacity(Tool.PAPER))
                            .map(listener -> {
                                listener.onAdded(Tool.PAPER);
                                movingObjects.add(new MovingObject(Tool.PAPER, pressPoint, motion));
                                return paperImage;
                            }).findFirst().orElse(null);
                }


                BufferedImage finalImage = image;
                SwingUtilities.invokeLater(() -> {
                    Graphics g = getGraphics();
                    g.drawImage(finalImage, pressPoint.x, pressPoint.y, null);
                });
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    class MotionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    class MovingObject {
        Tool tool;
        Point location;
        Vector2d motion;

        public MovingObject(Tool tool, Point location, Vector2d motion) {
            this.tool = tool;
            this.location = location;
            this.motion = motion;
        }

        public void updateMovement() {
            location.x += (int) motion.x;
            location.y += (int) motion.y;
            if (!withinShape(location)) {
                switch (shape) {
                    case TRIANGLE -> {
                        if (location.y < getHeight() - PADDING) {
                            Vector2d normal = new Vector2d(2, 1);
                            if (location.x > getWidth() / 2) {
                                normal.set(-2, 1);
                            }

                            motion = calculateReflection(motion, normal);
                        } else {
                            motion.y *= -1;
                        }
                    }

                    case SQUARE -> {
                        if (location.x < PADDING || location.x > getWidth() - PADDING) {
                            motion.x *= -1;
                        } else {
                            motion.y *= -1;
                        }
                    }

                    case CIRCLE -> {
                        Vector2d normal = new Vector2d((double) getWidth() / 2 - location.x, (double) getHeight() / 2 - location.y);
                        motion = calculateReflection(motion, normal);
                    }
                }//*/
            }
        }

        public Vector2d calculateReflection(Vector2d motion, Vector2d normal) {
            double dotMotionNormal = motion.dot(normal);
            double dotNormalNormal = normal.dot(normal);
            double coefficient = 2 * (dotMotionNormal / dotNormalNormal);
            motion.sub(normal.mul(coefficient));
            motion.normalize();
            motion.mul(10);
            return motion;
        }

        public static List<MovingObject> eliminateObjects(List<MovingObject> movingObjects, double maxDistance) {
            for (int i = 0; i < movingObjects.size(); i++) {
                for (int j = i + 1; j < movingObjects.size(); j++) {
                    if (movingObjects.get(i).location.distance(movingObjects.get(j).location) < maxDistance) {
                        int winner = winner(movingObjects.get(i).tool, movingObjects.get(j).tool);
                        if (winner == 1) {
                            movingObjects.remove(movingObjects.get(j));
                        } else if (winner == 2) {
                            movingObjects.remove(movingObjects.get(i));
                        }
                    }
                }
            }
            return movingObjects;
        }

        private static int winner(Tool t1, Tool t2) {
            return (t1.ordinal() - t2.ordinal() + 3) % 3;
        }
    }
}
