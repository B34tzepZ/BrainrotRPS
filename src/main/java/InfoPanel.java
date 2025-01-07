import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InfoPanel extends JPanel implements GameListener {
    private int rockCapacity;
    private int paperCapacity;
    private int scissorsCapacity;
    private final int maxCapacity;
    private final JLabel rock;
    private final JLabel paper;
    private final JLabel scissors;
    private boolean invert;
    private int gameSpeed;
    private int refillSpeed;

    InfoPanel(final int width, final int height, final int startingCapacity, final int maxCapacity) {
        super();
        this.setPreferredSize(new Dimension(width, height));
        this.rockCapacity = startingCapacity;
        this.paperCapacity = startingCapacity;
        this.scissorsCapacity = startingCapacity;
        this.maxCapacity = maxCapacity;
        this.rock = new JLabel(Tool.ROCK.asLabelContent(rockCapacity, maxCapacity));
        this.paper = new JLabel(Tool.PAPER.asLabelContent(rockCapacity, maxCapacity));
        this.scissors = new JLabel(Tool.SCISSORS.asLabelContent(rockCapacity, maxCapacity));
        this.invert = false;
        this.gameSpeed = 1;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(rock);
        this.add(paper);
        this.add(scissors);
        final JCheckBox invertControls = new JCheckBox("Invert Controls");
        this.add(invertControls);
        invertControls.addActionListener(e ->  {
            if (invertControls.isSelected()) {
                invert = true;
            } else {
                invert = false;
            }
        });

        final JLabel gameSpeedLabel= new JLabel("Game Speed");
        this.add(gameSpeedLabel);
        final Integer[] gameSpeedOptions = {0, 1, 2, 3};
        final JComboBox<Integer> gameSpeedBox = new JComboBox<>(gameSpeedOptions);
        this.add(gameSpeedBox);
        gameSpeedBox.addActionListener(e -> {
            gameSpeed = gameSpeedBox.getSelectedIndex();
        });

        final JLabel refillSpeedLabel = new JLabel("Refill Speed");
        this.add(refillSpeedLabel);
        final Integer[] refillSpeedOptions = {0, 1, 2, 3};
        final JComboBox<Integer> refillSpeedBox = new JComboBox<>(refillSpeedOptions);
        this.add(refillSpeedBox);
        refillSpeedBox.addActionListener(e -> {
            refillSpeed = refillSpeedBox.getSelectedIndex();
        });

        Timer timer = new Timer(10000, e -> {
            rockCapacity += refillSpeed;
            if (rockCapacity > maxCapacity) {
                rockCapacity = maxCapacity;
            }
            paperCapacity += refillSpeed;
            if (paperCapacity > maxCapacity) {
                paperCapacity = maxCapacity;
            }
            scissorsCapacity += refillSpeed;
            if (scissorsCapacity > maxCapacity) {
                scissorsCapacity = maxCapacity;
            }
            rock.setText(Tool.ROCK.asLabelContent(rockCapacity, maxCapacity));
            paper.setText(Tool.PAPER.asLabelContent(rockCapacity, maxCapacity));
            scissors.setText(Tool.SCISSORS.asLabelContent(rockCapacity, maxCapacity));
        });

        timer.setRepeats(true);
        timer.start();

        this.setBackground(Color.gray);
        this.setBorder(new EmptyBorder(0, 10, 0, 0));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        final Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(0, 10, 0, 0));
    }

    @Override
    public void onAdded(Tool tool) {
        SwingUtilities.invokeLater(() -> {
            switch (tool) {
                case ROCK -> rock.setText(tool.asLabelContent(--rockCapacity, maxCapacity));
                case PAPER -> paper.setText(tool.asLabelContent(--paperCapacity, maxCapacity));
                case SCISSORS -> scissors.setText(tool.asLabelContent(--scissorsCapacity, maxCapacity));
            }
        });
    }

    @Override
    public boolean hasCapacity(Tool tool) {
        switch (tool) {
            case ROCK -> {
                return rockCapacity > 0;
            }
            case PAPER -> {
                return paperCapacity > 0;
            }
            case SCISSORS -> {
                return scissorsCapacity > 0;
            }
        }
        return false;
    }

    @Override
    public boolean invertedControls() {
        return invert;
    }

    @Override
    public int gameSpeed() {
        return gameSpeed;
    }
}
