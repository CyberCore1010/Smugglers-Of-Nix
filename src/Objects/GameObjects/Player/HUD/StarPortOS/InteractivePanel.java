package Objects.GameObjects.Player.HUD.StarPortOS;

import Init.Window;
import Objects.GameObjects.Properties.Drawable;
import Objects.GameObjects.Properties.Effect;
import Objects.Utility.MouseButtons;
import Objects.Utility.MouseHandler;
import Objects.Utility.SFXPlayer;

public abstract class InteractivePanel {
    public int x, y ,width, height;
    public boolean hover;
    public Effect effect;

    private SFXPlayer hoverSound1, hoverSound2, clickSound1, clickSound2;
    private Boolean hoverSounded = false, clickSounded = false;

    public InteractivePanel(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        effect = ()->{};
        hover = false;

        hoverSound1 = new SFXPlayer("res/SFX/UI/Hover 1.wav", false);
        hoverSound2 = new SFXPlayer("res/SFX/UI/Hover 2.wav", false);

        clickSound1 = new SFXPlayer("res/SFX/UI/Click 1.wav", false);
        clickSound2 = new SFXPlayer("res/SFX/UI/Click 2.wav", false);
    }

    public void update() {
        hover = true;
        if(Window.getInstance().mousePoint.x > x+Window.gameWidth/2 && Window.getInstance().mousePoint.x < x+Window.gameWidth/2+width) {
            if(Window.getInstance().mousePoint.y > y+Window.gameHeight/2 && Window.getInstance().mousePoint.y < y+Window.gameHeight/2+height) {
                if(!hoverSounded) {
                    hoverSounded = true;
                    int random = (int)Math.round(Math.random());
                    if(random == 0) {
                        hoverSound1.play();
                    } else {
                        hoverSound2.play();
                    }
                }

                if(MouseHandler.isMouseClicked(MouseButtons.LMB)) {
                    if(!clickSounded) {
                        clickSounded = true;
                        int random = (int)Math.round(Math.random());
                        if(random == 1) {
                            clickSound1.play();
                        } else {
                            clickSound2.play();
                        }
                    }
                    effect.activate();
                    MouseHandler.forceMouseClick(MouseButtons.LMB, false);
                } else {
                    clickSounded = false;
                }
            } else {
                hover = false;
                hoverSounded = false;
            }
        } else {
            hover = false;
            hoverSounded = false;
        }
    }

    public abstract Drawable getGraphics();
}
