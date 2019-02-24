package Objects.GameWorld;

import Init.Game;
import Objects.GameObjects.GameObject;
import Objects.GameObjects.ObjectID;
import Objects.GameObjects.Player.Player;
import Objects.Utility.ObjectList;

public class Systems {
    public ObjectList<GameObject> entities;
    private SystemID id;

    Systems(SystemID system){
        id = system;
        entities = system.getInitial();
    }

    public void addEntity(GameObject gameObject) {
        entities.add(gameObject);
        for(GameObject object : Game.getInstance().handler) {
            if(object.id == ObjectID.player) {
                if(((Player)object).getCurrentLocation() == id) {
                    Game.getInstance().rebuildHandler();
                }
            }
        }
    }

    public void removeEntity(GameObject gameObject) {
        entities.remove(gameObject);
        Game.getInstance().rebuildHandler();
    }
}
