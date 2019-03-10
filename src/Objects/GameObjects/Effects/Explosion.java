package Objects.GameObjects.Effects;

import Init.CameraID;
import Init.Game;
import Objects.GameObjects.GameObject;
import Objects.GameObjects.ObjectID;
import Objects.GameObjects.Properties.Drawable;
import Objects.GameWorld.System;
import Objects.GameWorld.SystemID;
import Objects.Utility.BufferedImageLoader;
import Objects.Utility.Maths.Vector2D;
import Objects.Utility.ObjectList;
import Objects.Utility.SFXPlayer;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Explosion extends GameObject {
    private ObjectList<BufferedImage> sprites;
    private int spriteIndex = 0, spriteTime = 10;
    private SystemID systemID;

    public Explosion(int midX, int midY, int size, boolean large, SystemID systemID) {
        super(ObjectID.NA);
        this.position = new Vector2D(midX-size/2, midY-size/2);
        this.width = size;
        this.height = size;
        this.systemID = systemID;

        sprites = new ObjectList<>();

        BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
        BufferedImage spriteSheet = bufferedImageLoader.loadImage("/Sprites/Effects/ExplosionSpriteSheet.png");
        for(int y = 0; y < 320; y += 64) {
            for(int x = 0; x < 320; x += 64) {
                sprites.add(spriteSheet.getSubimage(x, y, 64, 64));
            }
        }

        if(large) {
            new SFXPlayer("res/SFX/Effects/Large explosion.wav", false).play();
        } else {
            new SFXPlayer("res/SFX/Effects/Small explosion.wav", false).play();
        }
    }

    @Override
    public void update() {
        if(spriteTime == 0) {
            if(spriteIndex >= sprites.size()-1) {
                Game.getInstance().universe.systems.get(systemID).removeEntity(this);
            }
            spriteTime = 10;
            spriteIndex++;
        } else {
            spriteTime--;
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        Drawable drawable = (g2)-> g2.drawImage(sprites.get(spriteIndex), (int)position.x, (int)position.y, (int)width, (int)height, null);

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
