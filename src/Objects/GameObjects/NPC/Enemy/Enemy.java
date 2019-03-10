package Objects.GameObjects.NPC.Enemy;

import Init.CameraID;
import Init.Game;
import Init.Window;
import Objects.GameObjects.Effects.Projectile;
import Objects.GameObjects.GameObject;
import Objects.GameObjects.NPC.NPC;
import Objects.GameObjects.ObjectID;
import Objects.GameObjects.Player.Components.ComponentID;
import Objects.GameObjects.Player.Player;
import Objects.GameObjects.Properties.Drawable;
import Objects.GameObjects.Properties.Physics;
import Objects.GameWorld.SystemID;
import Objects.Utility.BufferedImageLoader;
import Objects.Utility.Maths.Maths;
import Objects.Utility.Maths.Vector2D;
import Objects.Utility.ObjectList;
import Objects.Utility.SFXPlayer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class Enemy extends NPC implements Physics{
    public Vector2D directionUnitVector;

    private BufferedImage sprite;

    private boolean turning;

    private SFXPlayer turn;

    private double mass = 200;
    private Vector2D velocity = new Vector2D();
    private Vector2D resultantForce = new Vector2D();

    private int weaponCooldown = 0;

    public Enemy(SystemID systemID) {
        super(ObjectID.enemy);

        this.systemID = systemID;

        width = Window.gameWidth/15;
        height = Window.gameWidth/15;
        int x = (int)((Math.random() * 10001));
        if(Math.round(Math.random()) == 1) {
            x *= -1;
        }
        int y = (int)((Math.random() * 10001));
        if(Math.round(Math.random()) == 1) {
            y *= -1;
        }
        position = new Vector2D(x, y);
        midPos = new Vector2D(position.x+width/2, position.y+height/2);

        directionUnitVector = new Vector2D(0.01, 0.01);

        turn = new SFXPlayer("res/SFX/Ship/Turn.wav", true);

        BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
        BufferedImage spriteSheet;

        spriteSheet = bufferedImageLoader.loadImage("/Sprites/EnemyShip/ShipSheet.png");
        sprite = spriteSheet.getSubimage(0, 0, 64, 64);
    }

    @Override
    public void update() {
        resultantForce = resultantForce.set(0, 0);
        if(!dead) {
            if(health <= 0) {
                dead = true;
                deathSequence();
            }
            if(detectPlayer()) {
                rotateToPlayer();
                followPlayer();
                shoot();
            }
        }
        movement();
    }

    private boolean detectPlayer() {
        for(GameObject object : Game.getInstance().handler) {
            if(object.id == ObjectID.player) {
                if(Math.abs(midPos.x-object.midPos.x) < Window.gameWidth*2 && Math.abs(midPos.y-object.midPos.y) < Window.gameWidth*2) {
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("Duplicates")
    private void rotateToPlayer() {
        Player player = Game.getInstance().player;
        double angle = midPos.angle(player.midPos);
        if(health < 20) {
            angle += Math.PI;
        }

        double facingAngle = midPos.polarAngle(midPos.add(directionUnitVector));

        if(facingAngle > Math.PI && angle < (Math.PI/2)+(Math.PI/4)) {
            angle = angle+(Math.PI*2);
        }
        if(facingAngle < (Math.PI/2)-(Math.PI/4) && angle > Math.PI) {
            angle = angle-(Math.PI*2);
        }

        //set the rotation to the new value from the lerp function
        setRotation((float)Maths.lerp(facingAngle,angle,player.getStat(ComponentID.engine)[1]));

        //set the direction unit vector to the new angle
        directionUnitVector = Vector2D.polar(getRotation(),1);

        if(!(Math.round(facingAngle * 10d)/10d == Math.round(angle * 10d) / 10d)) {
            if(!turning) {
                turning = true;
                turn.play();
            }
        } else {
            turning = false;
            turn.stop();
        }
    }

    private void followPlayer() {
        applyForce(directionUnitVector.scale(10));
    }

    @Override
    public void applyForce(Vector2D force) {
        resultantForce = resultantForce.add(force);
    }

    private void movement() {
        Vector2D dragForceVector = new Vector2D(velocity.getUnitVector().scale(-1*velocity.mag()*0.9));
        applyForce(dragForceVector);
        Vector2D acceleration = new Vector2D(resultantForce.x/mass,resultantForce.y/mass);
        velocity = velocity.add(acceleration);
        position = position.add(velocity);
        midPos = midPos.add(velocity);
    }

    private void shoot() {
        if(weaponCooldown == 0) {
            new SFXPlayer("res/SFX/Ship/Fire weapons.wav", false).play();
            Game.getInstance().universe.systems.get(systemID).entities.add(
                    new Projectile(Game.getInstance().player.getStat(ComponentID.weaponLeft)[0], getLeftGunPos(), directionUnitVector.scale(20), this, true));
            Game.getInstance().universe.systems.get(systemID).entities.add(
                    new Projectile(Game.getInstance().player.getStat(ComponentID.weaponRight)[0], getRightGunPos(), directionUnitVector.scale(20), this, false));
            Game.getInstance().rebuildHandler();
            weaponCooldown = 20;
        } else {
            weaponCooldown--;
        }
    }

    public Vector2D getLeftGunPos() {
        Vector2D offset = Vector2D.polar(directionUnitVector.angle()-0.5, width/3);

        return midPos.add(offset);
    }

    public Vector2D getRightGunPos() {
        Vector2D offset = Vector2D.polar(directionUnitVector.angle()+0.5, width/3);

        return midPos.add(offset);
    }

    @Override
    public void render(Graphics2D g2d) {
        Drawable drawable = (g2)->{
            AffineTransform newTransform = g2.getTransform();
            newTransform.rotate(getRotation(), midPos.x, midPos.y);
            g2.setTransform(newTransform);
            g2.drawImage(sprite, (int)position.x, (int)position.y, (int)width, (int)height, null);

//            g2.setColor(Color.white);
//            g2.drawRect((int)getSquareBounds().getX(), (int)getSquareBounds().getY(), (int)getSquareBounds().getWidth(), (int)getSquareBounds().getHeight());
//            for(Line2D line : getPolyBounds()) {
//                g2.drawLine((int)line.getX1(), (int)line.getY1(), (int)line.getX2(), (int)line.getY2());
//            }
        };

        renderToCamera(drawable, g2d, Game.getInstance().cameraMap.get(CameraID.game));
    }

    @Override
    public ObjectList<Line2D> getPolyBounds() {
        ObjectList<Line2D> returnList = new ObjectList<>();
        returnList.add(new Line2D.Double( //Back
                midPos.x-width/5, midPos.y+height/3, midPos.x-width/5, midPos.y-height/3
        ));
        returnList.add(new Line2D.Double( //Left
                midPos.x-width/5, midPos.y-height/3, midPos.x+width/5, midPos.y-height/3
        ));
        returnList.add(new Line2D.Double( //Right
                midPos.x-width/5, midPos.y+height/3, midPos.x+width/5, midPos.y+height/3
        ));
        returnList.add(new Line2D.Double( //Left cone
                midPos.x+width/5, midPos.y-height/3, midPos.x+width/3, midPos.y
        ));
        returnList.add(new Line2D.Double( //Right cone
                midPos.x+width/5, midPos.y+height/3, midPos.x+width/3, midPos.y
        ));

        for(Line2D line : returnList) {
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
        return returnList;
    }
}
