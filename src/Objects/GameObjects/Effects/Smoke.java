package Objects.GameObjects.Effects;

import Init.CameraID;
import Init.Game;
import Objects.GameObjects.GameObject;
import Objects.GameObjects.ObjectID;
import Objects.GameObjects.Properties.Drawable;
import Objects.GameWorld.SystemID;
import Objects.Utility.BufferedImageLoader;
import Objects.Utility.Maths.Vector2D;
import Objects.Utility.ObjectList;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Smoke extends GameObject{
    private Vector2D position;
    private BufferedImage image;

    private Smoke me;

    public Smoke(double x, double y) {
        super(ObjectID.NA);
        position = new Vector2D(x, y);

        int number = (int)Math.ceil(Math.random() * 4)+1;
        image = new BufferedImageLoader().loadImage("/Sprites/Effects/Smoke/"+number+".png");

        me = this;

        SystemID id = Game.getInstance().player.getCurrentLocation();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Game.getInstance().universe.systems.get(id).entities.remove(me);
                Game.getInstance().rebuildHandler();
            }
        };
        timer.schedule(task, 1000000);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2d) {
        Drawable drawable = (g2)-> g2.drawImage(image, (int)position.x, (int)position.y, Init.Window.gameWidth/20, Init.Window.gameWidth/20, null);

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
