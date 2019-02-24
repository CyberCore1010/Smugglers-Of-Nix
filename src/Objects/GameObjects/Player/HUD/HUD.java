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
    Font font;
    SFXPlayer hover1, hover2;

    private MiddleConsole middleConsole;
    private RightConsole rightConsole;

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
    }

    public void update() {
        middleConsole.update();
        rightConsole.update();
    }

    public void render(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(3));
        g2d.setFont(font);

        draw(middleConsole.getGraphics(), g2d);
        draw(rightConsole.getGraphics(), g2d);
    }

    private void draw(Drawable item, Graphics2D g2d){
        g2d.setTransform(Game.getInstance().cameraMap.get(CameraID.screen).getTransform());
        item.draw(g2d);
    }
}
