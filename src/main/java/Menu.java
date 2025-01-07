import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Menu {
    public static void main(String[] args) throws IOException {
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
        useStartingCapacity.setSelected(true);
        useStartingCapacity.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(useStartingCapacity);

        final JSpinner spinner = new JSpinner();
        panel.add(spinner);

        final JLabel rockImageLabel = new JLabel("Rock Image");
        rockImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(rockImageLabel);

        final JButton rockImageButton = new JButton("Browse for Rock Image");
        rockImageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(rockImageButton);

        final BufferedImage[] rockImage = {ImageIO.read(new File("src/main/resources/rock.jpg"))};
        rockImageButton.addActionListener(e -> {
            if (uploadPNG(frame, rockImageLabel) != null) {
                rockImage[0] = uploadPNG(frame, rockImageLabel);
            }
        });

        final JLabel paperImageLabel = new JLabel("Paper Image");
        paperImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(paperImageLabel);

        final JButton paperImageButton = new JButton("Browse for Paper Image");
        paperImageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(paperImageButton);

        final BufferedImage[] paperImage = {ImageIO.read(new File("src/main/resources/paper.jpg"))};
        paperImageButton.addActionListener(e -> {
            if (uploadPNG(frame, paperImageLabel) != null) {
                paperImage[0] = uploadPNG(frame, paperImageLabel);
            }
        });

        final JLabel scissorsImageLabel = new JLabel("Scissors Image");
        scissorsImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(scissorsImageLabel);

        final JButton scissorsImageButton = new JButton("Browse for Scissors Image");
        scissorsImageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(scissorsImageButton);

        final BufferedImage[] scissorsImage = {ImageIO.read(new File("src/main/resources/scissors.jpg"))};
        scissorsImageButton.addActionListener(e -> {
            if (uploadPNG(frame, scissorsImageLabel) != null) {
                scissorsImage[0] = uploadPNG(frame, scissorsImageLabel);
            }
        });

        final JButton button = new JButton("GO");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button);

        button.addActionListener(e -> {
            int maxCapacity = useStartingCapacity.isSelected() || (int) spinner.getValue() < (int) startingCapacities.getSelectedItem() ? (int) startingCapacities.getSelectedItem() : (int) spinner.getValue();
            JFrame gameWindow = null;
            try {
                gameWindow = DisplayArea.newWindow((Shape) shape.getSelectedItem(), (int) startingCapacities.getSelectedItem(), maxCapacity, rockImage[0], paperImage[0], scissorsImage[0]);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JFrame finalGameWindow = gameWindow;
            SwingUtilities.invokeLater(() -> finalGameWindow.setVisible(true));
            SwingUtilities.invokeLater(() -> frame.setVisible(false));
        });
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    public static BufferedImage uploadPNG(JFrame frame , JLabel label) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose PNG File");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG Image", "png"));

        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            label.setText(file.getAbsolutePath());
            try {
                return ImageIO.read(file);
            } catch (IOException e) {
                return null;
            }
        } else {
            label.setText("No File Selected");
            return null;
        }
    }
}