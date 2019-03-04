package Objects.GameObjects.Player.HUD.StarPortOS;

import Objects.GameObjects.Properties.Drawable;

public abstract class Tab {
    int x, y, width, height;

    public abstract void update();
    public abstract Drawable getGraphics();
}
