package Objects.GameObjects.Player.Components;

public abstract class Component {
    float[] stat = new float[3];

    Component() {
        stat[0] = 0;
        stat[1] = 0;
        stat[2] = 0;
    }

    public float[] getStat() {
        return stat;
    }
}
