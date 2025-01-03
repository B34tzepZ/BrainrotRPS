import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;


public class DisplayArea extends JPanel implements Normalizer {
    private static final int PADDING = 10;
    Shape shape;
    private final List<GameListener> listeners = new ArrayList<>();


    /*
    private static final int POINT_SIZE = 10;
    private final List<Point> points = new LinkedList<>();
    private Point currentMousePosition = new Point();*/
    DisplayArea(final int width, final int height, Shape shape) {
        super();
        this.setPreferredSize(new Dimension(width, height));
        this.shape = shape;
        this.addMouseListener(new ClickListener());
        this.addMouseMotionListener(new MotionListener());
    }

    public static JFrame newWindow(Shape shape, int startingCapacity, int maxCapacity) {
        final JFrame frame = new JFrame();
        final JPanel panel = new JPanel();
        final DisplayArea shapeArea = new DisplayArea(1920, 1080, shape);
        final InfoPanel capacityArea = new InfoPanel(400, shapeArea.getHeight(), startingCapacity, maxCapacity);
        shapeArea.addListener(capacityArea);

        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(shapeArea);
        panel.add(capacityArea);

        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);
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
        g2D.setColor(Color.darkGray);
        g2D.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2D.setColor(Color.white);
        this.shape.drawFunction.draw(g2D, this);

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

    class ClickListener implements MouseListener {
        private Point pressPoint;
        private Point releasePoint;

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            pressPoint = e.getPoint();
            NormalizedPoint normalizedPoint = normalize(new Point(pressPoint.x - PADDING, pressPoint.y - PADDING));
            //TODO: Herausfinden ob innerhalb des Shapes
            if (normalizedPoint.x() < 0.5 && (normalizedPoint.x() + normalizedPoint.y()) < 1) {
                listeners.forEach(listener -> listener.onAdded(Tool.ROCK));
            }
            else if (normalizedPoint.x() > 0.5 && normalizedPoint.x() > normalizedPoint.y()) {
                listeners.forEach(listener -> listener.onAdded(Tool.SCISSORS));
            }
            else listeners.forEach(listener -> listener.onAdded(Tool.PAPER));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            releasePoint = e.getPoint();
            SwingUtilities.invokeLater(() -> {
                Graphics g = getGraphics();
                g.setColor(Color.white);
                g.drawLine(pressPoint.x, pressPoint.y, releasePoint.x, releasePoint.y);
            });
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
}
