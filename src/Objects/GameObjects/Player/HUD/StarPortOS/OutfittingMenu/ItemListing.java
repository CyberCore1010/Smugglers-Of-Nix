package Objects.GameObjects.Player.HUD.StarPortOS.OutfittingMenu;

import Init.Game;
import Objects.GameObjects.Player.Components.Component;
import Objects.GameObjects.Player.Components.ComponentID;
import Objects.GameObjects.Player.Components.Weapon;
import Objects.GameObjects.Player.HUD.StarPortOS.InteractivePanel;
import Objects.GameObjects.Player.HUD.StarPortOS.StarPortOS;
import Objects.GameObjects.Player.Player;
import Objects.GameObjects.Properties.Drawable;
import Objects.Utility.Maths.Maths;

import java.awt.*;

public class ItemListing extends InteractivePanel {
    private StarPortOS starPortOS;
    private int price;
    private ComponentID type;
    private Component component;

    private boolean error;
    private int errorTimer = 0;

    private String name;
    private String[] description;

    public ItemListing(int x, int y, int width, int height, StarPortOS starPortOS, int price, ComponentID type, Component component) {
        super(x, y, width, height);
        this.starPortOS = starPortOS;
        this.price = price;
        this.type = type;
        this.component = component;

        effect = ()->{
            Player player = Game.getInstance().player;
            if(player.credits > price) {
                player.credits -= price;
                player.upgrade(type, component);
            } else {
                error = true;
                errorTimer = 4000;
            }
        };

        name = randomName();
    }

    private String randomName() {
        int index = Maths.randomInt(1, 7);
        switch(index) {
            case 1:
                return "Vodel";
            case 2:
                return "Gutamaya";
            case 3:
                return "Zorgon Peterson";
            case 4:
                return "Lakon Spaceways";
            case 5:
                return "Faulcon DeLacy";
            case 6:
                return "Saud Kruger";
            default:
                return "Core Dynamics";
        }
    }

    @Override
    public Drawable getGraphics() {
        return (g2)->{
            if(hover) {
                g2.setColor(starPortOS.hud.darkHudColor);
                g2.fillRect(x, y, width, height);
            }

            g2.setColor(starPortOS.hud.opaqueHudColor);
            g2.drawLine(x, y+height/5, x+width, y+height/5);
            g2.drawRect(x, (int)(y+height/1.5), width, (int)(height-height/1.5));
            g2.drawRect(x, (int)((y+height/1.5)+(height-height/1.5)/2), width, (int)((height-height/1.5)/2));

            g2.setColor(starPortOS.hud.textColor);
            g2.drawString(name,x+width/15, (int)(y+height/6.9));

            String stats;
            switch(type) {
                case shield:
                    stats = "Hit points: "+component.getStat()[0]+" - Recharge rate: "+component.getStat()[1];
                    break;
                case hull:
                    stats = "Structural Integrity: "+component.getStat()[0];
                    break;
                case engine:
                    stats = "Acceleration: "+component.getStat()[0]+" - Turn rate: "+component.getStat()[1];
                    break;
                case jumpdrive:
                    stats = "Range: "+component.getStat()[0];
                    break;
                default: //Weapons
                    stats = "Damage per shot: "+component.getStat()[0];
                    break;
            }
            g2.drawString(stats, x+width/15, (int)(y+height/1.4+height/14));

            if(error) {
                g2.setColor(new Color(114, 47, 55, 150));
                g2.fillRect(x, y, width, height);
                g2.setColor(Color.RED);
                if(errorTimer > 0) errorTimer--;
                else error = false;
                String errorString = "ERROR: Insufficient Credits";
                g2.drawString(errorString, (int)(x+width/2-(Math.ceil(g2.getFont().getSize()*errorString.length()/4))), y+height/2+g2.getFont().getSize()/2);
            } else {
                g2.setColor(starPortOS.hud.opaqueHudColor);
            }

            g2.drawRect(x, y, width, height);
        };
    }
}
