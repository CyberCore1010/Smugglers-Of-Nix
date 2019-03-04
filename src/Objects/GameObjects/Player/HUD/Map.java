package Objects.GameObjects.Player.HUD;

import Init.Game;
import Objects.GameObjects.GameObject;
import Objects.GameObjects.ObjectID;
import Objects.Utility.Maths.Vector2D;
import Objects.Utility.ObjectList;
import Objects.Utility.ObjectMap;

import java.awt.*;

public class Map {
    private int x, y, width, height;
    private Vector2D midPos;
    private HUD hud;

    private ObjectMap<Vector2D, ObjectID> blips;

    Map(int x, int y, int width, int height, HUD hud) {
        this.x = x;
        this.y = y;
        midPos = new Vector2D(x+width/2, y+height/2);
        this.width = width;
        this.height = height;
        this.hud = hud;

        blips = new ObjectMap<>();
    }

    void update() {
        int mapReach = 100;
        ObjectMap<Vector2D, ObjectID> newBlips = new ObjectMap<>();
        for(GameObject gameObject : Game.getInstance().handler) {
            try {
                double distance = (hud.player.midPos.dist(gameObject.midPos))/mapReach;
                if(distance < (width/2)-5) {
                    Vector2D position = midPos.add(new Vector2D((gameObject.midPos.x-hud.player.midPos.x)/mapReach,
                            (gameObject.midPos.y-hud.player.midPos.y)/mapReach));
                    newBlips.put(position, gameObject.id);
                }
            } catch (Exception ignored) {}
        }
        blips = newBlips;
    }

    void draw(Graphics2D g2) {
        g2.setColor(new Color(195, 97, 31, 60));
        g2.fillOval(x, y, width, height);
        g2.setColor(hud.opaqueHudColor);
        g2.drawOval(x, y, width, height);
        g2.setColor(new Color(195, 97, 31, 80));
        int interval = width/10;
        int originalInterval = interval;
        for(int z = 0; z < 10; z++) {
            g2.drawOval(x+interval, y+interval, width-interval*2, height-interval*2);
            interval = interval+originalInterval;
        }
        for(java.util.Map.Entry set : blips.entrySet()) {
            Vector2D blip = (Vector2D)set.getKey();
            switch((ObjectID)set.getValue()) {
                case player:
                    g2.setColor(hud.opaqueHudColor);
                    break;
                case civilian:
                    g2.setColor(hud.opaqueHudColor);
                    break;
                case enemy:
                    g2.setColor(Color.RED);
                    break;
                case station:
                    g2.setColor(Color.white);
                    break;
                case NA:
                    g2.setColor(Color.white);
                    break;
            }

            g2.fillRect((int)blip.x-2, (int)blip.y-2, 4, 4);
        }
    }
}
