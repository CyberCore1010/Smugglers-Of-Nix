package Objects.GameObjects.Player.Components;

public class Hull extends Component{
    public Hull(Level level) {
        switch(level) {
            case basic:
                stat = 100;
                break;
            case advanced:
                stat = 250;
                break;
            case elite:
                stat = 500;
                break;
        }
    }
}
