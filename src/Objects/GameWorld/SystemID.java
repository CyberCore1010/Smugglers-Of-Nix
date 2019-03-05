package Objects.GameWorld;

import Init.Window;
import Objects.GameObjects.GameObject;
import Objects.GameObjects.Station;
import Objects.Utility.ObjectList;

public enum SystemID {
    //Close locations
    Sol(()->{
        ObjectList<GameObject> objects = new ObjectList<>();
        objects.add(new Station(-Window.gameWidth/4, Window.gameHeight, Window.gameWidth/2, Window.gameWidth/2, "Sol"));
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

    private ObjectList<GameObject> objects;
    private Initialise initialise;
    public int distance;
    SystemID(Initialise initialise) {
        this.initialise = initialise;
    }

    public ObjectList<GameObject> getInitial() {
        if(this == SystemID.Sol || this == SystemID.Fortuna || this == SystemID.Nero) {
            distance = 1;
        } else if(this == SystemID.Titanus || this == SystemID.Genesis || this == SystemID.Novis) {
            distance = 2;
        } else {
            distance = 3;
        }

        if(objects == null) {
            objects = initialise.create();
        }
        return objects;
    }
}
