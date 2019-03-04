package Objects.GameObjects.Player;

import Init.CameraID;
import Init.Game;
import Init.Window;
import Objects.GameObjects.Player.HUD.HUD;
import Objects.GameObjects.Properties.Drawable;
import Objects.GameObjects.GameObject;
import Objects.GameObjects.ObjectID;
import Objects.GameObjects.Properties.Physics;
import Objects.GameObjects.Player.Components.*;
import Objects.GameObjects.Player.Components.Component;
import Objects.GameWorld.SystemID;
import Objects.Utility.*;
import Objects.Utility.Maths.Maths;
import Objects.Utility.Maths.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import static Objects.GameObjects.ObjectID.station;

public class Player extends GameObject implements Physics{
    public Vector2D directionUnitVector;

    public int maxHealth;
    public int health;
    public int maxShield;
    public int shield;
    public int fuel;
    public int maxFuel;
    public int credits = 1000;

    private double mass;
    private Vector2D velocity;
    private Vector2D resultantForce;

    private double rotation = 1;
    private double mouseAngle;
    private boolean turning;

    private ObjectList<BufferedImage> playerSprites;
    private ObjectList<BufferedImage> engineSprites;
    private int spriteIndex = 0;
    private int weaponOutTime = 0;
    private boolean enginesOn = false;
    private int engineIndex = 0;
    private int engineTime = 0;

    private SFXPlayer speedUp;
    private SFXPlayer speedDown;
    private SFXPlayer thrust;
    private SFXPlayer turn;
    public SFXPlayer jumpSound;

    private SystemID currentLocation;
    public boolean jumping, chargingJump;
    private BufferedImage jump;
    public Timer timer;

    private ObjectMap<ComponentID, Component> components;
    private HUD hud;

    public boolean canDock = true;
    private boolean docking = false;
    public boolean docked = false;

    public Player(float x, float y, float width, float height) {
        super(ObjectID.player);
        position = new Vector2D(x, y);
        midPos = new Vector2D(x+(width/2), y+height/2);
        directionUnitVector = new Vector2D(0.01, 0.01);
        mass = 200;
        velocity = new Vector2D();
        resultantForce = new Vector2D(0,0);

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

        thrust = new SFXPlayer("res/SFX/Ship/Thrust.wav", true);
        speedUp = new SFXPlayer("res/SFX/Ship/Speed up.wav", false);
        speedDown = new SFXPlayer("res/SFX/Ship/Speed down.wav", false);
        turn = new SFXPlayer("res/SFX/Ship/Turn.wav", true);
        jumpSound = new SFXPlayer("res/SFX/Ship/Frame shift.wav", false);

        currentLocation = SystemID.Sol;
        chargingJump = false;
        jumping = false;
        jump = bufferedImageLoader.loadImage("/Sprites/PlayerShip/jump.png");

        components = new ObjectMap<>();
        components.put(ComponentID.weaponLeft, new Weapon(Level.basic));
        components.put(ComponentID.weaponRight, new Weapon(Level.basic));
        components.put(ComponentID.shield, new Shield(Level.basic));
        components.put(ComponentID.hull, new Hull(Level.basic));
        components.put(ComponentID.engine, new Engine(Level.basic));
        components.put(ComponentID.jumpdrive, new Jumpdrive(Level.basic));

        maxHealth = (int)components.get(ComponentID.hull).getStat()[0];
        health = maxHealth;
        maxShield = (int)components.get(ComponentID.shield).getStat()[0];
        shield = maxShield;
        fuel = 100;
        maxFuel = fuel;

        hud = new HUD(this);
    }

    public SystemID getCurrentLocation() {
        return currentLocation;
    }

    public void jumpTo(SystemID location) {
        canDock = false;
        chargingJump = true;
        jumpSound.play();
        timer = new Timer();
        TimerTask start = new TimerTask() {
            @Override
            public void run() {
                chargingJump = false;
                jumping = true;
            }
        };
        TimerTask end = new TimerTask() {
            @Override
            public void run() {
                currentLocation = location;
                Game.getInstance().rebuildHandler();
                jumping = false;
                position.set(0, 0);
                midPos.set(0+width/2, 0+height/2);
            }
        };
        timer.schedule(start, 20115);
        timer.schedule(end, 33553);
    }

    public void cancelJump() {
        chargingJump = false;
        jumpSound.stop();
        timer.cancel();
    }

    public void upgrade(ComponentID id, Component component) {
        components.replace(id, component);
    }

    public float[] getStat(ComponentID id) {
        return components.get(id).getStat();
    }

    @Override
    public void update() {
        resultantForce.set(0, 0);

        if(!docked) {
            if(!jumping && !chargingJump && !docking) {
                for(GameObject object : Game.getInstance().handler) {
                    if(object.id == ObjectID.station) {
                        canDock = Math.abs(midPos.x - object.midPos.x) < object.width/2 &&
                                Math.abs(midPos.y - object.midPos.y) < object.height/2;
                    }
                }
                Vector2D mousePoint = Window.getInstance().getMousePoint();
                mouseAngle = midPos.polarAngle(mousePoint);
                followMouse();
                keyboard();
            } else if(docking) {
                followMouse();
                dockingManeuver();
            } else {
                followMouse();
                applyForce(directionUnitVector.scale(getStat(ComponentID.engine)[0]));
            }
            movement();
        }

        Game.getInstance().cameraMap.get(CameraID.game).setX(midPos.x);
        Game.getInstance().cameraMap.get(CameraID.game).setY(midPos.y);

        hud.update();
    }

    private void followMouse() {
        double facingAngle = midPos.polarAngle(midPos.add(directionUnitVector));

        if(facingAngle > Math.PI && mouseAngle < (Math.PI/2)+(Math.PI/4)) {
            mouseAngle = mouseAngle+(Math.PI*2);
        }
        if(facingAngle < (Math.PI/2)-(Math.PI/4) && mouseAngle > Math.PI) {
            mouseAngle = mouseAngle-(Math.PI*2);
        }

        //set the rotation to the new value from the lerp function
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

            enginesOn = true;
            applyForce(directionUnitVector.scale(getStat(ComponentID.engine)[0]));
        } else if(KeyHandler.isKeyPressed(Keys.S)) {
            enginesOn = false;
            thrust.play();
            applyForce(directionUnitVector.scale(getStat(ComponentID.engine)[0]/5).invert());
        } else {
            thrust.stop();
            speedUp.stop();
            if(enginesOn) {
                speedDown.play();
            }
            enginesOn = false;
        }

        if(canDock) {
            if(KeyHandler.isKeyPressed(Keys.home)) {
                canDock = false;
                docking = true;
            }
        }
    }

    private void movement() {
        if(enginesOn || chargingJump || jumping) {
            if(engineTime > 10) {
                engineTime = 0;
                engineIndex++;
                if(engineIndex >= engineSprites.size()-1) {
                    engineIndex -= engineSprites.size()-1;
                }
            } else {
                engineTime++;
            }
        }
        
        Vector2D dragForceVector = new Vector2D(velocity.getUnitVector().scale(-1*velocity.mag()*0.9));
        applyForce(dragForceVector);
        Vector2D acceleration = new Vector2D(resultantForce.x/mass,resultantForce.y/mass);
        velocity = velocity.add(acceleration);
        position = position.add(velocity);
        midPos = midPos.add(velocity);
    }

    private void dockingManeuver() {
        for(GameObject object : Game.getInstance().handler) {
            if(object.id == station) {
                mouseAngle = midPos.polarAngle(object.midPos);

                if(Math.abs(rotation - mouseAngle) < 0.01) {
                    rotation = mouseAngle;
                    if(Math.abs(midPos.x - object.midPos.x) < 10 && Math.abs(midPos.y - object.midPos.y) < 10) {
                        enginesOn = false;
                        thrust.stop();
                        midPos = object.midPos;
                        position.set(midPos.x-width/2, midPos.y-height/2);

                        docking = false;
                        docked = true;
                    } else {
                        if(!thrust.getClip().isActive()) {
                            thrust.play();
                        }
                        enginesOn = true;
                        position = position.add(directionUnitVector.scale(2));
                        midPos = midPos.add(directionUnitVector.scale(2));
                    }
                }
            }
        }
    }

    @Override
    public void applyForce(Vector2D force) {
        resultantForce = resultantForce.add(force);
    }

    public void setRotation(double rotation){
        this.rotation = rotation;
    }

    public double getRotation(){
        return rotation;
    }

    @Override
    public void render(Graphics2D g2d) {
        if(!docked) {
            Drawable drawable = (g) -> {
                if(jumping) {
                    g.setPaint(new TexturePaint(jump, new Rectangle2D.Double(-position.x*5, -position.y*5, Init.Window.gameWidth*2, Init.Window.gameHeight*2)));
                    g.fillRect((int)Game.getInstance().cameraMap.get(CameraID.game).getX(), (int)Game.getInstance().cameraMap.get(CameraID.game).getY(), Init.Window.gameWidth, Window.gameHeight);
                }

                AffineTransform newTransform = g.getTransform();
                newTransform.rotate(getRotation(), midPos.x, midPos.y);
                g.setTransform(newTransform);
                if(enginesOn || chargingJump || jumping) {
                    g.drawImage(engineSprites.get(engineIndex), (int)position.x, (int)position.y, (int)width, (int)height, null);
                }
                g.drawImage(playerSprites.get(spriteIndex), (int)position.x, (int)position.y, (int)width, (int)height, null);
            };

            renderToCamera(drawable, g2d, Game.getInstance().cameraMap.get(CameraID.game));
        }

        hud.render(g2d);
    }
}
