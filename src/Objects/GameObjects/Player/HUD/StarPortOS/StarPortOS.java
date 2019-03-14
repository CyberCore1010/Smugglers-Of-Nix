package Objects.GameObjects.Player.HUD.StarPortOS;

import Init.CameraID;
import Init.Game;
import Init.Window;
import Objects.GameObjects.Player.HUD.HUD;
import Objects.GameObjects.Player.HUD.StarPortOS.MissionMenu.MissionMenu;
import Objects.GameObjects.Player.HUD.StarPortOS.OutfittingMenu.OutfittingMenu;
import Objects.GameObjects.Properties.Drawable;
import Objects.Utility.BufferedImageLoader;
import Objects.Utility.Maths.Vector2D;
import Objects.Utility.SFXPlayer;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class StarPortOS {
    public HUD hud;

    public int x, y, width, height;

    public boolean playJingle = true;

    private SFXPlayer jingle;
    private int SplashScreenOffset = 0, SplashScreenTime = 0, max = Window.gameWidth/2;
    private BufferedImage splashScreen1, splashScreen2;

    public SFXPlayer ambiance;

    TabType currentTab;
    private Tab menu, info, missionMenu, outfittingMenu;

    public StarPortOS(HUD hud) {
        this.hud = hud;

        x = (int)(-Window.gameWidth/2.5);
        y = (int)(-Window.gameHeight/2.3);
        width = (int)(Window.gameWidth/1.25);
        height = (int)(Window.gameHeight/1.25);

        jingle = new SFXPlayer("res/SFX/UI/Starport jingle.wav", false);
        ambiance = new SFXPlayer("res/SFX/UI/Ambiance.wav", false);

        BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
        splashScreen1 = bufferedImageLoader.loadImage("/Sprites/UI/Starport1.png");
        splashScreen2 = bufferedImageLoader.loadImage("/Sprites/UI/Starport2.png");

        menu = new Menu(this);

        missionMenu = new MissionMenu( this);
        outfittingMenu = new OutfittingMenu(this);
    }

    public void update() {
        if(!ambiance.getClip().isActive()) ambiance.play();

        if(SplashScreenOffset > max/1.5) {
            menu.update();
            switch (currentTab) {
                case info:
                    break;
                case mission:
                    missionMenu.update();
                    break;
                case outfitting:
                    outfittingMenu.update();
                    break;
            }
        }

        updateSplashScreen();
    }

    private void updateSplashScreen() {
        if(playJingle) {
            SplashScreenTime = 0;
            SplashScreenOffset = 0;
            MissionMenu missionMenu = (MissionMenu)this.missionMenu;
            missionMenu.generate();
            playJingle = false;
            jingle.play();
        }
        if(SplashScreenTime > 250) {
            if(SplashScreenOffset <= max) {
                SplashScreenOffset += 20;
            }
        } else {
            SplashScreenTime++;
        }
    }

    private void renderSplashScreen(Graphics2D g2) {
        g2.setPaint(new TexturePaint(splashScreen1, new Rectangle2D.Double(x-SplashScreenOffset*2, y, width, height)));
        g2.fillRect(x, y, width-SplashScreenOffset*2, height);

        g2.setPaint(new TexturePaint(splashScreen2, new Rectangle2D.Double(x+SplashScreenOffset, y, width, height)));
        g2.fillRect(x+SplashScreenOffset, y, width-SplashScreenOffset, height);
    }

    public Drawable getGraphics() {
        return (g2)->{
            g2.setColor(hud.darkHudColor);
            g2.fillRect(x, y, width, height);
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(x, y, width, height);

            draw(menu.getGraphics(), g2);

            switch (currentTab) {
                case info:
                    break;
                case mission:
                    draw(missionMenu.getGraphics(), g2);
                    break;
                case outfitting:
                    draw(outfittingMenu.getGraphics(), g2);
                    break;
            }

            renderSplashScreen(g2);

            g2.setColor(hud.opaqueHudColor);
            g2.drawRect(x, y, width, height);
        };
    }

    private void draw(Drawable drawable, Graphics2D g2) {
        drawable.draw(g2);
    }
}
