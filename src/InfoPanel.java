import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InfoPanel extends JPanel implements GameListener{
    private int rockCapacity;
    private int paperCapacity;
    private int scissorsCapacity;
    private final int maxCapacity;
    private final JLabel rock;
    private final JLabel paper;
    private final JLabel scissors;

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
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(rock);
        this.add(paper);
        this.add(scissors);
        this.setBorder(new EmptyBorder(0,10,0,0));
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
}
