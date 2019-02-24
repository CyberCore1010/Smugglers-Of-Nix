package Objects.GameObjects.Player.HUD;

import java.awt.*;

public class Map {
    private int x, y, width, height;
    private HUD hud;

    Map(int x, int y, int width, int height, HUD hud) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hud = hud;
    }

    void update() {

    }

    void draw(Graphics2D g2) {
        g2.setColor(new Color(195, 97, 31, 40));
        g2.fillOval(x, y, width, height);
        g2.setColor(hud.opaqueHudColor);
        g2.drawOval(x, y, width, height);
        g2.setColor(new Color(195, 97, 31, 60));
        int interval = width/10;
        int originalInterval = interval;
        for(int z = 0; z < 10; z++) {
            g2.drawOval(x+interval, y+interval, width-interval*2, height-interval*2);
            interval = interval+originalInterval;
        }
    }
}
