package Init;

import Objects.Utility.KeyHandler;
import Objects.Utility.Maths.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Window {
    private static Window instance;
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static final int gameWidth = (int)screenSize.getWidth();
    public static final int gameHeight = (int)screenSize.getHeight();

    private KeyHandler keyHandler;
    public Vector2D mousePoint = new Vector2D(0, 0);

    public Window(Component comp, String title) {
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
        keyHandler = new KeyHandler();
        frame.addKeyListener(keyHandler);
        frame.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                Point temp = e.getPoint();
                mousePoint.x = temp.x+(int)Game.getInstance().cameraMap.get(CameraID.game).getX();
                mousePoint.y = temp.y+(int)Game.getInstance().cameraMap.get(CameraID.game).getY();
                }
        });
        instance = this;
    }

    public static Window getInstance() {
        return instance;
    }
}
