package Init;

import Objects.GameObjects.Background;
import Objects.GameObjects.GameObject;
import Objects.GameObjects.ObjectID;
import Objects.GameObjects.Player.Player;
import Objects.GameObjects.Station;
import Objects.GameWorld.Universe;
import Objects.Utility.*;

import javax.swing.*;
import java.awt.*;

public class Game extends JComponent {
    public ObjectMap<CameraID, Camera> cameraMap;

    private boolean isRunning = true;
    private Thread thread;

    public Window window;
    private static Game game;
    public boolean restart = false;

    public Universe universe;
    public Player player;
    public ObjectList<GameObject> handler;
    private MusicHandler musicHandler;

    private Game() {
        cameraMap = new ObjectMap<>();
        cameraMap.put(CameraID.game, new Camera(0, 0, 1, Window.gameWidth, Window.gameHeight));
        cameraMap.put(CameraID.screen, new Camera(0, 0, 1, Window.gameWidth, Window.gameHeight));

        universe = new Universe();
        player = new Player(0, 0, Window.gameWidth/20, Window.gameWidth/20);

        handler = new ObjectList<>();
        handler.add(new Background(player));
        handler.addAll(universe.systems.get(player.getCurrentLocation()).entities);
        handler.add(player);

        musicHandler = new MusicHandler();

        thread = new Thread(this::start);
        thread.start();
    }

    private void constructNewGame() {
        for(Objects.GameWorld.System system: universe.systems.values()) {
            for(GameObject object : system.entities) {
                if(object.id != ObjectID.station) {
                    system.entities.remove(object);
                }
            }
        }
        Station station = null;
        for(GameObject object : handler) {
            if(object.id == ObjectID.station) {
                station = (Station)object;
            }
        }
        assert station != null;
        player = new Player((int)station.midPos.x-((Window.gameWidth/20)/2)-1, (int)station.midPos.y-((Window.gameWidth/20)/2)-1, Window.gameWidth/20, Window.gameWidth/20);

        handler.clear();
        handler.add(new Background(player));
        handler.addAll(universe.systems.get(player.getCurrentLocation()).entities);
        handler.add(player);
        KeyHandler.forceKey(Keys.home, true);
        player.update();
        KeyHandler.forceKey(Keys.home, false);
        restart = false;
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
        if(!handler.isEmpty()) {
            ObjectList<GameObject> temp = new ObjectList<>();
            temp.add(handler.get(0));
            temp.addAll(universe.systems.get(player.getCurrentLocation()).entities);
            temp.add(player);
            handler = temp;
        }
    }

    private void update() {
        if(restart) {
            constructNewGame();
        }
        for(GameObject object : handler) {
            object.update();
        }
        musicHandler.update();
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
        Graphics2D g2d = (Graphics2D) g;

        if(!restart) {
            super.paintComponent(g);
            ////////DRAWING AREA////////

            for(GameObject object : handler) {
                object.render(g2d);
            }

            ////////MENU DRAWING////////
        }

        g2d.dispose();
        g.dispose();
    }
}
