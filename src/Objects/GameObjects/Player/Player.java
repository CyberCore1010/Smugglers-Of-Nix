package Objects.GameObjects.Player;

import Init.CameraID;
import Init.Game;
import Init.Window;
import Objects.GameObjects.Drawable;
import Objects.GameObjects.GameObject;
import Objects.GameObjects.Player.Components.*;
import Objects.GameObjects.Player.Components.Component;
import Objects.GameWorld.SystemID;
import Objects.Utility.*;
import Objects.Utility.Maths.Maths;
import Objects.Utility.Maths.Physics;
import Objects.Utility.Maths.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Player extends GameObject implements Physics {
    private Vector2D position;
    private Vector2D midPos;
    private Vector2D directionUnitVector;

    private boolean moving = true;
    private Vector2D force;
    private double mass;
    private Vector2D speed;
    private Vector2D velocity;
    private Vector2D resultantForce;

    private float width, height;
    private float rotation = 1;
    private boolean turning;

    private ObjectList<BufferedImage> playerSprites;
    private ObjectList<BufferedImage> engineSprites;
    private int spriteIndex = 0;
    private int weaponOutTime = 0;
    private boolean enginesOn = false;
    private int engineIndex = 0;
    private int engineTime = 0;

    private BufferedImage stars;

    private SFXPlayer speedUp;
    private SFXPlayer speedDown;
    private SFXPlayer thrust;
    private SFXPlayer turn;

    private SystemID currentLocation;

    private ObjectMap<ComponentID, Component> components;

    public Player(float x, float y, float width, float height) {
        position = new Vector2D(x, y);
        midPos = new Vector2D(x+(width/2), y+height/2);
        directionUnitVector = new Vector2D(0.01, 0.01);

        force = new Vector2D();
        mass = 200;
        speed = new Vector2D();
        velocity = new Vector2D();
        resultantForce = new Vector2D();

        this.width = width;
        this.height = height;

        BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
        BufferedImage spriteSheet;

        spriteSheet = bufferedImageLoader.loadImage("/Sprites/PlayerShip/spriteSheet.png");
        playerSprites = new ObjectList<>();
        int spriteX = 0;
        while(spriteX < spriteSheet.getWidth()) {
            playerSprites.add(spriteSheet.getSubimage(spriteX, 0, 500, 500));
            spriteX += 500;
        }

        spriteSheet = bufferedImageLoader.loadImage("/Sprites/PlayerShip/engineSheet.png");
        engineSprites = new ObjectList<>();
        spriteX = 0;
        while(spriteX < spriteSheet.getWidth()) {
            engineSprites.add(spriteSheet.getSubimage(spriteX, 0, 500, 500));
            spriteX += 500;
        }

        stars = bufferedImageLoader.loadImage("/stars.png");

        thrust = new SFXPlayer("res/SFX/Ship/Thrust.wav", true);
        speedUp = new SFXPlayer("res/SFX/Ship/Speed up.wav", false);
        speedDown = new SFXPlayer("res/SFX/Ship/Speed down.wav", false);
        turn = new SFXPlayer("res/SFX/Ship/Turn.wav", true);

        currentLocation = SystemID.Sol;

        components = new ObjectMap<>();
        components.put(ComponentID.weaponLeft, new Weapon(Level.basic));
        components.put(ComponentID.weaponRight, new Weapon(Level.basic));
        components.put(ComponentID.shield, new Shield(Level.basic));
        components.put(ComponentID.hull, new Hull(Level.basic));
        components.put(ComponentID.engine, new Engine(Level.basic));
        components.put(ComponentID.jumpdrive, new Jumpdrive(Level.basic));
    }

    public SystemID getCurrentLocation() {
        return currentLocation;
    }

    public void upgrade(ComponentID id, Component component) {
        components.replace(id, component);
    }

    public float[] getStat(ComponentID id) {
        return components.get(id).getStat();
    }

    @Override
    public void update() {
        Game.getInstance().cameraMap.get(CameraID.game).setX(midPos.x);
        Game.getInstance().cameraMap.get(CameraID.game).setY(midPos.y);

        followMouse();
        keyboard();
        movement();
    }

    private void followMouse() {
        //get the angles of the mousePoint and the facing point from the centre of the player
        Vector2D mousePoint = Window.getInstance().getMousePoint();

        double mouseAngle = midPos.polarAngle(mousePoint);
        double facingAngle = midPos.polarAngle(midPos.add(directionUnitVector));

        if(facingAngle > Math.PI && mouseAngle < (Math.PI/2)+(Math.PI/4)) {
            mouseAngle = mouseAngle+(Math.PI*2);
        }
        if(facingAngle < (Math.PI/2)-(Math.PI/4) && mouseAngle > Math.PI) {
            mouseAngle = mouseAngle-(Math.PI*2);
        }

        //set the rotation to the new value from the lerp function(lerp doesn't return difference anymore just the new point)
        setRotation((float)Maths.lerp(facingAngle,mouseAngle,getStat(ComponentID.engine)[1]));

        //set the direction unit vector to the new angle
        directionUnitVector = Vector2D.polar(getRotation(),1);

        if(!(Math.round(facingAngle * 10d)/10d == Math.round(mouseAngle * 10d) / 10d)) {
            if(!turning) {
                turning = true;
                turn.play();
            }
        } else {
            turning = false;
            turn.stop();
        }
    }

    private void keyboard() {
        if(KeyHandler.isKeyPressed(Keys.W)) {
            if(!enginesOn) {
                speedUp.play();
                speedDown.stop();
                thrust.stop();
            }
            if(!speedUp.getClip().isActive() && !thrust.getClip().isActive()) {
                thrust.play();
            }
            moving = true;
            enginesOn = true;
            force.set(directionUnitVector.scale(getStat(ComponentID.engine)[1]));
            //forward
        } else if(KeyHandler.isKeyPressed(Keys.S)) {
            moving = true;
            enginesOn = false;
            force.set(directionUnitVector.scale(getStat(ComponentID.engine)[1]/5).invert());
        } else {
            thrust.stop();
            speedUp.stop();
            if(enginesOn) {
                speedDown.play();
            }
            enginesOn = false;
            force.set(0, 0);
        }
    }

    private void movement() {
        if(enginesOn) {
            if(engineTime > 10) {
                engineTime = 0;
                engineIndex++;
                if(engineIndex >= engineSprites.size()) {
                    engineIndex = 0;
                }
            } else {
                engineTime++;
            }
        }

        applyForce(force);
        position = position.add(resultantForce);
        midPos = midPos.add(resultantForce);
    }

    @Override
    public void applyForce(Vector2D force) {
        Vector2D acceleration = new Vector2D(force.x/mass, force.y/mass);
        speed = speed.add(acceleration);
        velocity = velocity.add(speed);

        Vector2D dragForceVector = new Vector2D(velocity.getUnitVector().scale(-1*velocity.mag()*0.8));

        resultantForce = velocity.add(dragForceVector);
    }

    public void setRotation(float rotation){
        this.rotation = rotation;
    }

    public float getRotation(){
        return rotation;
    }

    @Override
    public void render(Graphics2D g2d) {
        Drawable drawable = (g) -> {
            g.setPaint(new TexturePaint(stars, new Rectangle2D.Double(0, 0, Window.gameWidth*2, Window.gameHeight*2)));
            g.fillRect((int)midPos.x - Window.gameWidth/2, (int)midPos.y - Window.gameHeight/2, Window.gameWidth, Window.gameHeight);

            g.setPaint(Color.red);
            g.drawString(String.valueOf(String.format("%.2f%n", getRotation())), (int)position.x, (int)position.y-30);
            g.drawString(String.valueOf(position), (int)position.x+width, (int)position.y-30);
            g.drawString(String.valueOf(Window.getInstance().getMousePoint()), (int)Window.getInstance().getMousePoint().x , (int)Window.getInstance().getMousePoint().y);

            AffineTransform newTransform = g.getTransform();
            newTransform.rotate(getRotation(), midPos.x, midPos.y);
            g.setTransform(newTransform);
            if(enginesOn) {
                g.drawImage(engineSprites.get(engineIndex), (int)position.x, (int)position.y, (int)width, (int)height, null);
            }
            g.drawImage(playerSprites.get(spriteIndex), (int)position.x, (int)position.y, (int)width, (int)height, null);
        };

        renderToCamera(drawable, g2d, Game.getInstance().cameraMap.get(CameraID.game));
    }
}
