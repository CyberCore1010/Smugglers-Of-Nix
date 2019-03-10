package Objects.GameObjects.Effects;

import Objects.GameObjects.GameObject;
import Objects.GameObjects.ObjectID;
import Objects.Utility.ObjectList;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class Smoke extends GameObject{

    public Smoke() {
        super(ObjectID.NA);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2d) {

    }

    @Override
    public Rectangle2D getSquareBounds() {
        return null;
    }

    @Override
    public ObjectList<Line2D> getPolyBounds() {
        return null;
    }
}
