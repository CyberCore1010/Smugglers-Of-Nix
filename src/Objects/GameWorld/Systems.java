package Objects.GameWorld;

import Objects.GameObjects.GameObject;
import Objects.Utility.ObjectList;

public class Systems {
    public ObjectList<GameObject> entities;

    Systems(SystemID system){
        entities = system.getInitial();
    }
}
