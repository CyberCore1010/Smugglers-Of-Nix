package Objects.Utility;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class MouseHandler implements MouseListener {
    private static Map<MouseButtons, Boolean> buttonMap;

    public MouseHandler() {
        buttonMap = new HashMap<>();

        buttonMap.put(MouseButtons.LMB, false);
        buttonMap.put(MouseButtons.RMB, false);
        buttonMap.put(MouseButtons.MMB, false);
    }

    public static boolean isMouseClicked(MouseButtons buttons) {
        if(buttonMap != null) {
            return buttonMap.get(buttons);
        } else {
            return false;
        }
    }

    public static void forceMouseClick(MouseButtons button, boolean forcedBool) {
        buttonMap.replace(button, forcedBool);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            buttonMap.replace(MouseButtons.LMB, true);
        } else if(e.getButton() == MouseEvent.BUTTON2) {
            buttonMap.replace(MouseButtons.MMB, true);
        } else {
            buttonMap.replace(MouseButtons.RMB, true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            buttonMap.replace(MouseButtons.LMB, false);
        } else if(e.getButton() == MouseEvent.BUTTON2) {
            buttonMap.replace(MouseButtons.MMB, false);
        } else {
            buttonMap.replace(MouseButtons.RMB, false);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
