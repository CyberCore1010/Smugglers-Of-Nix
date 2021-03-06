package Objects.GameObjects.Player;

import Init.Camera;
import Init.CameraID;
import Init.Game;
import Init.Window;
import Objects.GameObjects.Effects.Explosion;
import Objects.GameObjects.Effects.Projectile;
import Objects.GameObjects.Effects.Smoke;
import Objects.GameObjects.NPC.Enemy.Enemy;
import Objects.GameObjects.Player.HUD.HUD;
import Objects.GameObjects.Player.Missions.Mission;
import Objects.GameObjects.Player.Missions.Tutorial;
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
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import static Objects.GameObjects.ObjectID.station;

public class Player extends GameObject implements Physics{

    public int maxHealth;
    public int health;
    public int maxShield;
    public int shield;
    private int shieldRecharge;
    public int fuel;
    public int maxFuel;
    public int credits = 0;
    public Mission currentMission;
    public Enemy targetedEnemy;

    private boolean dead = false;
    private boolean displayShip = true;

    @SuppressWarnings("FieldCanBeLocal")
    private double mass = 200;
    private Vector2D velocity = new Vector2D();
    private Vector2D resultantForce = new Vector2D();

    private Vector2D directionUnitVector = new Vector2D(0.01, 0.01);
    private double rotation = 1;
    private double mouseAngle;
    private boolean turning;

    private ObjectList<BufferedImage> playerSprites;
    private ObjectList<BufferedImage> engineSprites;
    private int spriteIndex = 0;
    public boolean weaponOut = false;
    private int weaponOutTime = 0;
    private int weaponCooldown = 0;
    private boolean enginesOn = false;
    private int engineIndex = 0;
    private int engineTime = 0;

    private int cameraZoomTime = 0;
    private double cameraZoomValue = Double.MAX_VALUE;

    //Sounds
    private SFXPlayer speedUp = new SFXPlayer("res/SFX/Ship/Speed up.wav", false);
    private SFXPlayer speedDown = new SFXPlayer("res/SFX/Ship/Speed down.wav", false);
    private SFXPlayer thrust = new SFXPlayer("res/SFX/Ship/Thrust.wav", true);
    private SFXPlayer turn = new SFXPlayer("res/SFX/Ship/Turn.wav", true);
    private SFXPlayer jumpSound = new SFXPlayer("res/SFX/Ship/Frame shift.wav", false);
    private SFXPlayer deployWeapons = new SFXPlayer("res/SFX/Ship/Deploy weapons.wav", false);
    private SFXPlayer stowWeapons = new SFXPlayer("res/SFX/Ship/Stow weapons.wav", false);

    private SystemID currentLocation = SystemID.Sol;
    public boolean jumping = false, chargingJump = false;
    private BufferedImage jump;
    private Timer timer;

    private ObjectMap<ComponentID, Component> components;
    private HUD hud;

    public boolean canDock = true;
    private boolean docking = false;
    public boolean docked = false;

    public Player(float x, float y, float width, float height) {
        super(ObjectID.player);
        position = new Vector2D(x, y);
        midPos = new Vector2D(x+(width/2), y+height/2);

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
        currentMission = new Tutorial();

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
        statUpdate();
    }

    private void statUpdate() {
        int oldMaxHealth = maxHealth;
        maxHealth = (int)components.get(ComponentID.hull).getStat()[0];
        health += maxHealth-oldMaxHealth;

        int oldMaxShield = maxShield;
        maxShield = (int)components.get(ComponentID.shield).getStat()[0];
        shield += maxShield-oldMaxShield;
    }

    public float[] getStat(ComponentID id) {
        return components.get(id).getStat();
    }

    @Override
    public void update() {
        if(!dead) {
            updateShield();
            if(weaponCooldown > 0) weaponCooldown--;
            if(cameraZoomTime > 0) cameraZoomTime--;

            if(!docked) {
                checkDead();
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
                    input();
                    collisions(velocity);

//                    position = Game.getInstance().universe.systems.get(SystemID.Sol).entities.get(0).position.add(1, 1);
//                    midPos = Game.getInstance().universe.systems.get(SystemID.Sol).entities.get(0).midPos.add(1, 1);
                } else if(docking) {
                    followMouse();
                    dockingManeuver();
                } else {
                    followMouse();
                    applyForce(directionUnitVector.scale(getStat(ComponentID.engine)[0]));
                }
            }

            currentMission.update();
            hud.update();
        }

        movement();
        spriteUpdate();

        handleCamera();

        resultantForce.set(0, 0);
    }

    private void handleCamera() {
        Camera gameCam = Game.getInstance().cameraMap.get(CameraID.game);
        gameCam.setX(midPos.x);
        gameCam.setY(midPos.y+Window.gameHeight/25);

        if(cameraZoomValue == Double.MAX_VALUE) {
            cameraZoomValue = gameCam.getZoom();
        }

        if(gameCam.getZoom() > cameraZoomValue) {
            if(Math.abs(gameCam.getZoom() - cameraZoomValue) < 0.001) {
                gameCam.setZoom(gameCam.getZoom() - Math.abs(gameCam.getZoom() - cameraZoomValue));
            } else {
                gameCam.setZoom(gameCam.getZoom() - 0.001);
            }
        } else if(gameCam.getZoom() < cameraZoomValue) {
            if(Math.abs(gameCam.getZoom() - cameraZoomValue) < 0.001) {
                gameCam.setZoom(gameCam.getZoom() + Math.abs(gameCam.getZoom() - cameraZoomValue));
            } else {
                gameCam.setZoom(gameCam.getZoom() + 0.001);
            }
        }
    }

    private void checkDead() {
        if(health <= 0) {
            dead = true;
            deathSequence();
        }
    }

    @SuppressWarnings("Duplicates")
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

    private void input() {
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
                if(weaponOut) {
                    hud.hardpointError();
                    //todo play error sound "Docking request denied"
                } else {
                    new SFXPlayer("res/SFX/Ship/Docking request.wav", false).play();
                    canDock = false;
                    docking = true;
                }
            }
        }
        if(MouseHandler.isMouseClicked(MouseButtons.LMB)) {
            if(weaponOut) {
                if(spriteIndex == playerSprites.size()-1) {
                    if(weaponCooldown == 0) {
                        new SFXPlayer("res/SFX/Ship/Fire weapons.wav", false).play();
                        Game.getInstance().universe.systems.get(Game.getInstance().player.currentLocation).entities.add(
                                new Projectile(getStat(ComponentID.weaponLeft)[0], getLeftGunPos(), directionUnitVector.scale(20), true));
                        Game.getInstance().universe.systems.get(Game.getInstance().player.currentLocation).entities.add(
                                new Projectile(getStat(ComponentID.weaponRight)[0], getRightGunPos(), directionUnitVector.scale(20), false));
                        Game.getInstance().rebuildHandler();
                        weaponCooldown = 20;
                    }
                }
            } else {
                weaponOut = true;
                deployWeapons.play();
            }
        }
        if(MouseHandler.isMouseClicked(MouseButtons.RMB)) {
            if(weaponOut) {
                weaponOut = false;
                stowWeapons.play();
            }
        }
        if(MouseHandler.isMouseClicked(MouseButtons.MMB)) {
            if(cameraZoomTime == 0) {
                if(cameraZoomValue != 0.7)  {
                    cameraZoomValue = 0.7;
                } else {
                    cameraZoomValue = 1;
                }
                cameraZoomTime = 10;
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

    private void spriteUpdate() {
        if(weaponOut) {
            if(spriteIndex < playerSprites.size()-1) {
                if(weaponOutTime > 20) {
                    weaponOutTime = 0;
                    spriteIndex++;
                }
                weaponOutTime++;
            }
        } else {
            if(spriteIndex > 0) {
                if(weaponOutTime > 20) {
                    weaponOutTime = 0;
                    spriteIndex--;
                }
                weaponOutTime++;
            }
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

    public void takeDamage(int damage) {
        if(shield > 0) {
            shield--;
        } else {
            health -= damage;
            new SFXPlayer("res/SFX/Effects/Impact.wav", false).play();
        }
        shieldRecharge = (int)getStat(ComponentID.shield)[1];
    }

    private void updateShield() {
        if(shield < maxShield) {
            if(shieldRecharge > 0) {
                shieldRecharge--;
            } else {
                shield = maxShield;
            }
        }
    }

    private void deathSequence() {
        Game.getInstance().universe.systems.get(getCurrentLocation()).addEntity(new Explosion((int)midPos.x, (int)midPos.y, (int)width/2, false, getCurrentLocation()));

        Timer start = new Timer();
        Timer end = new Timer();
        Timer restart = new Timer();
        TimerTask startTask = new TimerTask() {
            @Override
            public void run() {
                Game.getInstance().universe.systems.get(currentLocation).addEntity(new Smoke(position.x, position.y));
                Game.getInstance().rebuildHandler();
            }
        };
        TimerTask endTask = new TimerTask() {
            @Override
            public void run() {
                Game.getInstance().universe.systems.get(getCurrentLocation()).addEntity(new Explosion((int)midPos.x, (int)midPos.y, (int)width, true, getCurrentLocation()));
                displayShip = false;
                Game.getInstance().rebuildHandler();
                start.cancel();
            }
        };
        TimerTask restartTask = new TimerTask() {
            @Override
            public void run() {
                Game.getInstance().restart = true;
            }
        };
        start.scheduleAtFixedRate(startTask, 0, 100);
        int randomTime = (int)((Math.random() * 501) + 1000);
        end.schedule(endTask, randomTime);
        restart.schedule(restartTask, randomTime+2000);
    }

    private void dockingManeuver() {
        for(GameObject object : Game.getInstance().handler) {
            if(object.id == station) {
                mouseAngle = midPos.polarAngle(object.midPos);

                if(Math.abs(rotation - mouseAngle) < 0.01) {
                    rotation = mouseAngle;
                    velocity = new Vector2D();
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

    private void setRotation(double rotation){
        this.rotation = rotation;
    }

    private double getRotation(){
        return rotation;
    }

    @Override
    public void render(Graphics2D g2d) {
        if(!docked && displayShip) {
            Drawable drawable = (g) -> {
                if(jumping) {
                    g.setPaint(new TexturePaint(jump, new Rectangle2D.Double(-position.x*5, -position.y*5, Init.Window.gameWidth*2, Init.Window.gameHeight*2)));
                    g.fillRect((int)Game.getInstance().cameraMap.get(CameraID.game).getX()-Window.gameWidth/2,
                            (int)Game.getInstance().cameraMap.get(CameraID.game).getY()-Window.gameHeight/2,
                            Init.Window.gameWidth*2, Window.gameHeight*2);
                }

                AffineTransform newTransform = g.getTransform();
                newTransform.rotate(getRotation(), midPos.x, midPos.y);
                g.setTransform(newTransform);
                if(enginesOn || chargingJump || jumping) {
                    g.drawImage(engineSprites.get(engineIndex), (int)position.x, (int)position.y, (int)width, (int)height, null);
                }
                g.drawImage(playerSprites.get(spriteIndex), (int)position.x, (int)position.y, (int)width, (int)height, null);

//                g.setColor(Color.RED);
//                g.drawRect((int)getSquareBounds().getX(), (int)getSquareBounds().getY(), (int)getSquareBounds().getWidth(), (int)getSquareBounds().getHeight());
//                for(Line2D line : getPolyBounds()) {
//                    g.drawLine((int)line.getX1(), (int)line.getY1(), (int)line.getX2(), (int)line.getY2());
//                }
            };

            renderToCamera(drawable, g2d, Game.getInstance().cameraMap.get(CameraID.game));
        }

        if(!dead) hud.render(g2d);
    }

    @Override
    public Rectangle2D getSquareBounds() {
        return new Rectangle2D.Double(position.x, position.y, width, height);
    }

    @Override
    public ObjectList<Line2D> getPolyBounds() {
        ObjectList<Line2D> returnList = new ObjectList<>();
        returnList.add(new Line2D.Double( //Back
                midPos.x-width/7, midPos.y-height/2.3, midPos.x-width/7, midPos.y+height/2.3
        ));
        returnList.add(new Line2D.Double( //Left
                midPos.x-width/7, midPos.y-height/2.3, midPos.x+width/2.5, midPos.y-height/3.4
        ));
        returnList.add(new Line2D.Double( //Right
                midPos.x-width/7, midPos.y+height/2.3, midPos.x+width/2.5, midPos.y+height/3.4
        ));
        returnList.add(new Line2D.Double( //Front
                midPos.x+width/2.5, midPos.y-height/3.4,  midPos.x+width/2.5, midPos.y+height/3.4
        ));

        return Maths.rotatePolygon(returnList, midPos, rotation);
    }
}
