package Objects.GameObjects.Player.HUD;

import Init.Window;
import Objects.GameObjects.Properties.Drawable;
import Objects.Utility.BufferedImageLoader;
import Objects.Utility.SFXPlayer;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class StarPortOS {
    private HUD hud;

    private int x, y, width, height;

    boolean playJingle = true;

    private SFXPlayer jingle;
    private int SplashScreenOffset = 0, SplashScreenTime = 0, max = Window.gameWidth/2;
    private BufferedImage splashScreen1, splashScreen2;

    StarPortOS(HUD hud) {
        this.hud = hud;

        x = (int)(-Window.gameWidth/2.5);
        y = (int)(-Window.gameHeight/2.5);
        width = (int)(Window.gameWidth/1.25);
        height = (int)(Window.gameHeight/1.25);

        jingle = new SFXPlayer("res/SFX/UI/Starport jingle.wav", false);

        BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
        splashScreen1 = bufferedImageLoader.loadImage("/Sprites/UI/Starport1.png");
        splashScreen2 = bufferedImageLoader.loadImage("/Sprites/UI/Starport2.png");
    }

    void update() {
        if(playJingle) {
            SplashScreenTime = 0;
            SplashScreenOffset = 0;
            playJingle = false;
            jingle.play();
        }

        if(SplashScreenTime > 250) {
            if(SplashScreenOffset <= max) {
                SplashScreenOffset+=20;
            }
        } else {
            SplashScreenTime++;
        }
    }

    Drawable getGraphics() {
        return (g2)->{
            g2.setColor(hud.darkHudColor);
            g2.fillRect(x, y, width, height);
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRect(x, y, width, height);

            splashScreen(g2);

            g2.setColor(hud.opaqueHudColor);
            g2.drawRect(x, y, width, height);
        };
    }

    private void splashScreen(Graphics2D g2) {
        g2.setPaint(new TexturePaint(splashScreen1, new Rectangle2D.Double(x-SplashScreenOffset*2, y, width, height)));
        g2.fillRect(x, y, width-SplashScreenOffset*2, height);

        g2.setPaint(new TexturePaint(splashScreen2, new Rectangle2D.Double(x+SplashScreenOffset, y, width, height)));
        g2.fillRect(x+SplashScreenOffset, y, width-SplashScreenOffset, height);
    }
}
