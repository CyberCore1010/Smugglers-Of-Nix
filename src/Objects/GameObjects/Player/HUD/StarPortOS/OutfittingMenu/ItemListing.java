package Objects.GameObjects.Player.HUD.StarPortOS.OutfittingMenu;

import Init.Game;
import Objects.GameObjects.Player.Components.Component;
import Objects.GameObjects.Player.Components.ComponentID;
import Objects.GameObjects.Player.Components.Level;
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
    private Level level;
    private Component component;

    private boolean error;
    private int errorTimer = 0;

    private String name;
    private String[] description;

    public ItemListing(int x, int y, int width, int height, StarPortOS starPortOS, int price, ComponentID type, Level level, Component component) {
        super(x, y, width, height);
        this.starPortOS = starPortOS;
        this.price = price;
        this.type = type;
        this.level = level;
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
        description = description();
    }

    public ItemListing(int x, int y, int width, int height, ItemListing itemListing, ComponentID newType) {
        super(x, y, width, height);
        this.starPortOS = itemListing.starPortOS;
        this.price = itemListing.price;
        this.type = newType;
        this.level = itemListing.level;
        this.component = itemListing.component;

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

        name = itemListing.name;
        description = itemListing.description;
    }

    private String randomName() {
        int index = Maths.randomInt(1, 7);
        if((type == ComponentID.weaponLeft || type == ComponentID.weaponRight) && level == Level.elite) return "Unknown";
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

    //25 max length for each line
    private String[] description() {
        String[] returnString;

        if(type == ComponentID.shield) {
            if(level == Level.basic) {
                returnString = new String[3];
                returnString[0] = "A basic energy shield that's not too";
                returnString[1] = "effective.";
                returnString[2] = "Wait, is that a AA battery?";
            } else if(level == Level.advanced) {
                returnString = new String[2];
                returnString[0] = "An advanced shield used by military";
                returnString[1] = "ships. ";
            } else {
                returnString = new String[3];
                returnString[0] = "An elite level shield that uses a";
                returnString[1] = "double level battery bank to raise";
                returnString[2] = "damage soaking and recharge rate.";
            }
        } else if(type == ComponentID.engine) {
            if(level == Level.basic) {
                returnString = new String[2];
                returnString[0] = "A basic engine with a low steer range";
                returnString[1] = "but a reasonable acceleration rate.";
            } else if(level == Level.advanced) {
                returnString = new String[3];
                returnString[0] = "An advanced racing grade engine with";
                returnString[1] = "a faster acceleration and steer rate";
                returnString[2] = "than your regular engine.";
            } else {
                returnString = new String[3];
                returnString[0] = "This experimental engine uses new";
                returnString[1] = "technology to use a partial jump";
                returnString[2] = "to accelerate much faster.";
            }
        } else if(type == ComponentID.hull) {
            if(level == Level.basic) {
                returnString = new String[3];
                returnString[0] = "A basic hull created with aluminum,";
                returnString[1] = "Provides basic protection from";
                returnString[2] = "the surrounding environment.";
            } else if(level == Level.advanced) {
                returnString = new String[3];
                returnString[0] = "An advanced hull that uses a mix";
                returnString[1] = "of high quality metals and plastics";
                returnString[2] = "To provide substantial protection.";
            } else {
                returnString = new String[3];
                returnString[0] = "Based on the advanced hull, this";
                returnString[1] = "model adds a layer of highly ";
                returnString[2] = "reflective plating to deflect light";
            }
        } else if(type == ComponentID.jumpdrive) {
            if(level == Level.basic) {
                returnString = new String [3];
                returnString[0] = "Allows for small jumps between two";
                returnString[1] = "Systems. Can travel to: Sol,";
                returnString[2] = "Fortuna, and Nero";
            } else if(level == Level.advanced) {
                returnString = new String [3];
                returnString[0] = "Allows for frame shifts between two";
                returnString[1] = "Systems. Can travel to: Titanus,";
                returnString[2] = "Genesis, and Novis + basic systems";
            } else {
                returnString = new String [3];
                returnString[0] = "Allows for high range quantum travel";
                returnString[1] = "Can travel to: Astraeus, Nyx and";
                returnString[2] = "⠠⠛⠗⠊⠍ + advanced & basic systems";
            }
        } else { //weapons
            if(level == Level.basic) {
                returnString = new String [2];
                returnString[0] = "Basic light cannon that can burn";
                returnString[1] = "through a few mm of metal";
            } else if(level == Level.advanced) {
                returnString = new String [3];
                returnString[0] = "High powered light cannon that";
                returnString[1] = "cuts through metals like a saw";
                returnString[2] = "through wood.";
            } else {
                returnString = new String [3];
                returnString[0] = "Extremely hot heat ray found";
                returnString[1] = "on a tripod after the martian";
                returnString[2] = "attack on earth in 1894.";
            }
        }

        return returnString;
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

            g2.drawString("Price: "+price, x+width/15, (int)(y+height/1.045));

            int yValue = y+height/3;
            for(String string : description) {
                g2.drawString(string, x+width/15, yValue);
                yValue+=g2.getFont().getSize()*1.3;
            }

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
