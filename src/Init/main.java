package Init;

public class main {
    /**This is simply the main method. It creates the handler and sends it to the game. It also adds the game to a new
     * Tab object and runs LevelSelect's selectLevel method to set up the level.
     *
     * @param args - Not used
     */
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");

        Game game = Game.getInstance();
        Window window = new Window(game, "Smugglers Of Nix");
        game.giveWindow(window);
    }
}
