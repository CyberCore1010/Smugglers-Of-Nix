package Objects.GameObjects.Player.Missions;

public class Empty extends Mission {
    public Empty() {
        super("No mission", "Go to a station to receive a mission ", "Select a mission ", 0);
    }

    @Override
    public void update() {

    }
}
