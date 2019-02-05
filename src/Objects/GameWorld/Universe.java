package Objects.GameWorld;

import Objects.Utility.ObjectMap;

public class Universe {
    public ObjectMap<SystemID, Systems> systems;

    Universe() {
        systems = new ObjectMap<>();
        //Close locations
        systems.put(SystemID.Sol, new Systems(SystemID.Sol));
        systems.put(SystemID.Fortuna, new Systems(SystemID.Fortuna));
        systems.put(SystemID.Nero, new Systems(SystemID.Nero));

        //Mid-game locations
        systems.put(SystemID.Titanus, new Systems(SystemID.Titanus));
        systems.put(SystemID.Genesis, new Systems(SystemID.Genesis));
        systems.put(SystemID.Novis, new Systems(SystemID.Novis));

        //Distant locations
        systems.put(SystemID.Astraeus, new Systems(SystemID.Astraeus));
        systems.put(SystemID.Nyx, new Systems(SystemID.Nyx));
        systems.put(SystemID.GrimHex, new Systems(SystemID.GrimHex));
    }

    public void update() {

    }

    public void render() {

    }
}
