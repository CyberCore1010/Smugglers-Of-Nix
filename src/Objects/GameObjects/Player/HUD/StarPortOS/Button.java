package Objects.GameObjects.Player.HUD.StarPortOS;

import Objects.GameObjects.Properties.Drawable;
import Objects.GameObjects.Properties.Effect;
import Objects.Utility.SFXPlayer;

import java.awt.*;

public class Button extends InteractivePanel {
    private String text;


    public Button(int x, int y, int width, int height, String text, Effect effect) {
        super(x, y, width, height);
        this.text = text;
        this.effect = effect;
    }

    @Override
    public Drawable getGraphics() {
        return (g2) -> {
            g2.setColor(new Color(195, 97, 31));
            if(hover) {
                g2.fillRect(x, y, width, height);
            } else {
                g2.drawRect(x, y, width, height);
            }

            g2.setColor(Color.white);
            g2.drawString(text, x+g2.getFont().getSize(), y+height/2+(g2.getFont().getSize()/2));
        };
    }
}
