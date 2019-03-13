package Objects.GameObjects.Player.HUD.StarPortOS.OutfittingMenu;

import Objects.GameObjects.Player.Components.ComponentID;
import Objects.GameObjects.Player.HUD.StarPortOS.InteractivePanel;
import Objects.GameObjects.Player.HUD.StarPortOS.StarPortOS;
import Objects.GameObjects.Player.HUD.StarPortOS.Tab;
import Objects.GameObjects.Properties.Drawable;
import Objects.Utility.BufferedImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OutfittingMenu extends Tab{
    private BufferedImage shipDiagram;

    ComponentID currentMenu;
    InteractivePanel shieldButton, engineButton;

    public OutfittingMenu(StarPortOS starPortOS) {
        this.starPortOS = starPortOS;

        x = starPortOS.x+starPortOS.width/5;
        y = starPortOS.y;
        width = starPortOS.width-starPortOS.width/5;
        height = starPortOS.height;

        shipDiagram = new BufferedImageLoader().loadImage("/Sprites/UI/PlayerWireframe.png");
        shieldButton = new ComponentButton((int)(x+width/5.18), (int)(y+height/6.3), (int)(width/6.468), (int)(height/11.05),
                starPortOS, ()-> currentMenu = ComponentID.shield);
        engineButton = new ComponentButton((int)(x+width/5.18), (int)(y+height/3.455), (int)(width/6.468), (int)(height/11.1),
                starPortOS, ()-> currentMenu = ComponentID.engine);
    }

    @Override
    public void update() {
        shieldButton.update();
        engineButton.update();
    }

    @Override
    public Drawable getGraphics() {
        return (g2) -> {
            g2.drawImage(shipDiagram, x-height/8, y, 1+height-1, height, null);

            g2.setColor(starPortOS.hud.opaqueHudColor);
            g2.drawRect((int)(x+width/1.5), y, width/3, height);

            shieldButton.getGraphics().draw(g2);
            engineButton.getGraphics().draw(g2);
            //g2.setColor(new Color(0, 0, 255, 100));
            //g2.fillRect((int)(x+width/5.18), (int)(y+height/3.455), (int)(width/6.468), (int)(height/11.1));
        };
    }
}
