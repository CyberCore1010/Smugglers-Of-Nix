package Objects.Utility;

import Init.Game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyHandler extends KeyAdapter {

    private static Map<Keys, Boolean> keyMap;

    public KeyHandler() {
        keyMap = new HashMap<>();
        //Movement
        keyMap.put(Keys.W, false);
        keyMap.put(Keys.A, false);
        keyMap.put(Keys.S, false);
        keyMap.put(Keys.D, false);

        //Panel
        keyMap.put(Keys.num4, false);
        keyMap.put(Keys.num5, false);
        keyMap.put(Keys.num6, false);
        keyMap.put(Keys.num7, false);
        keyMap.put(Keys.num8, false);
        keyMap.put(Keys.num9, false);
        keyMap.put(Keys.enter, false);
    }

    public static boolean isKeyPressed(Keys key) {
        if(key == Keys.any) {
            for(Boolean value : keyMap.values()) {
                if(value) return true;
            }
        }
        if(keyMap != null) {
            return keyMap.get(key);
        } else {
            return false;
        }

    }

    public static void forceKey(Keys key, boolean forcedBool) {
        keyMap.replace(key, forcedBool);
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
        if(key == KeyEvent.VK_NUMPAD4) {
            keyMap.replace(Keys.num4, true);
        }
        if(key == KeyEvent.VK_NUMPAD5) {
            keyMap.replace(Keys.num5, true);
        }
        if(key == KeyEvent.VK_NUMPAD6) {
            keyMap.replace(Keys.num6, true);
        }
        if(key == KeyEvent.VK_NUMPAD7) {
            keyMap.replace(Keys.num7, true);
        }
        if(key == KeyEvent.VK_NUMPAD8) {
            keyMap.replace(Keys.num8, true);
        }
        if(key == KeyEvent.VK_NUMPAD9) {
            keyMap.replace(Keys.num9, true);
        }
        if(key == KeyEvent.VK_ENTER) {
            keyMap.replace(Keys.enter, true);
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
        if(key == KeyEvent.VK_NUMPAD4) {
            keyMap.replace(Keys.num4, false);
        }
        if(key == KeyEvent.VK_NUMPAD5) {
            keyMap.replace(Keys.num5, false);
        }
        if(key == KeyEvent.VK_NUMPAD6) {
            keyMap.replace(Keys.num6, false);
        }
        if(key == KeyEvent.VK_NUMPAD7) {
            keyMap.replace(Keys.num7, false);
        }
        if(key == KeyEvent.VK_NUMPAD8) {
            keyMap.replace(Keys.num8, false);
        }
        if(key == KeyEvent.VK_NUMPAD9) {
            keyMap.replace(Keys.num9, false);
        }
        if(key == KeyEvent.VK_ENTER) {
            keyMap.replace(Keys.enter, false);
        }
    }
}
