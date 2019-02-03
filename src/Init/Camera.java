package Init;

public class Camera {
    private static float x, y;

    Camera(float x, float y) {
        Camera.x = x;
        Camera.y = y;
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        Camera.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        Camera.y = y;
    }
}