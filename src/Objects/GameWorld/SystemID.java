package Objects.GameWorld;

import Objects.GameObjects.GameObject;
import Objects.Utility.ObjectList;

public enum SystemID {
    //Close locations
    Sol(()->{
        ObjectList<GameObject> objects = new ObjectList<>();

        return objects;
    }),
    Fortuna(()->{
        ObjectList<GameObject> objects = new ObjectList<>();

        return objects;
    }),
    Nero(()->{
        ObjectList<GameObject> objects = new ObjectList<>();

        return objects;
    }),

    //Mid-game locations
    Titanus(()->{
        ObjectList<GameObject> objects = new ObjectList<>();

        return objects;
    }),
    Genesis(()->{
        ObjectList<GameObject> objects = new ObjectList<>();

        return objects;
    }),
    Novis(()->{
        ObjectList<GameObject> objects = new ObjectList<>();

        return objects;
    }),

    //Distant locations
    Astraeus(()->{
        ObjectList<GameObject> objects = new ObjectList<>();

        return objects;
    }),
    Nyx(()->{
        ObjectList<GameObject> objects = new ObjectList<>();

        return objects;
    }),
    GrimHex(()->{
        ObjectList<GameObject> objects = new ObjectList<>();

        return objects;
    });

    private Initialise initialise;
    SystemID(Initialise initialise) {
        this.initialise = initialise;
    }

    public ObjectList<GameObject> getInitial() {
        return initialise.create();
    }
}
