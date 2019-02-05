package Objects.GameObjects.Player.Components;

public class Jumpdrive extends Component {
    public Jumpdrive(Level level) {
        switch(level) {
            case basic:
                stat = 1;
                break;
            case advanced:
                stat = 2;
                break;
            case elite:
                stat = 3;
                break;
        }
    }
}
