package Objects.GameObjects;

import Init.CameraID;
import Init.Game;
import Objects.GameObjects.Properties.Drawable;

import java.awt.*;

public class Test extends GameObject {

    public Test() {
        super(ObjectID.NA);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2d) {
        Drawable drawable = (g2)->{
            g2.setColor(Color.red);
            g2.fillRect(-100, -100, 200, 200);
        };

        renderToCamera(drawable, g2d, Game.getInstance().cameraMap.get(CameraID.game));
    }
}
