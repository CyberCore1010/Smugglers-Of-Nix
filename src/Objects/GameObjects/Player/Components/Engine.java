package Objects.GameObjects.Player.Components;

public class Engine extends Component{
    public Engine(Level level) {
        switch(level) {
            case basic:
                stat = 1;
                break;
            case advanced:
                stat = 2;
                break;
            case elite:
                stat = 4;
                break;
        }
    }
}
