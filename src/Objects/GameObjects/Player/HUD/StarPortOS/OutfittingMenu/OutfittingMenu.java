package Objects.GameObjects.Player.HUD.StarPortOS.OutfittingMenu;

import Init.Window;
import Objects.GameObjects.Player.Components.*;
import Objects.GameObjects.Player.HUD.StarPortOS.InteractivePanel;
import Objects.GameObjects.Player.HUD.StarPortOS.StarPortOS;
import Objects.GameObjects.Player.HUD.StarPortOS.Tab;
import Objects.GameObjects.Properties.Drawable;
import Objects.Utility.BufferedImageLoader;
import Objects.Utility.Maths.Maths;
import Objects.Utility.ObjectList;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OutfittingMenu extends Tab{
    private BufferedImage shipDiagram;

    private ComponentID currentMenu = ComponentID.jumpdrive;
    private InteractivePanel shieldButton, engineButton, jumpdriveButton, hullButton, leftGunButton, rightGunButton;

    private ObjectList<ItemListing> currentSelection, shieldSelection, engineSelection, jumpdriveSelection,
            hullSelection, leftWeaponSelection, rightWeaponSelection;

    private Font titleFont;

    public OutfittingMenu(StarPortOS starPortOS) {
        this.starPortOS = starPortOS;

        x = starPortOS.x+starPortOS.width/5;
        y = starPortOS.y;
        width = starPortOS.width-starPortOS.width/5;
        height = starPortOS.height;

        titleFont = new Font("Dialog", Font.PLAIN, Window.gameWidth/50);

        createDiagram();
        createSelections();
    }

    private void createDiagram() {
        shipDiagram = new BufferedImageLoader().loadImage("/Sprites/UI/PlayerWireframe.png");
        shieldButton = new ComponentButton((int)(x+width/5.18), (int)(y+height/6.3), (int)(width/6.468), (int)(height/11.05),
                starPortOS, ()-> currentMenu = ComponentID.shield);
        engineButton = new ComponentButton((int)(x+width/5.18), (int)(y+height/3.455), (int)(width/6.468), (int)(height/11.1),
                starPortOS, ()-> currentMenu = ComponentID.engine);
        jumpdriveButton = new ComponentButton((int)(x+width/5.18), (int)(y+height/1.6655), (int)(width/6.468), (int)(height/10.95),
                starPortOS, ()-> currentMenu = ComponentID.jumpdrive);
        hullButton = new ComponentButton((int)(x+width/5.18), (int)(y+height/1.37), (int)(width/6.468), (int)(height/10.95),
                starPortOS, ()-> currentMenu = ComponentID.hull);
        leftGunButton = new ComponentButton((int)(x+width/2.605), (int)(y+height/3.04), (int)(width/11.8), height/17,
                starPortOS, ()-> currentMenu = ComponentID.weaponLeft);
        rightGunButton = new ComponentButton((int)(x+width/2.605), (int)(y+height/1.69), (int)(width/11.8), (int)(height/16.8),
                starPortOS, ()-> currentMenu = ComponentID.weaponRight);
    }

    private void createSelections() {
        int boxX = (int)(x+width/1.5);
        int boxY = this.y+height/4;
        int boxWidth = width/3;

        shieldSelection = new ObjectList<>();
        shieldSelection.add(new ItemListing(boxX+boxWidth/20, boxY, boxWidth-boxWidth/10, (int)(height/4.5),
                starPortOS, Maths.randomInt(100, 500), ComponentID.shield, Level.basic, new Shield(Level.basic)));
        shieldSelection.add(new ItemListing(boxX+boxWidth/20, boxY+height/4, boxWidth-boxWidth/10, (int)(height/4.5),
                starPortOS, Maths.randomInt(1000, 2000), ComponentID.shield, Level.advanced, new Shield(Level.advanced)));
        shieldSelection.add(new ItemListing(boxX+boxWidth/20, boxY+((height/4)*2), boxWidth-boxWidth/10, (int)(height/4.5),
                starPortOS, Maths.randomInt(2500, 4000), ComponentID.shield, Level.elite, new Shield(Level.elite)));

        engineSelection = new ObjectList<>();
        engineSelection.add(new ItemListing(boxX+boxWidth/20, boxY, boxWidth-boxWidth/10, (int)(height/4.5),
                starPortOS, Maths.randomInt(100, 500), ComponentID.engine, Level.basic, new Engine(Level.basic)));
        engineSelection.add(new ItemListing(boxX+boxWidth/20, boxY+height/4, boxWidth-boxWidth/10, (int)(height/4.5),
                starPortOS, Maths.randomInt(750, 2000), ComponentID.engine, Level.advanced, new Engine(Level.advanced)));
        engineSelection.add(new ItemListing(boxX+boxWidth/20, boxY+((height/4)*2), boxWidth-boxWidth/10, (int)(height/4.5),
                starPortOS, Maths.randomInt(1500, 2500), ComponentID.engine, Level.elite, new Engine(Level.elite)));

        jumpdriveSelection = new ObjectList<>();
        jumpdriveSelection.add(new ItemListing(boxX+boxWidth/20, boxY, boxWidth-boxWidth/10, (int)(height/4.5),
                starPortOS, Maths.randomInt(1000, 1500), ComponentID.jumpdrive, Level.basic, new Jumpdrive(Level.basic)));
        jumpdriveSelection.add(new ItemListing(boxX+boxWidth/20, boxY+height/4, boxWidth-boxWidth/10, (int)(height/4.5),
                starPortOS, Maths.randomInt(2000, 3500), ComponentID.jumpdrive, Level.advanced, new Jumpdrive(Level.advanced)));
        jumpdriveSelection.add(new ItemListing(boxX+boxWidth/20, boxY+((height/4)*2), boxWidth-boxWidth/10, (int)(height/4.5),
                starPortOS, Maths.randomInt(4000, 6000), ComponentID.jumpdrive, Level.elite, new Jumpdrive(Level.elite)));

        hullSelection = new ObjectList<>();
        hullSelection.add(new ItemListing(boxX+boxWidth/20, boxY, boxWidth-boxWidth/10, (int)(height/4.5),
                starPortOS, Maths.randomInt(100, 500), ComponentID.hull, Level.basic, new Hull(Level.basic)));
        hullSelection.add(new ItemListing(boxX+boxWidth/20, boxY+height/4, boxWidth-boxWidth/10, (int)(height/4.5),
                starPortOS, Maths.randomInt(1000, 1500), ComponentID.hull, Level.advanced, new Hull(Level.advanced)));
        hullSelection.add(new ItemListing(boxX+boxWidth/20, boxY+((height/4)*2), boxWidth-boxWidth/10, (int)(height/4.5),
                starPortOS, Maths.randomInt(2000, 3500), ComponentID.hull, Level.elite, new Hull(Level.elite)));

        leftWeaponSelection = new ObjectList<>();
        leftWeaponSelection.add(new ItemListing(boxX+boxWidth/20, boxY, boxWidth-boxWidth/10, (int)(height/4.5),
                starPortOS, Maths.randomInt(100, 500), ComponentID.weaponLeft, Level.basic, new Hull(Level.basic)));
        leftWeaponSelection.add(new ItemListing(boxX+boxWidth/20, boxY+height/4, boxWidth-boxWidth/10, (int)(height/4.5),
                starPortOS, Maths.randomInt(1000, 2000), ComponentID.weaponLeft, Level.advanced, new Hull(Level.advanced)));
        leftWeaponSelection.add(new ItemListing(boxX+boxWidth/20, boxY+((height/4)*2), boxWidth-boxWidth/10, (int)(height/4.5),
                starPortOS, Maths.randomInt(2500, 4000), ComponentID.weaponLeft, Level.elite, new Weapon(Level.elite)));

        rightWeaponSelection = new ObjectList<>();
        rightWeaponSelection.add(new ItemListing(boxX+boxWidth/20, boxY, boxWidth-boxWidth/10, (int)(height/4.5),
                leftWeaponSelection.get(0), ComponentID.weaponRight));
        rightWeaponSelection.add(new ItemListing(boxX+boxWidth/20, boxY+height/4, boxWidth-boxWidth/10, (int)(height/4.5),
                leftWeaponSelection.get(1), ComponentID.weaponRight));
        rightWeaponSelection.add(new ItemListing(boxX+boxWidth/20, boxY+((height/4)*2), boxWidth-boxWidth/10, (int)(height/4.5),
                leftWeaponSelection.get(2), ComponentID.weaponRight));
    }

    @Override
    public void update() {
        shieldButton.update();
        engineButton.update();
        jumpdriveButton.update();
        hullButton.update();
        leftGunButton.update();
        rightGunButton.update();

        switch(currentMenu) {
            case weaponLeft:
                currentSelection = leftWeaponSelection;
                break;
            case weaponRight:
                currentSelection = rightWeaponSelection;
                break;
            case shield:
                currentSelection = shieldSelection;
                break;
            case hull:
                currentSelection = hullSelection;
                break;
            case engine:
                currentSelection = engineSelection;
                break;
            case jumpdrive:
                currentSelection = jumpdriveSelection;
                break;
        }

        for(ItemListing listing : currentSelection) {
            listing.update();
        }
    }

    @Override
    public Drawable getGraphics() {
        return (g2) -> {
            g2.setColor(starPortOS.hud.opaqueHudColor);
            g2.drawRect((int)(x+width/1.5), y, width/3, height);

            g2.setFont(titleFont);
            switch(currentMenu) {
                case weaponLeft:
                    g2.drawString("Left Weapon", (int)(x+width/1.5+width/20), y+height/7);
                    break;
                case weaponRight:
                    g2.drawString("Right Weapon", (int)(x+width/1.5+width/20), y+height/7);
                    break;
                case shield:
                    g2.drawString("Shield", (int)(x+width/1.5+width/20), y+height/7);
                    break;
                case hull:
                    g2.drawString("Hull", (int)(x+width/1.5+width/20), y+height/7);
                    break;
                case engine:
                    g2.drawString("Engine", (int)(x+width/1.5+width/20), y+height/7);
                    break;
                case jumpdrive:
                    g2.drawString("Jumpdrive", (int)(x+width/1.5+width/20), y+height/7);
                    break;
            }
            g2.setFont(starPortOS.hud.font);

            drawInteractiveDiagram(g2);

            for(ItemListing listing : currentSelection) {
                listing.getGraphics().draw(g2);
            }
        };
    }

    private void drawInteractiveDiagram(Graphics2D g2) {
        g2.drawImage(shipDiagram, x-height/8, y, 1+height-1, height, null);

        g2.drawLine(shieldButton.x, shieldButton.y, shieldButton.x-shieldButton.width, shieldButton.y);
        g2.drawString("Shield Generator", shieldButton.x-shieldButton.width, shieldButton.y-g2.getFont().getSize()/2);

        g2.drawLine(engineButton.x, engineButton.y, engineButton.x-engineButton.width, engineButton.y);
        g2.drawString("Engine", engineButton.x-engineButton.width, engineButton.y-g2.getFont().getSize()/2);

        g2.drawLine(jumpdriveButton.x, jumpdriveButton.y, jumpdriveButton.x-jumpdriveButton.width, jumpdriveButton.y);
        g2.drawString("Jumpdrive", jumpdriveButton.x-jumpdriveButton.width, jumpdriveButton.y-g2.getFont().getSize()/2);

        g2.drawLine(hullButton.x, hullButton.y, hullButton.x-hullButton.width, hullButton.y);
        g2.drawString("Hull", hullButton.x-hullButton.width, hullButton.y-g2.getFont().getSize()/2);

        g2.drawLine(leftGunButton.x+leftGunButton.width, leftGunButton.y, leftGunButton.x+(leftGunButton.width*3), leftGunButton.y);
        g2.drawString("Left Gun", leftGunButton.x+(leftGunButton.width*2), leftGunButton.y-g2.getFont().getSize()/2);

        g2.drawLine(rightGunButton.x+rightGunButton.width, rightGunButton.y, rightGunButton.x+(rightGunButton.width*3), rightGunButton.y);
        g2.drawString("Right Gun", rightGunButton.x+(rightGunButton.width*2), rightGunButton.y-g2.getFont().getSize()/2);

        shieldButton.getGraphics().draw(g2);
        engineButton.getGraphics().draw(g2);
        jumpdriveButton.getGraphics().draw(g2);
        hullButton.getGraphics().draw(g2);
        leftGunButton.getGraphics().draw(g2);
        rightGunButton.getGraphics().draw(g2);
    }
}
