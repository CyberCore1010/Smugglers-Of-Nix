package Objects.GameObjects.Player;

import Init.CameraID;
import Init.Game;
import Objects.GameObjects.Drawable;
import Objects.GameObjects.GameObject;
import Objects.GameObjects.Player.Components.*;
import Objects.GameObjects.Player.Components.Component;
import Objects.GameWorld.SystemID;
import Objects.Utility.BufferedImageLoader;
import Objects.Utility.ObjectList;
import Objects.Utility.ObjectMap;
import Objects.Utility.Vector2D;
import javafx.util.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends GameObject {
    private Vector2D position;
    private Vector2D midPos;
    private float width, height;

    private BufferedImageLoader bufferedImageLoader;
    private ObjectList<BufferedImage> sprites;
    private int spriteIndex = 0;
    private int weaponOutTime = 0;

    private SystemID currentLocation;

    private ObjectMap<ComponentID, Component> components;

    public Player(float x, float y, float width, float height) {
        position = new Vector2D(x, y);
        midPos = new Vector2D(x+(width/2), y+height/2);
        this.width = width;
        this.height = height;

        bufferedImageLoader = new BufferedImageLoader();
        BufferedImage spriteSheet = bufferedImageLoader.loadImage("/PlayerShip/spriteSheet.png");
        sprites = new ObjectList<>();
        int spriteX = 0;
        while(spriteX < spriteSheet.getWidth()) {
            sprites.add(spriteSheet.getSubimage(spriteX, 0, 500, 500));
            spriteX += 500;
        }

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

    public float getStat(ComponentID id) {
        return components.get(id).getStat();
    }

    @Override
    public void update() {
        Game.getInstance().cameraMap.get(CameraID.game).setX(midPos.x);
        Game.getInstance().cameraMap.get(CameraID.game).setY(midPos.y);
        if(weaponOutTime < 20) {
            weaponOutTime++;
        } else {
            weaponOutTime = 0;
            spriteIndex = spriteIndex+1;
            if(spriteIndex >= sprites.size()-1) {
                if(spriteIndex == sprites.size()) {
                    spriteIndex = sprites.size()-2;
                } else {
                    spriteIndex = sprites.size()-1;
                }
            }
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        Drawable drawable = (g) -> g.drawImage(sprites.get(spriteIndex), (int)position.x, (int)position.y, (int)width, (int)height, null);

        renderToCamera(drawable, g2d, Game.getInstance().cameraMap.get(CameraID.game));
    }
}
