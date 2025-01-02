import java.awt.*;

@FunctionalInterface
public interface Drawable {
    void draw(Graphics2D graphics2D, DisplayArea displayArea);
}
