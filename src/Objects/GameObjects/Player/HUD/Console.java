package Objects.GameObjects.Player.HUD;

import Objects.GameObjects.Properties.Drawable;

public abstract class Console {
    HUD hud;
    int x, y, width, height;

    Console(HUD hud) {
        this.hud = hud;
    }

    abstract void update();
    abstract Drawable getGraphics();
    void hoverSound() {
        int index = (int)Math.round(Math.random());
        if(index == 0) {
            hud.hover1.play();
        } else {
            hud.hover2.play();
        }
    }
    void clickSound() {
        int index = (int)Math.round(Math.random());
        if(index == 0) {
            hud.click1.play();
        } else {
            hud.click2.play();
        }
    }
}
