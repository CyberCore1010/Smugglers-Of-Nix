package Objects.GameObjects.Player.Components;

public class Shield extends Component {

    public Shield(Level level) {
        switch(level) {
            case basic:
                stat = 2;
                break;
            case advanced:
                stat = 5;
                break;
            case elite:
                stat = 10;
                break;
        }
    }
}
