package Objects.GameObjects;

import Init.Camera;
import Init.Game;
import Objects.GameObjects.Properties.Drawable;
import Objects.GameObjects.Properties.Physics;
import Objects.Utility.Maths.Vector2D;
import Objects.Utility.ObjectList;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public abstract class GameObject {
    public ObjectID id;

    public Vector2D position;
    public Vector2D midPos;
    public float width, height;

    public GameObject(ObjectID id) {
        this.id = id;
    }

    public abstract void update();
    public abstract void render(Graphics2D g2d);

    public abstract Rectangle2D getSquareBounds();
    public abstract ObjectList<Line2D> getPolyBounds();

    protected void collisions(Vector2D velocity) {
        try {
            for(GameObject object : Game.getInstance().handler) {
                if(object.getSquareBounds().intersects(getSquareBounds())) {
                    for(Line2D otherLine : object.getPolyBounds()) {
                        for(Line2D myLine : getPolyBounds()) {
                            if(otherLine.intersects(myLine.getBounds())) {
                                double angle = midPos.angle(object.midPos);
                                angle += Math.PI;
                                Vector2D force = Vector2D.polar(angle, 50000);
                                Physics physics = (Physics)this;
                                physics.applyForce(force);
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { }
    }

    //method for rendering the game object to a specific camera
    protected void renderToCamera(Drawable item, Graphics2D g2d, Camera camera){
        g2d.setTransform(camera.getTransform());
        item.draw(g2d);
    }
}
