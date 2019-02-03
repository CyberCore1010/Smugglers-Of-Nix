package Init;

import javax.swing.*;
import java.awt.*;

public class Game extends JComponent {
    static Camera camera;

    private static boolean isRunning = true;
    private Thread thread;

    Game() {
        camera = new Camera(0, 0);

        thread = new Thread(this::start);
        thread.start();
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

    private void update() {

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

        g2d.translate(-camera.getX(), -camera.getY());

        g2d.translate(0, 0);

        ////////MENU DRAWING////////
        g2d.dispose();
        g.dispose();
    }
}
