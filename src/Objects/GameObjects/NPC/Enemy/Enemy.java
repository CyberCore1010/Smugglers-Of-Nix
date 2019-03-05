package Objects.GameObjects.NPC.Enemy;

import Init.CameraID;
import Init.Game;
import Init.Window;
import Objects.GameObjects.NPC.NPC;
import Objects.GameObjects.ObjectID;
import Objects.GameObjects.Player.Components.ComponentID;
import Objects.GameObjects.Player.Player;
import Objects.GameObjects.Properties.Drawable;
import Objects.GameWorld.SystemID;
import Objects.Utility.BufferedImageLoader;
import Objects.Utility.Maths.Maths;
import Objects.Utility.Maths.Vector2D;
import Objects.Utility.SFXPlayer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Enemy extends NPC{
    public Vector2D directionUnitVector;

    private BufferedImage sprite;

    private double rotation = 1;
    private boolean turning;

    private SFXPlayer turn;

    private SystemID systemID;

    public Enemy(SystemID systemID) {
        super(ObjectID.enemy);

        this.systemID = systemID;

        width = Window.gameWidth/20;
        height = Window.gameWidth/20;
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

        spriteSheet = bufferedImageLoader.loadImage("/Sprites/PlayerShip/spriteSheet.png");
        sprite = spriteSheet.getSubimage(0, 0, 500, 500);
    }

    @Override
    public void update() {
        if(!dead) {
            if(health <= 0) {
                dead = true;
                Game.getInstance().universe.systems.get(systemID).removeEntity(this);
            }
            rotateToPlayer();
        }
    }

    @SuppressWarnings("Duplicates")
    private void rotateToPlayer() {
        Player player = Game.getInstance().player;
        double angle = midPos.angle(player.midPos);

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

    public void setRotation(double rotation){
        this.rotation = rotation;
    }

    public double getRotation(){
        return rotation;
    }

    @Override
    public void render(Graphics2D g2d) {
        Drawable drawable = (g2)->{
            AffineTransform newTransform = g2.getTransform();
            newTransform.rotate(getRotation(), midPos.x, midPos.y);
            g2.setTransform(newTransform);
            g2.drawImage(sprite, (int)position.x, (int)position.y, (int)width, (int)height, null);
        };

        renderToCamera(drawable, g2d, Game.getInstance().cameraMap.get(CameraID.game));
    }
}
