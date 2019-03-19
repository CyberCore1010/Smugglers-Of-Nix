package Objects.GameObjects.Player.HUD.StarPortOS;

import Init.Game;
import Objects.GameObjects.Properties.Drawable;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Menu extends Tab {
    private int x, y, width, height;
    private InteractivePanel infoButton, missionBoardButton, outfittingButton, exitButton;

    Menu(StarPortOS starPortOS) {
        this.starPortOS = starPortOS;

        x = starPortOS.x;
        y = starPortOS.y;
        width = starPortOS.width/5;
        height = starPortOS.height;

        starPortOS.currentTab = TabType.mission;

        int buttonY = y+((height/5)*2)-(height/35);
        infoButton = new Button(x+20, buttonY, width-40, height/8, "Info", ()-> starPortOS.currentTab = TabType.info);
        missionBoardButton = new Button(x+20, buttonY+(height/8)+(height/35), width-40, height/8, "Mission Board", ()-> starPortOS.currentTab = TabType.mission);
        outfittingButton = new Button(x+20, buttonY+((height/8)*2)+((height/35)*2), width-40, height/8, "Outfitting", ()-> starPortOS.currentTab = TabType.outfitting);
        exitButton = new Button(x+20, buttonY+((height/8)*3)+((height/35)*3), width-40, height/8, "Exit", ()->{
            Game.getInstance().player.docked = false;
            Game.getInstance().player.canDock = true;
        });
    }

    @Override
    public void update() {
        infoButton.update();
        missionBoardButton.update();
        outfittingButton.update();

        exitButton.update();
    }

    @Override
    public Drawable getGraphics() {
        return (g2)-> {
            g2.setColor(starPortOS.hud.opaqueHudColor);
            g2.drawRect(x, y, width, height);

            header(g2);

            int buttonY = y+((height/5)*2)-(height/35);
            switch(starPortOS.currentTab) {
                case info:
                    g2.fillRect(x+20, buttonY, width-40, height/8);
                    break;
                case mission:
                    g2.fillRect(x+20, buttonY+(height/8)+(height/35), width-40, height/8);
                    break;
                case outfitting:
                    g2.fillRect(x+20, buttonY+((height/8)*2)+((height/35)*2), width-40, height/8);
                    break;
            }

            draw(infoButton.getGraphics(), g2);
            draw(missionBoardButton.getGraphics(), g2);
            draw(outfittingButton.getGraphics(), g2);
            draw(exitButton.getGraphics(), g2);
        };
    }

    private void header(Graphics2D g2) {
        g2.drawRect(x, y, width, height/8);
        icon(g2);
        g2.drawString(Game.getInstance().player.getCurrentLocation().toString(), x+(height/8), y+(height/8)/4);
        g2.drawLine(x+(height/8), y+(((height/8)/6)*2), (x+width)-20, y+(((height/8)/6)*2));
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        g2.drawString(dateFormat.format(cal.getTime()), x+(height/8), y+(((height/8)/8)*5));
        g2.drawString(cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+"/"+(cal.get(Calendar.YEAR)+1000), x+(height/8), y+(((height/8)/8)*7));

        g2.drawRect(x, y+height/8, width, height/9);
        g2.drawString("Welcome", x+20, ((y+height/8)+((height/9)/5)*2));
        g2.drawString("Commander", x+20, ((y+height/8)+((height/9)/5)*4));

        g2.drawRect(x, y+height/8, width, (height/9)*2);
        g2.drawString("Credits:", x+20, ((y+height/8)+(height/9)+((height/9)/5)*2));
        g2.drawString(Integer.toString(Game.getInstance().player.credits), x+20, ((y+height/8)+(height/9)+((height/9)/5)*4));
    }
    private void icon(Graphics2D g2) {
        int iconX = x+10;
        int iconY = y+10;
        int iconWidth = height/8-20;
        int iconHeight = height/8-20;
        g2.drawRect(iconX, iconY, iconWidth, iconHeight);

        g2.drawLine(iconX, iconY+iconHeight/2, iconX+iconWidth/2, iconY);
        g2.drawLine(iconX, iconY+iconHeight/2, iconX+iconWidth/2, iconY+iconHeight);

        g2.drawLine(iconX+iconWidth, iconY+iconHeight/2, iconX+iconWidth/2, iconY);
        g2.drawLine(iconX+iconWidth, iconY+iconHeight/2, iconX+iconWidth/2, iconY+iconHeight);

        g2.drawRect(iconX+iconWidth/4, (iconY+iconHeight/2)-4, iconWidth/2,8);
    }

    private void draw(Drawable drawable, Graphics2D g2) {
        drawable.draw(g2);
    }
}
