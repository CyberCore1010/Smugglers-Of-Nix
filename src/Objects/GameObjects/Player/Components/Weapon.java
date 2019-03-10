package Objects.GameObjects.Player.Components;

public class Weapon extends Component{

    public Weapon(Level level) {
        switch(level) {
            case basic:
                stat[0] = 1;
                break;
            case advanced:
                stat[0] = 5;
                break;
            case elite:
                stat[0] = 10;
                break;
        }
    }
}
