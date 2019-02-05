package Objects.Utility;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyHandler extends KeyAdapter {

    private static Map<Keys, Boolean> keyMap;

    public KeyHandler() {
        keyMap = new HashMap<>();
        keyMap.put(Keys.W, false);
        keyMap.put(Keys.A, false);
        keyMap.put(Keys.S, false);
        keyMap.put(Keys.D, false);
    }

    public static boolean isKeyPressed(Keys key) {
        if(keyMap != null) {
            return keyMap.get(key);
        } else {
            return false;
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_W) {
            keyMap.replace(Keys.W, true);
        }
        if(key == KeyEvent.VK_A) {
            keyMap.replace(Keys.A, true);
        }
        if(key == KeyEvent.VK_S) {
            keyMap.replace(Keys.S, true);
        }
        if(key == KeyEvent.VK_D) {
            keyMap.replace(Keys.D, true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_W) {
            keyMap.replace(Keys.W, false);
        }
        if(key == KeyEvent.VK_A) {
            keyMap.replace(Keys.A, false);
        }
        if(key == KeyEvent.VK_S) {
            keyMap.replace(Keys.S, false);
        }
        if(key == KeyEvent.VK_D) {
            keyMap.replace(Keys.D, false);
        }
    }
}
