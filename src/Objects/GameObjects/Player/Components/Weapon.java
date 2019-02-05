package Objects.GameObjects.Player.Components;

public class Weapon extends Component{

    public Weapon(Level level) {
        switch(level) {
            case basic:
                stat = 5;
                break;
            case advanced:
                stat = 25;
                break;
            case elite:
                stat = 50;
                break;
        }
    }
}
