package Objects.GameObjects.Player.Components;

public class Shield extends Component {

    public Shield(Level level) {
        switch(level) {
            case basic:
                stat[0] = 10;
                stat[1] = 500;
                break;
            case advanced:
                stat[0] = 20;
                stat[1] = 250;
                break;
            case elite:
                stat[0] = 30;
                stat[1] = 100;
                break;
        }
    }
}
