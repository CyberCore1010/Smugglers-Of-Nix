package Objects.GameObjects.Player.HUD;

import Init.Game;
import Init.Window;
import Objects.GameObjects.Properties.Drawable;
import Objects.Utility.BufferedImageLoader;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class LeftConsole extends Console{
    BufferedImage wireframe;

    LeftConsole(HUD hud) {
        super(hud);
        x = -Window.gameWidth/2+20;
        y = (Window.gameHeight/2)-Window.gameHeight/5-80;
        width = Window.gameWidth/5;
        height = Window.gameHeight/5;

        wireframe = new BufferedImageLoader().loadImage("/Sprites/UI/EnemyWireframe.png");
    }

    @Override
    void update() {
        if(Game.getInstance().player.targetedEnemy != null) {
            if(Game.getInstance().player.targetedEnemy.dead) Game.getInstance().player.targetedEnemy = null;
        }
    }

    @Override
    Drawable getGraphics() {
        return (g2)->{
            g2.setColor(hud.darkHudColor);
            g2.fillRect(x, y, width, height);

            g2.setColor(hud.opaqueHudColor);
            g2.drawRect(x, y, width, height);

            if(Game.getInstance().player.targetedEnemy != null) {
                AffineTransform old = g2.getTransform();
                g2.rotate(hud.player.targetedEnemy.getRotation(), x+(height/1.5)/2, y+(height/1.5)/2);
                g2.drawImage(wireframe, x, y, (int)(height/1.5), (int)(height/1.5), null);
                g2.setTransform(old);

                g2.setColor(new Color(255,255,153, 200));
                double shieldAmount = width/2;
                shieldAmount = shieldAmount/hud.player.targetedEnemy.maxShield;
                shieldAmount = shieldAmount*hud.player.targetedEnemy.shield;
                g2.fillRect((int)(x+width/2.22), y+height/8, (int)shieldAmount, (int)(height/2.4));
                g2.setColor(hud.opaqueHudColor);
                g2.drawRect((int)(x+width/2.22), y+height/8, width/2, (int)(height/2.4));

                g2.setColor(hud.lightHudColor);
                double healthAmount = width-(width/10);
                healthAmount = healthAmount/100;
                healthAmount = healthAmount*hud.player.targetedEnemy.health;
                g2.fillRect(x+width/20, (int)(y+height/1.4), (int)healthAmount,height/5);
                g2.setColor(hud.opaqueHudColor);
                g2.drawRect(x+width/20, (int)(y+height/1.4), width-(width/10), height/5);
            } else {
                g2.setColor(hud.textColor);
                g2.drawString("No enemy targeted", (int)(x+width/3.5), (int)(y+height/1.9));
            }
        };
    }
}
