package Objects.GameObjects.Player.Missions;

import Objects.Utility.ObjectList;
import Objects.Utility.SFXPlayer;

public abstract class Mission {
    public String name, shortDesc, longDesc;
    public int reward;
    public boolean complete = false;
    SFXPlayer completeSound = new SFXPlayer("res/SFX/UI/Click 2.wav", false);

    Mission(String name, String shortDesc, String longDesc, int reward) {
        this.name = name;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.reward = reward;
    }

    public abstract void update();

    public ObjectList<String> fixDescription(int lineLen, boolean shorter) {
        ObjectList<String> returnList = new ObjectList<>();

        int characterCount = 0;
        String description;
        if(shorter) {
            description = shortDesc;
        } else {
            description = longDesc;
        }

        StringBuilder currentWord = new StringBuilder();
        StringBuilder currentLine = new StringBuilder();

        for(int i = 0; i < description.length(); i++) {
            currentWord.append(description.substring(i, i + 1));
            if(description.substring(i, i+1).equals(" ")) {
                characterCount += currentWord.length();
                if(characterCount-1 >= lineLen) {
                    characterCount = 0;
                    returnList.add(currentLine.toString());
                    currentLine = new StringBuilder();
                }
                currentLine.append(currentWord);
                currentWord = new StringBuilder();
            }
        }
        returnList.add(currentLine.toString());
        return returnList;
    }
}
