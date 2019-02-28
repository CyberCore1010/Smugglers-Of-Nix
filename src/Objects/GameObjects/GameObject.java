package Objects.GameObjects;

import Init.Camera;
import Objects.GameObjects.Properties.Drawable;
import Objects.Utility.Maths.Vector2D;

import java.awt.*;

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

    //method for rendering the game object to a specific camera
    protected void renderToCamera(Drawable item, Graphics2D g2d, Camera camera){
        g2d.setTransform(camera.getTransform());
        item.draw(g2d);
    }
}
