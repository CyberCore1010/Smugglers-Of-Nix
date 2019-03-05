package Objects.GameObjects.Player.HUD.StarPortOS;

import Objects.GameObjects.Properties.Drawable;

public abstract class Tab {
    public int x, y, width, height;
    public StarPortOS starPortOS;

    public abstract void update();
    public abstract Drawable getGraphics();
}
