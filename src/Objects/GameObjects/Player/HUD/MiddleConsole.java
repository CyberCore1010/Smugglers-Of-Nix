package Objects.GameObjects.Player.HUD;

import Init.Window;
import Objects.GameObjects.Properties.Drawable;
import Objects.Utility.Maths.Maths;

import java.awt.*;

class MiddleConsole extends Console{
    private Map map;
    MiddleConsole(HUD hud) {
        super(hud);
        x = -Window.gameWidth/4-Window.gameWidth/30;
        y = (Window.gameHeight/2)-Window.gameHeight/10-40;
        width = Window.gameWidth/2+Window.gameWidth/15;
        height = Window.gameHeight/10;

        map = new Map(-Window.gameWidth/20, y-((Window.gameWidth/10)/2), Window.gameWidth/10, Window.gameWidth/10, hud);
    }

    @Override
    void update() {
        map.update();
    }

    @Override
    Drawable getGraphics() {
        return (g2)->{
            g2.setColor(hud.darkHudColor);
            g2.fillRect(x, y, width, height);

            g2.setColor(hud.opaqueHudColor);
            g2.drawRect(x, y, width, height);

            Composite old = g2.getComposite();
            g2.setComposite(AlphaComposite.Src);

            map.draw(g2);

            float healthChunk = (width/5f)/hud.player.maxHealth;
            g2.fillRect(x+width/2+width/10, y+y/28, (int)(healthChunk*hud.player.maxHealth), height/3);
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(x+width/2+width/10, y+y/28, (int)(healthChunk*hud.player.health), height/3);
            g2.setColor(hud.opaqueHudColor);
            g2.drawRect(x+width/2+width/10, y+y/28, (int)(healthChunk*hud.player.maxHealth), height/3);
            g2.setColor(hud.textColor);
            g2.drawString("Hull integrity", x+width/2+width/7+width/100, y+y/10);

            float fuelChunk = (width/5f)/hud.player.maxFuel;
            g2.fillRect(x+width/2+width/10, y+height/2+height/15, (int)(fuelChunk*hud.player.maxFuel), height/3);
            g2.setColor(Color.BLUE);
            g2.fillRect(x+width/2+width/10, y+height/2+height/15, (int)(fuelChunk*hud.player.fuel), height/3);
            g2.setColor(hud.opaqueHudColor);
            g2.drawRect(x+width/2+width/10, y+height/2+height/15, (int)(fuelChunk*hud.player.maxFuel), height/3);
            g2.setColor(hud.textColor);
            g2.drawString("Fuel", x+width/2+width/6+width/70, (int)(y+height/2+height/3.2));

            g2.setComposite(old);
        };
    }
}
