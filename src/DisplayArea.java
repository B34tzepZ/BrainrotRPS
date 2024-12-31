import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;

public class DisplayArea extends JPanel {
    Shape shape;
    int startingCapacity;
    int maxCapacity;

    /*
    private static final int POINT_SIZE = 10;
    private final List<Point> points = new LinkedList<>();
    private Point currentMousePosition = new Point();*/

    DisplayArea(final int width, final int height, Shape shape, int startingCapacity, int maxCapacity) {
        super();
        this.shape = shape;
        this.startingCapacity = startingCapacity;
        this.maxCapacity = maxCapacity;
        this.setPreferredSize(new Dimension(width, height));
        this.addMouseListener(new ClickListener());
        this.addMouseMotionListener(new MotionListener());
    }

    public static JFrame newWindow(Shape shape, int startingCapacity, int maxCapacity) {
        final JFrame frame = new JFrame();
        final DisplayArea displayArea = new DisplayArea(1920, 1080, shape, startingCapacity, maxCapacity);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(displayArea, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }

    class ClickListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

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
