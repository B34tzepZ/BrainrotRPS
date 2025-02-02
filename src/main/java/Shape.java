import java.awt.*;

public enum Shape {
    TRIANGLE((graphics2D, displayArea) -> {
        NormalizedPoint top = new NormalizedPoint(0.5,0);
        NormalizedPoint bottomLeft = new NormalizedPoint(0,1);
        NormalizedPoint bottomRight = new NormalizedPoint(1,1);
        Point p1 = displayArea.denormalize(top);
        Point p2 = displayArea.denormalize(bottomLeft);
        Point p3 = displayArea.denormalize(bottomRight);
        Polygon polygon = new Polygon();
        polygon.addPoint(p1.x, p1.y);
        polygon.addPoint(p2.x, p2.y);
        polygon.addPoint(p3.x, p3.y);
        graphics2D.drawPolygon(polygon);
    }),
    SQUARE((graphics2D, displayArea) -> {
        NormalizedPoint topLeft = new NormalizedPoint(0, 0);
        NormalizedPoint bottomRight = new NormalizedPoint(1, 1);
        Point p1 = displayArea.denormalize(topLeft);
        Point p2 = displayArea.denormalize(bottomRight);
        graphics2D.drawRect(p1.x, p1.y, p2.x - displayArea.getPadding(), p2.y - displayArea.getPadding());
    }),
    CIRCLE((graphics2D, displayArea) -> {
        NormalizedPoint topLeft = new NormalizedPoint(0, 0);
        NormalizedPoint bottomRight = new NormalizedPoint(1, 1);
        Point p1 = displayArea.denormalize(topLeft);
        Point p2 = displayArea.denormalize(bottomRight);
        graphics2D.drawOval(p1.x, p1.y, p2.x - displayArea.getPadding(), p2.y - displayArea.getPadding());
    });

    public final Drawable drawFunction;

    Shape(Drawable drawFunction) {
        this.drawFunction = drawFunction;
    }
}
