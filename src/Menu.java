import javax.swing.*;
import java.awt.*;

public class Menu {
    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.add(panel);

        final JLabel shapeLabel = new JLabel("Shape");
        shapeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(shapeLabel);

        final JComboBox<Shape> shape = new JComboBox<>(Shape.values());
        panel.add(shape);

        final JLabel startingCapacityLabel = new JLabel("Starting Capacity");
        startingCapacityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(startingCapacityLabel);

        final Integer[] startingCapacityOptions = {5, 10, 20, 50};
        final JComboBox<Integer> startingCapacities = new JComboBox<>(startingCapacityOptions);
        panel.add(startingCapacities);

        final JLabel maxCapacityLabel = new JLabel("Max Capacity");
        maxCapacityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(maxCapacityLabel);

        final JCheckBox useStartingCapacity = new JCheckBox("Same as Starting Capacity");
        useStartingCapacity.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(useStartingCapacity);

        final JSpinner spinner = new JSpinner();
        panel.add(spinner);

        final JButton button = new JButton("GO");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button);

        button.addActionListener(e -> {
            int maxCapacity = useStartingCapacity.isSelected() ? (int) startingCapacities.getSelectedItem() : (int) spinner.getValue();
            JFrame gameWindow = DisplayArea.newWindow((Shape) shape.getSelectedItem(), (int) startingCapacities.getSelectedItem(), maxCapacity);
            SwingUtilities.invokeLater(() -> gameWindow.setVisible(true));
            SwingUtilities.invokeLater(() -> frame.setVisible(false));
        });
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
}