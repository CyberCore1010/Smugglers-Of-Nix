package Objects.GameObjects.Player.Components;

public class Engine extends Component{
    public Engine(Level level) {
        switch(level) {
            case basic:
                stat[0] = 10;
                stat[1] = 0.03f;
                stat[2] = 0.003f;
                break;
            case advanced:
                stat[0] = 15;
                stat[1] = 0.06f;
                stat[2] = 0.006f;
                break;
            case elite:
                stat[0] = 20;
                stat[1] = 0.09f;
                stat[2] = 0.009f;
                break;
        }
    }
}
