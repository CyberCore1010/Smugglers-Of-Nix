package Objects.Utility.Maths;

import Objects.Utility.ObjectList;

import java.awt.geom.Line2D;

public class Maths {
    public static Vector2D lerp(Vector2D currentPos, Vector2D targetPos, double speedPercent) {
        double diffX = ((targetPos.x-currentPos.x)*speedPercent);
        double diffY = ((targetPos.y-currentPos.y)*speedPercent);

        return new Vector2D(currentPos.x+diffX, currentPos.y+diffY);
    }

    public static double lerp(double currentValue, double targetValue, double speedPercent) {
        return currentValue+((targetValue-currentValue)*speedPercent);
    }

    public static double clamped(double lowerBounds, double upperBounds, double value) {
        if(value > upperBounds) return upperBounds;
        else if(value < lowerBounds) return lowerBounds;
        return value;
    }

    public static ObjectList<Line2D> rotatePolygon(ObjectList<Line2D> list, Vector2D midPos, double rotation) {
        for(Line2D line : list) {
            Vector2D v1 = new Vector2D(line.getX1(), line.getY1());
            v1.subtract(midPos);
            v1.rotate(rotation);
            v1.add(midPos);
            Vector2D v2 = new Vector2D(line.getX2(), line.getY2());
            v2.subtract(midPos);
            v2.rotate(rotation);
            v2.add(midPos);
            line.setLine(v1.x, v1.y, v2.x, v2.y);
        }
        return list;
    }

    public static int randomInt(int min, int max) {
        boolean negativeMin = false;
        if(min < 0) {
            negativeMin = true;
            min = 0;
        }
        double range = (max - min);
        int value = (int)Math.round((Math.random() * range) + min);
        if(negativeMin) {
            if(randomInt(0, 1) == 0) {
                value = -value;
            }
        }
        return value;
    }
}
