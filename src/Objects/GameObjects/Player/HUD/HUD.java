package Objects.GameObjects.Player.HUD;

import Init.CameraID;
import Init.Game;
import Init.Window;
import Objects.GameObjects.Player.HUD.StarPortOS.StarPortOS;
import Objects.GameObjects.Player.Player;
import Objects.GameObjects.Properties.Drawable;
import Objects.Utility.SFXPlayer;

import java.awt.*;

public class HUD {
    public Player player;

    public Color opaqueHudColor, darkHudColor, lightHudColor, textColor;
    public Font font;
    SFXPlayer hover1, hover2, click1, click2;
    private SFXPlayer ambiance;

    private Console leftConsole;
    private Console middleConsole;
    private Console rightConsole;

    private StarPortOS starPortOS;

    boolean displayHardpointError = false;
    private int errorTimer = 0;

    public HUD(Player player) {
        this.player = player;

        textColor = Color.WHITE;
        opaqueHudColor = new Color(195, 97, 31);
        lightHudColor = new Color(195, 97, 31, 200);
        darkHudColor = new Color(195, 97, 31, 100);
        font = new Font("Dialog", Font.PLAIN, Window.gameWidth/100);

        hover1 = new SFXPlayer("res/SFX/UI/Hover 1.wav", false);
        hover2 = new SFXPlayer("res/SFX/UI/Hover 2.wav", false);
        click1 = new SFXPlayer("res/SFX/UI/Click 1.wav", false);
        click2 = new SFXPlayer("res/SFX/UI/Click 2.wav", false);

        ambiance = new SFXPlayer("res/SFX/Ambiance.wav", false);

        leftConsole = new LeftConsole(this);
        middleConsole = new MiddleConsole(this);
        rightConsole = new RightConsole(this);

        starPortOS = new StarPortOS(this);
    }

    public void update() {
        if(displayHardpointError) {
            if(errorTimer > 0) {
                errorTimer--;
            } else {
                displayHardpointError = false;
            }
        }

        if(player.docked) {
            starPortOS.update();
            ambiance.stop();
        } else {
            if(!ambiance.getClip().isActive()) {
                ambiance.play();
            }
            leftConsole.update();
            middleConsole.update();
            rightConsole.update();
            starPortOS.playJingle = true;
            starPortOS.ambiance.stop();
        }
    }

    public void hardpointError() {
        displayHardpointError = true;
        errorTimer = 150;
    }

    public void render(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(3));
        g2d.setFont(font);

        if(!player.docked) {
            draw(leftConsole.getGraphics(), g2d);
            draw(middleConsole.getGraphics(), g2d);
            draw(rightConsole.getGraphics(), g2d);
        }

        if(player.canDock) {
            if(displayHardpointError) {
                Drawable hardpointError = HUD::drawError;
                draw(hardpointError, g2d);
            } else {
                Drawable dockPrompt = (g2) -> {
                    g2.setColor(darkHudColor);
                    g2.fillRect(-120, -145, 230, g2.getFont().getSize()*2);
                    g2.setColor(opaqueHudColor);
                    g2.drawRect(-120, -145, 230, g2.getFont().getSize()*2);
                    g2.setColor(textColor);
                    g2.drawString("Press [HOME] to dock", -100, -120);
                };
                draw(dockPrompt, g2d);
            }
        } else if(player.docked) {
            draw(starPortOS.getGraphics(), g2d);
        }
    }

    private void draw(Drawable item, Graphics2D g2d) {
        g2d.setTransform(Game.getInstance().cameraMap.get(CameraID.screen).getTransform());
        item.draw(g2d);
    }

    static void drawError(Graphics2D g2) {
        g2.setColor(new Color(255, 0, 0, 100));
        g2.fillRect(-130, -145, 240, g2.getFont().getSize()*2);
        g2.setColor(Color.red);
        g2.drawRect(-130, -145, 240, g2.getFont().getSize()*2);
        g2.setColor(Color.white);
        g2.drawString("ERROR: stow weapons!", -110, -120);
    }
}
