package Objects.GameObjects;

import Init.CameraID;
import Init.Game;
import Init.Window;
import Objects.GameObjects.Player.Player;
import Objects.GameObjects.Properties.Drawable;
import Objects.GameWorld.SystemID;
import Objects.Utility.BufferedImageLoader;
import Objects.Utility.ObjectList;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Background extends GameObject {
    private Player player;
    private BufferedImage solBack, fortunaBack, neroBack, titanusBack, genesisBack, novisBack;
    private BufferedImage debris;

    public Background(Player player) {
        super(ObjectID.NA);
        this.player = player;

        BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
        solBack = bufferedImageLoader.loadImage("/Backgrounds/solBack.png");
        fortunaBack = bufferedImageLoader.loadImage("/Backgrounds/fortunaBack.png");
        neroBack = bufferedImageLoader.loadImage("/Backgrounds/neroBack.png");

        titanusBack = bufferedImageLoader.loadImage("/Backgrounds/titanusBack.png");
        genesisBack = bufferedImageLoader.loadImage("/Backgrounds/genesisBack.png");
        novisBack = bufferedImageLoader.loadImage("/Backgrounds/novisBack.png");

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
            } else if(player.getCurrentLocation() == SystemID.Nero) {
                back = neroBack;
            } else if(player.getCurrentLocation() == SystemID.Titanus) {
                back = titanusBack;
            } else if(player.getCurrentLocation() == SystemID.Genesis) {
                back = genesisBack;
            } else if(player.getCurrentLocation() == SystemID.Novis) {
                back = novisBack;
            }
            else {
                back = solBack;
            }
            g.setPaint(new TexturePaint(back, new Rectangle2D.Double(-player.position.x/100, -player.position.y/100, Window.gameWidth, Window.gameHeight)));
            g.fillRect(-Window.gameWidth/2, -Window.gameHeight/2, Init.Window.gameWidth*2, Window.gameHeight*2);
            //g.drawImage(back, -Window.gameWidth/2, -Window.gameHeight/2, Init.Window.gameWidth, Window.gameHeight, null);
        };
        Drawable movingBack = (g)-> {
            g.setPaint(new TexturePaint(debris, new Rectangle2D.Double(0, 0, Window.gameWidth, Window.gameHeight)));
            g.fillRect((int)player.midPos.x-Window.gameWidth, (int)player.midPos.y-Window.gameHeight, Window.gameWidth*2, Window.gameHeight*2);
        };

        renderToCamera(staticBack, g2d , Game.getInstance().cameraMap.get(CameraID.screen));
        renderToCamera(movingBack, g2d , Game.getInstance().cameraMap.get(CameraID.game));
    }

    @Override
    public Rectangle2D getSquareBounds() {
        return null;
    }

    @Override
    public ObjectList<Line2D> getPolyBounds() {
        return null;
    }
}
