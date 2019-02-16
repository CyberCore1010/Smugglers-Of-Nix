package Objects.GameObjects;

import Init.CameraID;
import Init.Game;
import Init.Window;
import Objects.GameObjects.Player.Player;
import Objects.Utility.BufferedImageLoader;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Background extends GameObject {
    Player player;
    private BufferedImage stars;

    public Background(Player player) {
        super(ObjectID.NA);
        this.player = player;

        BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
        stars = bufferedImageLoader.loadImage("/stars.png");
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2d) {
        Drawable drawable = (g)->{
            g.setPaint(new TexturePaint(stars, new Rectangle2D.Double(player.position.x*-2, player.position.y*-2, Init.Window.gameWidth*2, Init.Window.gameHeight*2)));
            g.fillRect((int)Game.getInstance().cameraMap.get(CameraID.game).getX(), (int)Game.getInstance().cameraMap.get(CameraID.game).getY(), Init.Window.gameWidth, Window.gameHeight);
        };
        renderToCamera(drawable, g2d , Game.getInstance().cameraMap.get(CameraID.game));
    }
}
