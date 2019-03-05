package Objects.GameWorld;

import Objects.Utility.ObjectMap;

public class Universe {
    public ObjectMap<SystemID, System> systems;

    public Universe() {
        systems = new ObjectMap<>();
        //Close locations
        systems.put(SystemID.Sol, new System(SystemID.Sol));
        systems.put(SystemID.Fortuna, new System(SystemID.Fortuna));
        systems.put(SystemID.Nero, new System(SystemID.Nero));

        //Mid-game locations
        systems.put(SystemID.Titanus, new System(SystemID.Titanus));
        systems.put(SystemID.Genesis, new System(SystemID.Genesis));
        systems.put(SystemID.Novis, new System(SystemID.Novis));

        //Distant locations
        systems.put(SystemID.Astraeus, new System(SystemID.Astraeus));
        systems.put(SystemID.Nyx, new System(SystemID.Nyx));
        systems.put(SystemID.GrimHex, new System(SystemID.GrimHex));
    }
}
