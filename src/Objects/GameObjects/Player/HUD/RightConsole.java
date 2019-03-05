package Objects.GameObjects.Player.HUD;

import Init.Window;
import Objects.GameObjects.Player.Components.ComponentID;
import Objects.GameObjects.Player.Missions.Bounty;
import Objects.GameObjects.Player.Missions.Empty;
import Objects.GameObjects.Player.Missions.Mission;
import Objects.GameObjects.Player.Missions.Tutorial;
import Objects.GameObjects.Properties.Drawable;
import Objects.GameWorld.SystemID;
import Objects.Utility.KeyHandler;
import Objects.Utility.Keys;
import Objects.Utility.Maths.Maths;
import Objects.Utility.ObjectList;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

class RightConsole extends Console{

    private boolean headingOption1;
    private boolean leftColumn;
    private int row, rowTimer;

    private Timer jumpChargeTimer;
    private int jumpChargeAmount = 0;
    private int jumpCountDown = 5;
    private boolean canCancel = false;

    RightConsole(HUD hud) {
        super(hud);
        x = (Window.gameWidth/2)-Window.gameWidth/5-20;
        y = (Window.gameHeight/2)-Window.gameHeight/5-40;
        width = Window.gameWidth/5;
        height = Window.gameHeight/5;

        headingOption1 = false;

        leftColumn = true;
        row = 0;
        rowTimer = 0;
    }

    @Override
    void update() {
        menuInteraction();
        if(hud.player.chargingJump) {
            if(jumpChargeAmount > 18) {
                jumpChargeAmount = 18;
            }
        }
    }

    private void menuInteraction() {
        //headings
        if(KeyHandler.isKeyPressed(Keys.num7)) {
            if(!headingOption1) {
                hoverSound();
            }
            headingOption1 = true;
        }
        if(KeyHandler.isKeyPressed(Keys.num9)) {
            if(headingOption1) {
                hoverSound();
            }
            headingOption1 = false;
        }

        if(headingOption1) {
            if(KeyHandler.isKeyPressed(Keys.num4)) {
                if(!leftColumn) {
                    hoverSound();
                }
                leftColumn = true;
            }
            if(KeyHandler.isKeyPressed(Keys.num6)) {
                if(leftColumn) {
                    hoverSound();
                }
                leftColumn = false;
            }

            if(KeyHandler.isKeyPressed(Keys.num8)) {
                if(rowTimer < 0) {
                    hoverSound();
                    rowTimer = 10;
                    row--;
                }
            }
            if(KeyHandler.isKeyPressed(Keys.num5)) {
                if(rowTimer < 0) {
                    hoverSound();
                    rowTimer = 10;
                    row++;
                }
            }

            if(KeyHandler.isKeyPressed(Keys.enter)) activateJump();
            row = (int)Maths.clamped(0, 3, row);
        }
        if(rowTimer >= 0) {
            rowTimer--;
        }
    }

    private void activateJump() {
        clickSound();
        KeyHandler.forceKey(Keys.enter, false);
        ObjectList<SystemID> list = new ObjectList<>();
        if(leftColumn) {
            list.add(SystemID.Sol);
            list.add(SystemID.Fortuna);
            list.add(SystemID.Nero);
            list.add(SystemID.Titanus);
        } else {
            list.add(SystemID.Genesis);
            list.add(SystemID.Novis);
            list.add(SystemID.Astraeus);
            list.add(SystemID.Nyx);
        }

        SystemID target = list.get(row);

        if(hud.player.getStat(ComponentID.jumpdrive)[0] >= target.distance) {
            if(!hud.player.jumping && !hud.player.chargingJump) {
                canCancel = true;
                jumpChargeTimer = new Timer();
                hud.player.jumpTo(target);
                TimerTask charge = new TimerTask() {
                    @Override
                    public void run() {
                        jumpChargeAmount++;
                    }
                };
                TimerTask cantCancel = new TimerTask() {
                    @Override
                    public void run() {
                        canCancel = false;
                    }
                };
                TimerTask countdown = new TimerTask() {
                    @Override
                    public void run() {
                        jumpCountDown--;
                    }
                };
                TimerTask end = new TimerTask() {
                    @Override
                    public void run() {
                        jumpChargeAmount = 0;
                        jumpCountDown = 5;
                        jumpChargeTimer.cancel();
                    }
                };
                jumpChargeTimer.scheduleAtFixedRate(charge, 1000, 1000);
                jumpChargeTimer.schedule(cantCancel, 10000);
                jumpChargeTimer.scheduleAtFixedRate(countdown, 15000, 1000);
                jumpChargeTimer.schedule(end, 19999);
            } else if(canCancel) {
                canCancel = false;
                hud.player.cancelJump();
                jumpChargeTimer.cancel();
                jumpChargeAmount = 0;
                jumpCountDown = 5;
            }
        }
    }

    @Override
    Drawable getGraphics() {
        return (g2)->{
            g2.setColor(hud.darkHudColor);
            g2.fillRect(x, y, width, height);

            g2.setColor(hud.opaqueHudColor);
            g2.drawRect(x, y, width, height);

            headings(g2);

            if(headingOption1) {
                jumpdriveMenu(g2);
            } else {
                missionMenu(g2);
            }

            if(hud.player.chargingJump) {
                jumpdriveDisplay(g2);
            }
        };
    }

    private void headings(Graphics2D g2) {
        g2.setColor(hud.lightHudColor);
        if(headingOption1) {
            g2.fillRect(x, y, width/2, height/8);
        } else {
            g2.fillRect(x+width/2, y, width/2, height/8);
        }

        g2.setColor(hud.opaqueHudColor);
        g2.drawRect(x, y, width/2, height/8);
        g2.setColor(hud.textColor);
        g2.drawString("Jumpdrive locations", x+width/30, y+height/11);

        g2.setColor(hud.opaqueHudColor);
        g2.drawRect(x+width/2, y, width/2, height/8);
        g2.setColor(hud.textColor);
        g2.drawString("Current bounty", x+width/2+width/13, y+height/11);
    }

    private void missionMenu(Graphics2D g2) {
        g2.setColor(hud.opaqueHudColor);
        g2.drawRect(x, y, width, (height/7)*2);
        g2.setColor(hud.textColor);
        g2.drawString(hud.player.currentMission.name,x+width/30, (y+height/5)+(height/30));

        ObjectList<String> description = hud.player.currentMission.fixDescription(36, true);
        int yIndex = (y+height/5)+(height/5);
        for(String string : description) {
            g2.drawString(string, x+width/30, yIndex);
            yIndex += g2.getFont().getSize()+(g2.getFont().getSize()/2);
        }

        if(hud.player.currentMission.getClass() == Bounty.class) {
            Bounty bounty = (Bounty)hud.player.currentMission;
            g2.drawString("System: "+bounty.systemID.toString(), x+width/30, y+height-((height/25)*5));
        }

        if(!(hud.player.currentMission.getClass() == Empty.class)) {
            g2.setColor(hud.opaqueHudColor);
            g2.drawRect(x, y+height-(height/7), width, height/7);
            g2.setColor(hud.textColor);
            g2.drawString("Reward: "+hud.player.currentMission.reward, x+width/30, y+height-(height/25));
            if(!(hud.player.currentMission.getClass() == Tutorial.class)) {
                g2.setColor(hud.opaqueHudColor);
                g2.drawRect(x+width/2, y+height-(height/7), width/2, height/7);
                g2.setColor(hud.textColor);
                Bounty bounty = (Bounty)hud.player.currentMission;
                g2.drawString("Ship count: "+ bounty.shipCount, x+width/2+g2.getFont().getSize(), y+height-(height/25));
            }
        }
    }

    private void jumpdriveMenu(Graphics2D g2) {
        int pointerX = x+width/30;
        if(!leftColumn) pointerX += width/2;

        int pointerY = y+height/5+((height/5)*row);

        g2.setColor(hud.lightHudColor);
        g2.fillRect(pointerX, pointerY, width/2-width/20, height/7);

        g2.setColor(hud.textColor);
        int gap = height/5;

        g2.drawString("Sol", x+width/20, y+height/4+height/20);
        g2.drawString("Fortuna", x+width/20, y+height/4+height/20+gap);
        g2.drawString("Nero", x+width/20, y+height/4+height/20+(gap*2));
        if(hud.player.getStat(ComponentID.jumpdrive)[0] == 1) {
            g2.setColor(new Color(169,169,169));
        }
        g2.drawString("Titanus", x+width/20, y+height/4+height/20+(gap*3));
        g2.drawString("Genesis", x+width/2+width/20, y+height/4+height/20);
        g2.drawString("Novis", x+width/2+width/20, y+height/4+height/20+gap);
        if(hud.player.getStat(ComponentID.jumpdrive)[0] == 2) {
            g2.setColor(new Color(169,169,169));
        }
        g2.drawString("Astraeus", x+width/2+width/20, y+height/4+height/20+(gap*2));
        g2.drawString("Nix", x+width/2+width/20, y+height/4+height/20+(gap*3));
    }

    private void jumpdriveDisplay(Graphics2D g2) {
        g2.setStroke(new BasicStroke(5));

        for(int x = 0; x <= jumpChargeAmount; x++) {
            g2.setColor(hud.opaqueHudColor);
            g2.drawLine(-150+(10*x), -150, -150+(10*x), -185);
            g2.drawLine(150-(10*x), -150, 150-(10*x), -185);
        }

        g2.setColor(hud.textColor);
        g2.drawString("CHARGING", -53, -160);
        g2.setStroke(new BasicStroke(3));

        if(canCancel) {
            g2.drawString("Press [ENTER] to cancel", -105, -120);
        } else {
            g2.setColor(Color.red);
            g2.drawString("JUMP LOCKED", -70, -120);
        }

        g2.setColor(Color.red);
        if(jumpCountDown != 5) {
            if(jumpCountDown == 0) {
                g2.drawString("Engaged", -45, -80);
            } else {
                g2.drawString(String.valueOf(jumpCountDown), -1, -80);
            }

        }
    }
}
