package Objects.GameObjects;

import Init.CameraID;
import Init.Game;
import Init.Window;
import Objects.GameObjects.Player.Player;
import Objects.GameObjects.Properties.Drawable;
import Objects.GameWorld.SystemID;
import Objects.Utility.BufferedImageLoader;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Background extends GameObject {
    private Player player;
    private BufferedImage solBack, fortunaBack;
    private BufferedImage debris;

    public Background(Player player) {
        super(ObjectID.NA);
        this.player = player;

        BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
        solBack = bufferedImageLoader.loadImage("/solBack.png");
        fortunaBack = bufferedImageLoader.loadImage("/fortunaBack.png");

        debris = bufferedImageLoader.loadImage("/debris.png");
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2d) {
        Drawable staticBack = (g)->{
            BufferedImage back;
            if(player.getCurrentLocation() == SystemID.Fortuna) {
                back = fortunaBack;
            } else {
                back = solBack;
            }
            g.drawImage(back, -Window.gameWidth/2, -Window.gameHeight/2, Init.Window.gameWidth, Window.gameHeight, null);
        };
        Drawable movingBack = (g)-> {
            g.setPaint(new TexturePaint(debris, new Rectangle2D.Double(0, 0, Window.gameWidth, Window.gameHeight)));
            g.fillRect((int)player.midPos.x-Window.gameWidth/2, (int)player.midPos.y-Window.gameHeight/2, Window.gameWidth, Window.gameHeight);
        };

        renderToCamera(staticBack, g2d , Game.getInstance().cameraMap.get(CameraID.screen));
        renderToCamera(movingBack, g2d , Game.getInstance().cameraMap.get(CameraID.game));
    }
}
