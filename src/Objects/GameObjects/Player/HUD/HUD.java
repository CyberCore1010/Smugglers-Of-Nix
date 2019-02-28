package Objects.GameObjects.Player.HUD;

import Init.CameraID;
import Init.Game;
import Init.Window;
import Objects.GameObjects.Player.Player;
import Objects.GameObjects.Properties.Drawable;
import Objects.Utility.SFXPlayer;

import java.awt.*;

public class HUD {
    Player player;

    Color textColor, opaqueHudColor, lightHudColor, darkHudColor;
    private Font font;
    SFXPlayer hover1, hover2;

    private MiddleConsole middleConsole;
    private RightConsole rightConsole;

    private StarPortOS starPortOS;

    public HUD(Player player) {
        this.player = player;

        textColor = Color.WHITE;
        opaqueHudColor = new Color(195, 97, 31);
        lightHudColor = new Color(195, 97, 31, 200);
        darkHudColor = new Color(195, 97, 31, 100);
        font = new Font("Dialog", Font.PLAIN, Window.gameWidth/100);

        hover1 = new SFXPlayer("res/SFX/UI/Hover 1.wav", false);
        hover2 = new SFXPlayer("res/SFX/UI/Hover 2.wav", false);

        middleConsole = new MiddleConsole(this);
        rightConsole = new RightConsole(this);

        starPortOS = new StarPortOS(this);
    }

    public void update() {
        if(player.docked) {
            starPortOS.update();
        } else {
            middleConsole.update();
            rightConsole.update();
            starPortOS.playJingle = true;
        }
    }

    public void render(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(3));
        g2d.setFont(font);

        if(!player.docked) {
            draw(middleConsole.getGraphics(), g2d);
            draw(rightConsole.getGraphics(), g2d);
        }

        if(player.canDock) {
            Drawable dockPrompt = (g2) -> {
                g2.setColor(textColor);
                g2.drawString("Press [HOME] to dock", -95, -120);
            };
            draw(dockPrompt, g2d);
        } else if(player.docked) {
            draw(starPortOS.getGraphics(), g2d);
        }
    }

    private void draw(Drawable item, Graphics2D g2d){
        g2d.setTransform(Game.getInstance().cameraMap.get(CameraID.screen).getTransform());
        item.draw(g2d);
    }
}
