package Objects.GameObjects.Player.Components;

public class Jumpdrive extends Component {
    public Jumpdrive(Level level) {
        switch(level) {
            case basic:
                stat[0] = 1;
                break;
            case advanced:
                stat[0] = 2;
                break;
            case elite:
                stat[0] = 3;
                break;
        }
    }
}
