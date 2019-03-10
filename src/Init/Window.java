package Init;

import Objects.Utility.KeyHandler;
import Objects.Utility.Maths.Vector2D;
import Objects.Utility.MouseHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Window {
    private static Window instance;
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static final int gameWidth = (int)screenSize.getWidth();
    public static final int gameHeight = (int)screenSize.getHeight();

    public Vector2D mousePoint = new Vector2D(0, 0);

    public Window(Component comp, String title) {
        instance = this;
        JFrame frame = new JFrame(title);
        frame.setLocation((int)((screenSize.getWidth()/2)-(gameWidth/2)), (int)((screenSize.getHeight()/2)-(gameHeight/2))-20);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(BorderLayout.CENTER, comp);
        frame.getContentPane().setBackground(Color.black);
        frame.setSize(gameWidth, gameHeight);

        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        frame.addKeyListener(new KeyHandler());
        frame.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point temp = e.getPoint();
                mousePoint.x = temp.x;
                mousePoint.y = temp.y;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Point temp = e.getPoint();
                mousePoint.x = temp.x;
                mousePoint.y = temp.y;
            }
        });
        frame.addMouseListener(new MouseHandler());
        instance = this;
    }

    public Vector2D getMousePoint() {
        return new Vector2D(mousePoint.x+(float)Game.getInstance().cameraMap.get(CameraID.game).getX(), mousePoint.y+(float)Game.getInstance().cameraMap.get(CameraID.game).getY());
    }

    public static Window getInstance() {
        return instance;
    }
}
