package Objects.GameObjects.Player.Missions;

import Init.Game;
import Objects.GameObjects.GameObject;
import Objects.GameObjects.NPC.Enemy.Enemy;
import Objects.GameObjects.ObjectID;
import Objects.GameObjects.Player.Components.ComponentID;
import Objects.GameWorld.SystemID;
import Objects.GameWorld.System;
import Objects.Utility.Maths.Maths;

public class Bounty extends Mission {
    public int shipCount;
    public SystemID systemID;
    private System system;
    private boolean spawned = false;

    public Bounty(String name, String shortDesc, String longDesc, int reward, int shipCount, SystemID systemID) {
        super("Bounty: "+name, shortDesc, longDesc, reward);

        this.shipCount = shipCount;
        this.systemID = systemID;
        system = Game.getInstance().universe.systems.get(systemID);
    }

    @Override
    public void update() {
        if(!spawned) {
            for(int i = 0; i < shipCount; i++) {
                system.addEntity(new Enemy(systemID));
            }
            spawned = true;
        }

        int currentCount = 0;
        for(GameObject object : system.entities) {
            if(object.id == ObjectID.enemy) {
                currentCount++;
            }
        }
        shipCount = currentCount;

        if(shipCount <= 0) {
            if(!complete) {
                completeSound.play();
            }
            complete = true;
            shortDesc = "Return to station to hand in bounty. ";
        }
    }

    public static Bounty randomBounty(int level) {
        level = (int)Maths.clamped(1, (double)Game.getInstance().player.getStat(ComponentID.jumpdrive)[0], level);

        SystemID system = getRandomSystem(level);
        String[] text = getMissionText(system, level);

        return new Bounty(text[0], text[1], text[2], (int)((Math.random() * 501) + 500)*level, level, system);
    }

    private static SystemID getRandomSystem(int level) {
        int temp = (int)Math.ceil(Math.random() * 3);
        level = (int)Maths.clamped(1, temp, level);
        int systemInt = (int)Math.ceil(Math.random() * 3);

        switch(level) {
            case 1:
                switch(systemInt) {
                    case 1:
                        return SystemID.Sol;
                    case 2:
                        return SystemID.Fortuna;
                    case 3:
                        return SystemID.Nero;
                }
                break;
            case 2:
                switch(systemInt) {
                    case 1:
                        return SystemID.Titanus;
                    case 2:
                        return SystemID.Genesis;
                    case 3:
                        return SystemID.Novis;
                }
                break;
            case 3:
                switch(systemInt) {
                    case 1:
                        return SystemID.Astraeus;
                    case 2:
                        return SystemID.Nyx;
                    case 3:
                        return SystemID.GrimHex;
                }
                break;
        }
        return SystemID.Sol;
    }

    private static String[] getMissionText(SystemID system, int level) {
        String[] returnString = new String[3];
        int random = (int)Math.ceil(Math.random() * 3);

        switch(system) {
            case Sol:
                returnString[1] = "Defeat "+level+" ships in Sol ";
                switch(random) {
                    case 1:
                        returnString[0] = "Sol survivor";
                        if(level > 1) {
                            returnString[2] = "A wing of pirates have been spotted in the Sol system. They could potentially be dangerous " +
                                    "and should be dealt with using extreme caution ";
                        } else {
                            returnString[2] = "A pirate has been spotted in the Sol system. They could potentially be dangerous " +
                                    "and should be dealt with using extreme caution ";
                        }
                        break;
                    case 2:
                        returnString[0] = "Stand and Deliver";
                        if(level > 1) {
                            returnString[2] = "Some pirates have been spotted in the Sol system. They have been robbing " +
                                    "civilian causers and are affecting our income. ";
                        } else {
                            returnString[2] = "A pirate have been spotted in the Sol system. He has been robbing " +
                                    "civilian causers and is affecting our income. ";
                        }

                        break;
                    case 3:
                        returnString[0] = "Major Tom";
                        if(level > 1) {
                            returnString[1] = "Defeat Lieutenant Commander Tom and "+(level-1)+" ships in Sol ";
                            returnString[2] = "Lieutenant Commander Tom is a military commander who recently went AWOL. " +
                                    "We believe that he is planning an attack on the Sol station. Take him out immediately. " +
                                    "Be advised he may have other ships under his wing. ";
                        } else {
                            returnString[1] = "Defeat Lieutenant Commander Tom in Sol ";
                            returnString[2] = "Lieutenant Commander Tom is a military commander who recently went AWOL. " +
                                    "We believe that he is planning an attack on the Sol station. Take him out immediately ";
                        }
                        break;
                }
                return returnString;
            case Fortuna:
                returnString[1] = "Defeat "+level+" ships in Fortuna ";
                switch(random) {
                    case 1:
                        returnString[0] = "Out of luck";
                        returnString[1] = "Defeat 11111111 ships in Fortuna ";
                        returnString[2] = "Our scanners seem to be finding eleven million one hundred eleven thousand " +
                                "one hundred and eleven ships near Fortuna space station. Either they're having a busy " +
                                "day or something is up with our scanners. Check it out for us. ";
                        break;
                    case 2:
                        returnString[0] = "Dark side of the Moon";
                        returnString[2] = "A group of cultists have been reported to have started operating on Fortuna's " +
                                "moon 'Umbriel'. We beg you to try and stop them from sacrificing anyone. ";
                        break;
                    case 3:
                        returnString[0] = "Cause and Effect";
                        returnString[2] = "We have some, as you would say... pests, we need to have dealt with. We're " +
                                "willing to reward you handsomely for your work. ";
                        break;
                }
                return returnString;
            case Nero:
                returnString[1] = "Defeat "+level+" ships in Nero ";
                switch(random) {
                    case 1:
                        returnString[0] = "The Matrix is a system, Nero";
                        if(level > 1) {
                            returnString[2] = "A wing of military grade AI ships were hacked recently. We can't risk " +
                                    "this technology getting into the wrong hands, take them out as quickly as possible. ";
                        } else {
                            returnString[2] = "A military grade AI ship was hacked recently. We can't risk " +
                                    "this technology getting into the wrong hands, take it out as quickly as possible. ";
                        }
                        break;
                    case 2:
                        returnString[0] = "Skidrow";
                        returnString[2] = "Pirates have created a business selling stolen goods in the Nero system. Take " +
                                "care of them. ";
                        break;
                    case 3:
                        returnString[0] = "Life on mars";
                        returnString[2] = "We've been getting reports of lawmen beating up the wrong guys, we believe " +
                                "this may be the work of foul play. See if you can resolve this issue. ";
                        break;
                }
                return returnString;
            case Titanus:
                break;
            case Genesis:
                break;
            case Novis:
                break;
            case Astraeus:
                break;
            case Nyx:
                break;
            case GrimHex:
                break;
        }

        returnString[0] = "Broken";
        returnString[1] = "Broken";
        returnString[2] = "Broken";
        return returnString;
    }
}
