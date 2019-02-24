package Objects.GameObjects.Player.Components;

public class Shield extends Component {

    public Shield(Level level) {
        switch(level) {
            case basic:
                stat[0] = 2;
                break;
            case advanced:
                stat[0] = 4;
                break;
            case elite:
                stat[0] = 8;
                break;
        }
    }
}
