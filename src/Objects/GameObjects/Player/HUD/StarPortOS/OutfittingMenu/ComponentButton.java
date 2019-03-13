package Objects.GameObjects.Player.HUD.StarPortOS.OutfittingMenu;

import Objects.GameObjects.Player.HUD.StarPortOS.InteractivePanel;
import Objects.GameObjects.Player.HUD.StarPortOS.StarPortOS;
import Objects.GameObjects.Properties.Drawable;
import Objects.GameObjects.Properties.Effect;

import java.awt.*;

public class ComponentButton extends InteractivePanel {
    private StarPortOS starPortOS;

    public ComponentButton(int x, int y, int width, int height, StarPortOS starPortOS, Effect effect) {
        super(x, y, width, height);
        this.starPortOS = starPortOS;
        this.effect = effect;
    }

    @Override
    public Drawable getGraphics() {
        return (g2) -> {
            if(hover) {
                g2.setColor(starPortOS.hud.lightHudColor);
                g2.setColor(new Color(255,140,0));
                g2.setColor(new Color(255, 255, 255 ,25));
                g2.fillRect(x, y, width, height);
            }
        };
    }
}
