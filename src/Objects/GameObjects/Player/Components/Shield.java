package Objects.GameObjects.Player.Components;

public class Shield extends Component {

    public Shield(Level level) {
        switch(level) {
            case basic:
                stat[0] = 2;
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
