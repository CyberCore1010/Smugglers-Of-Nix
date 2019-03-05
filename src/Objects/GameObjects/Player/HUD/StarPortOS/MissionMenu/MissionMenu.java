package Objects.GameObjects.Player.HUD.StarPortOS.MissionMenu;

import Objects.GameObjects.Player.HUD.StarPortOS.Button;
import Objects.GameObjects.Player.HUD.StarPortOS.StarPortOS;
import Objects.GameObjects.Player.HUD.StarPortOS.Tab;
import Objects.GameObjects.Player.Missions.Bounty;
import Objects.GameObjects.Player.Missions.Empty;
import Objects.GameObjects.Player.Missions.Mission;
import Objects.GameObjects.Player.Missions.Tutorial;
import Objects.GameObjects.Player.Player;
import Objects.GameObjects.Properties.Drawable;
import Objects.Utility.ObjectList;

import java.awt.*;

public class MissionMenu extends Tab {
    private Objects.GameObjects.Player.HUD.StarPortOS.InteractivePanel completeButton, abandonButton;
    private ObjectList<MissionBox> avalibleMissions;

    public MissionMenu(StarPortOS starPortOS) {
        this.starPortOS = starPortOS;

        x = starPortOS.x+starPortOS.width/5;
        y = starPortOS.y;
        width = starPortOS.width-starPortOS.width/5;
        height = starPortOS.height;

        completeButton = new Button(x+width-height/5, y+(height/5)/2, height/5, (height/5)/2,
                "Complete",
                ()->{
                    Player player = starPortOS.hud.player;

                    player.credits += player.currentMission.reward;

                    starPortOS.hud.player.currentMission = new Empty();
                }
        );

        abandonButton = new Button(x+width-height/5, y+(height/5)/2, height/5, (height/5)/2,
                "Abandon", ()->starPortOS.hud.player.currentMission = new Empty()
        );

        avalibleMissions = new ObjectList<>();
    }

    @Override
    public void update() {
        if(starPortOS.hud.player.currentMission.complete) {
            completeButton.update();
        } else {
            abandonButton.update();
        }

        for(MissionBox missionBox : avalibleMissions) {
            missionBox.update();
        }
    }

    public void generate() {
        avalibleMissions.clear();

        int offset = width/80;
        int height = this.height/5;

        int level = 0;
        for(int i = 0; i < 4; i++) {
            avalibleMissions.add(new MissionBox(x+width/80, y+height+offset, width-((width/80)*2),
                    (int)(this.height/5.6), Bounty.randomBounty(level)));

            level++;
            offset += this.height/5.6+width/80;
        }
    }

    @Override
    public Drawable getGraphics() {
        return (g2)->{
            drawCurrentMission(g2);
            for(MissionBox missionBox : avalibleMissions) {
                draw(missionBox.getGraphics(), g2);
            }
        };
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void drawCurrentMission(Graphics2D g2) {
        int height = (this.height/5);
        g2.setColor(starPortOS.hud.opaqueHudColor);
        g2.drawRect(x, y, width, height);

        g2.setColor(starPortOS.hud.textColor);
        Mission mission = starPortOS.hud.player.currentMission;
        g2.drawString(mission.name, x+width/80, y+(height/5));

        g2.setColor(starPortOS.hud.opaqueHudColor);
        int lineY = y+((height/3))-(height/30);

        g2.setColor(starPortOS.hud.textColor);
        ObjectList<String> description = mission.fixDescription(100, false);
        int yIndex = y+((height/2));
        for(String string : description) {
            g2.drawString(string, x+width/80, yIndex);
            yIndex += g2.getFont().getSize()+2;
        }

        g2.setColor(starPortOS.hud.opaqueHudColor);
        if(!(mission.getClass() == Empty.class)) {
            g2.drawLine(x, lineY, x+width-height, lineY);
            g2.drawRect(x+width-height, y, height, height);
            g2.drawRect(x+width-height, y+height/2, height, height/2);
            if(!(mission.getClass() == Tutorial.class)) g2.drawRect(x+width-height, y+(height/4), height, height/4);

            g2.setColor(starPortOS.hud.textColor);
            if(mission.getClass() == Bounty.class) {
                Bounty bounty = (Bounty)mission;
                g2.drawString("Reward: "+mission.reward, (x+width-height)+width/80, y+height/8+g2.getFont().getSize()/2);
                g2.drawString("Ship count: "+bounty.shipCount, (x+width-height)+width/80, y+((height/8)*3)+g2.getFont().getSize()/2);
            } else {
                g2.drawString("Reward: "+mission.reward, (x+width-height)+width/80, y+height/4+g2.getFont().getSize()/2);
            }

            if(starPortOS.hud.player.currentMission.complete) {
                draw(completeButton.getGraphics(), g2);
            } else {
                draw(abandonButton.getGraphics(), g2);
            }
        } else {
            g2.drawLine(x, lineY, x+width, lineY);
        }
    }

    private void draw(Drawable drawable, Graphics2D g2) {
        drawable.draw(g2);
    }
}
