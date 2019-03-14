package Objects.GameObjects.NPC;

import Init.Game;
import Objects.GameObjects.Effects.Explosion;
import Objects.GameObjects.Effects.Smoke;
import Objects.GameObjects.GameObject;
import Objects.GameObjects.ObjectID;
import Objects.GameWorld.SystemID;
import Objects.Utility.SFXPlayer;

import java.awt.geom.Rectangle2D;
import java.util.Timer;
import java.util.TimerTask;

public abstract class NPC extends GameObject{

    public int health = 100;
    public int maxShield = 10;
    public int shield = 10;
    private int shieldRecharge;
    public boolean dead = false;

    protected double rotation = 0;

    protected SystemID systemID;

    public NPC(ObjectID id) {
        super(id);
    }

    public void takeDamage(int damage) {
        if(shield > 0) {
            shield--;
        } else {
            health -= damage;
            new SFXPlayer("res/SFX/Effects/Impact.wav", false).play();
        }
        shieldRecharge = 250;
    }

    protected void updateShield() {
        if(shield < maxShield) {
            if(shieldRecharge > 0) {
                shieldRecharge--;
            } else {
                shield = maxShield;
            }
        }
    }

    protected void deathSequence() {
        Game.getInstance().universe.systems.get(systemID).addEntity(new Explosion((int)midPos.x, (int)midPos.y, (int)width/2, false, systemID));
        NPC npc = this;

        Timer start = new Timer();
        Timer end = new Timer();
        TimerTask startTask = new TimerTask() {
            @Override
            public void run() {
                Game.getInstance().universe.systems.get(systemID).addEntity(new Smoke(position.x, position.y));
                Game.getInstance().rebuildHandler();
            }
        };
        TimerTask endTask = new TimerTask() {
            @Override
            public void run() {
                Game.getInstance().universe.systems.get(systemID).addEntity(new Explosion((int)midPos.x, (int)midPos.y, (int)width, true, systemID));
                Game.getInstance().universe.systems.get(systemID).removeEntity(npc);
                start.cancel();
            }
        };

        start.scheduleAtFixedRate(startTask, 0, 200);
        end.schedule(endTask, (int)((Math.random() * 501) + 1000));
    }

    @Override
    public Rectangle2D getSquareBounds() {
        return new Rectangle2D.Double(position.x, position.y, width, height);
    }

    public void setRotation(double rotation){
        this.rotation = rotation;
    }

    public double getRotation(){
        return rotation;
    }
}
