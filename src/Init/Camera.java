package Init;

public class Camera {
    private static float x, y;

    Camera(float x, float y) {
        Camera.x = x;
        Camera.y = y;
    }


    public float getX() {
        return x+Window.gameWidth/2;
    }

    public void setX(float x) {
        Camera.x = x-Window.gameWidth/2;
    }

    public float getY() {
        return y-Window.gameHeight/2;
    }

    public void setY(float y) {
        Camera.y = y+Window.gameHeight/2;
    }
}