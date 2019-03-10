package Objects.GameObjects.Effects;

import Init.CameraID;
import Init.Game;
import Init.Window;
import Objects.GameObjects.GameObject;
import Objects.GameObjects.NPC.Enemy.Enemy;
import Objects.GameObjects.ObjectID;
import Objects.GameObjects.Player.Player;
import Objects.GameObjects.Properties.Drawable;
import Objects.GameWorld.SystemID;
import Objects.Utility.Maths.Vector2D;
import Objects.Utility.ObjectList;
import Objects.Utility.SFXPlayer;
import javafx.util.Pair;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Timer;
import java.util.TimerTask;


public class Projectile extends GameObject {
    private float damage;
    private Vector2D frontPosition, backPosition, direction;
    private boolean player, left;

    private SystemID id;
    private Projectile me;

    private Enemy enemy;

    public Projectile(float damage, Vector2D position, Vector2D direction, boolean left) {
        super(ObjectID.NA);
        this.damage = damage;
        this.frontPosition = position;
        this.backPosition = position;
        this.direction = direction;
        this.player = true;
        this.left = left;
        id = Game.getInstance().player.getCurrentLocation();

        me = this;

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Game.getInstance().universe.systems.get(id).entities.remove(me);
                Game.getInstance().rebuildHandler();
            }
        };
        timer.schedule(task, 10000);
    }

    public Projectile(float damage, Vector2D position, Vector2D direction, Enemy enemy, boolean left) {
        super(ObjectID.NA);
        this.damage = damage;
        this.frontPosition = position;
        this.backPosition = position;
        this.direction = direction;
        this.player = false;
        this.left = left;
        id = Game.getInstance().player.getCurrentLocation();

        this.enemy = enemy;
        me = this;

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Game.getInstance().universe.systems.get(id).entities.remove(me);
                Game.getInstance().rebuildHandler();
            }
        };
        timer.schedule(task, 10000);
    }

    @Override
    public void update() {
        frontPosition = frontPosition.add(direction);
        if(frontPosition.dist(backPosition) >= Window.gameWidth/20) {
            backPosition = backPosition.add(direction);
        } else {
            if(player) {
                if(left) {
                    backPosition = Game.getInstance().player.getLeftGunPos();
                } else {
                    backPosition = Game.getInstance().player.getRightGunPos();
                }
            } else {
                if(left) {
                    backPosition = enemy.getLeftGunPos();
                } else {
                    backPosition = enemy.getRightGunPos();
                }
            }
        }

        for(GameObject object : Game.getInstance().handler) {
            Line2D line = new Line2D.Double(frontPosition.x, frontPosition.y, backPosition.x, backPosition.y);
            if(player) {
                if(object.id == ObjectID.enemy) {
                    if(object.getSquareBounds().intersects(line.getBounds2D())) {
                        for(Line2D polyLine : object.getPolyBounds()) {
                            if(polyLine.intersects(line.getBounds())) {
                                Enemy enemy = (Enemy)object;
                                enemy.takeDamage((int)damage);
                                Game.getInstance().universe.systems.get(id).entities.remove(me);
                                Game.getInstance().rebuildHandler();
                            }
                        }
                    }
                }
            } else {
//                if(object.id == ObjectID.player) {
//                    if(object.getSquareBounds().intersects(line.getBounds2D())) {
//                        for(Line2D polyLine : object.getPolyBounds()) {
//                            if(polyLine.intersects(line.getBounds())) {
//                                Player player = (Player) object;
//                                player.takeDamage((int)damage);
//                                Game.getInstance().universe.systems.get(id).entities.remove(me);
//                                Game.getInstance().rebuildHandler();
//                            }
//                        }
//                    }
//                }
            }
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        Drawable drawable = (g2)->{
            g2.setStroke(new BasicStroke(5));
            g2.setColor(Color.red);
            g2.drawLine((int)frontPosition.x, (int)frontPosition.y, (int)backPosition.x, (int)backPosition.y);
        };

        renderToCamera(drawable, g2d, Game.getInstance().cameraMap.get(CameraID.game));
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
