package Objects.GameObjects.Player.Missions;

import Init.Game;
import Objects.Utility.*;

public class Tutorial extends Mission {
    private int stage = 1;
    private boolean wPressed = false, sPressed = false;
    private boolean LMBPressed = false, RMBPressed = false;

    public Tutorial() {
        super(
                "Tutorial",
                "Use W to thrust forward and S to thrust backward. The mouse is used to turn. ",
                "Use W to thrust forward and S to thrust backward. The mouse is used to turn. ",
                1000
        );
    }

    @Override
    public void update() {
        switch(stage) {
            case 1:
                stage1();
                break;
            case 2:
                stage2();
                break;
            case 3:
                stage3();
                break;
            case 4:
                stage4();
                break;
        }
    }

    private void stage1() {
        if(KeyHandler.isKeyPressed(Keys.W)) wPressed = true;
        else if (KeyHandler.isKeyPressed(Keys.S)) sPressed = true;

        if(wPressed && sPressed) {
            shortDesc = "Use LMB to take out and fire your weapon, stow your weapon with RMB. ";
            longDesc = shortDesc;
            completeSound.play();
            stage++;
        }
    }

    private void stage2() {
        if(MouseHandler.isMouseClicked(MouseButtons.RMB)) RMBPressed = true;
        if(MouseHandler.isMouseClicked(MouseButtons.LMB)) LMBPressed = true;

        if(RMBPressed && LMBPressed) {
            shortDesc = "Use the map at the bottom of the screen to fly towards the white blip. ";
            longDesc = shortDesc;
            completeSound.play();
            stage++;
        }
    }

    private void stage3() {
        if(Game.getInstance().player.canDock) {
            shortDesc = "This is a station. When close, press [HOME] to dock. Then use the mission board to " +
                    "complete the mission. ";
            longDesc = shortDesc;
            completeSound.play();
            stage++;
        }

    }
    private void stage4() {
        if(Game.getInstance().player.docked) {
            shortDesc = "Click the complete button to receive your reward. ";
            longDesc = shortDesc;
            complete = true;
            completeSound.play();
        }
    }
}
