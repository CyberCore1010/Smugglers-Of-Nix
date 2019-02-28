package Objects.GameObjects;

import Init.CameraID;
import Init.Game;
import Objects.GameObjects.Properties.Drawable;
import Objects.Utility.BufferedImageLoader;
import Objects.Utility.Maths.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Station extends GameObject{
    private BufferedImage sprite;

    public Station(float x, float y, float width, float height) {
        super(ObjectID.station);
        construct(x, y, width, height, "");
    }

    public Station(float x, float y, float width, float height, String name) {
        super(ObjectID.station);
        construct(x, y, width, height, name);
    }

    private void construct(float x, float y, float width, float height, String name) {
        position = new Vector2D(x, y);
        midPos = new Vector2D(x+width/2, y+height/2);
        this.width = width;
        this.height = height;

        BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
        sprite = bufferedImageLoader.loadImage("/Sprites/Stations/"+name+"Station.png");
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2d) {
        Drawable drawable = (g2) -> g2.drawImage(sprite, (int)position.x, (int)position.y, (int)width, (int)height, null);

        renderToCamera(drawable, g2d, Game.getInstance().cameraMap.get(CameraID.game));
    }
}
