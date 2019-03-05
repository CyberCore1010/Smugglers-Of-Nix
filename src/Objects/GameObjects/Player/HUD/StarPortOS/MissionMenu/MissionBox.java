package Objects.GameObjects.Player.HUD.StarPortOS.MissionMenu;

import Init.Game;
import Objects.GameObjects.Player.HUD.StarPortOS.InteractivePanel;
import Objects.GameObjects.Player.Missions.Empty;
import Objects.GameObjects.Player.Missions.Mission;
import Objects.GameObjects.Properties.Drawable;
import Objects.Utility.ObjectList;

import java.awt.*;

public class MissionBox extends InteractivePanel {
    private Mission mission;

    MissionBox(int x, int y, int width, int height, Mission mission) {
        super(x, y, width, height);
        this.mission = mission;

        effect = ()->{
            if(Game.getInstance().player.currentMission.getClass() == Empty.class) {
                Game.getInstance().player.currentMission = mission;
            }
        };
    }

    @Override
    public Drawable getGraphics() {
        return (g2)->{
            g2.setColor(new Color(195, 97, 31));
            if(hover) {
                g2.setColor(Color.white);
            }
            g2.drawRect(x, y, width, height);

            g2.drawLine(x, (int)(y+height/3), x+width, (int)(y+height/3));

            g2.setColor(Color.white);
            g2.drawString(mission.name, x+width/60, y+height/4);

            ObjectList<String> description = mission.fixDescription(130, false);
            int yIndex = y+((height/2));
            for(String string : description) {
                g2.drawString(string, x+width/60, yIndex);
                yIndex += g2.getFont().getSize()+2;
            }

            g2.drawString("Reward: "+mission.reward, x+width/60, y+height-g2.getFont().getSize());
        };
    }
}
