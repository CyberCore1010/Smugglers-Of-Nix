package Objects.GameObjects.Player.Components;

public class Hull extends Component{
    public Hull(Level level) {
        switch(level) {
            case basic:
                stat[0] = 100;
                break;
            case advanced:
                stat[0] = 250;
                break;
            case elite:
                stat[0] = 500;
                break;
        }
    }
}
