import java.awt.*;

public interface Normalizer {
    NormalizedPoint normalize(Point point);
    Point denormalize(NormalizedPoint normalizedPoint);
}

record NormalizedPoint(double x, double y) {};
