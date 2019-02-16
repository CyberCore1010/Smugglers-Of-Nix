package Init;

import Objects.GameObjects.Background;
import Objects.GameObjects.GameObject;
import Objects.GameObjects.Player.Player;
import Objects.GameWorld.Universe;
import Objects.Utility.ObjectList;
import Objects.Utility.ObjectMap;

import javax.swing.*;
import java.awt.*;

public class Game extends JComponent {
    public ObjectMap<CameraID, Camera> cameraMap;

    private boolean isRunning = true;
    private Thread thread;

    public Window window;
    private static Game game;

    //testing
    public Player player;
    private Universe universe;
    public ObjectList<GameObject> handler;

    private Game() {
        cameraMap = new ObjectMap<>();
        cameraMap.put(CameraID.game, new Camera(0, 0, 1, Window.gameWidth, Window.gameHeight));
        cameraMap.put(CameraID.screen, new Camera(0, 0, 1, Window.gameWidth, Window.gameHeight));

        player = new Player(0, 0, Window.gameWidth/20, Window.gameWidth/20);
        universe = new Universe();

        handler = new ObjectList<>();
        handler.add(new Background(player));
        handler.add(player);
        handler.addAll(universe.systems.get(player.getCurrentLocation()).entities);

        thread = new Thread(this::start);
        thread.start();
    }

    public static Game getInstance() {
        if(game == null) {
            game = new Game();
        }
        return game;
    }

    public void giveWindow(Window window){
        this.window = window;
    }

    private void start() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        while(isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                update();
                delta--;
            }
            repaint();

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }
        stop();
    }

    public void rebuildHandler() {
        ObjectList<GameObject> temp = new ObjectList<>();
        temp.add(player);
        temp.addAll(universe.systems.get(player.getCurrentLocation()).entities);
        handler = temp;
    }

    private void update() {
        for(GameObject object : handler) {
            object.update();
        }
    }

    private void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        ////////DRAWING AREA////////

        for(GameObject object : handler) {
            object.render(g2d);
        }

        ////////MENU DRAWING////////
        g2d.dispose();
        g.dispose();
    }
}
