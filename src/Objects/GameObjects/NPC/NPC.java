package Objects.GameObjects.NPC;

import Objects.GameObjects.GameObject;
import Objects.GameObjects.ObjectID;

import java.awt.geom.Rectangle2D;

public abstract class NPC extends GameObject{

    public int health = 100;
    public boolean dead = false;

    public NPC(ObjectID id) {
        super(id);
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(position.x, position.y, width, height);
    }
}
