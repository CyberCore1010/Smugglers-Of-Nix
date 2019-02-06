package Objects.Utility.Maths;

public class Maths {
    public static Vector2D lerp(Vector2D currentPos, Vector2D targetPos, double speedPercent) {
        double diffX = ((targetPos.x-currentPos.x)*speedPercent);
        double diffY = ((targetPos.y-currentPos.y)*speedPercent);

        return new Vector2D(diffX, diffY);
    }

    public static double clamped(double lowerBounds, double upperBounds, double value) {
        if(value > upperBounds) return upperBounds;
        else if(value < lowerBounds) return lowerBounds;
        return value;
    }
}
