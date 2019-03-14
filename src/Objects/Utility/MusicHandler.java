package Objects.Utility;

import Init.Game;
import Objects.GameObjects.GameObject;
import Objects.GameObjects.NPC.Enemy.Enemy;
import Objects.GameObjects.ObjectID;

public class MusicHandler {
    private SFXPlayer fightMusic;
    private float volume, maxVolume;

    public MusicHandler() {
        fightMusic = new SFXPlayer("res/Music/BattleMusic.wav", true);
        maxVolume = 0.50f;
        fightMusic.setVolume(maxVolume);
        volume = maxVolume;
    }

    public void update() {
        boolean fightMusicPlaying = false;
        for(GameObject object : Game.getInstance().handler) {
            if(object.id == ObjectID.enemy) {
                Enemy enemy = (Enemy)object;
                if(enemy.agro) {
                    fightMusicPlaying = true;
                }
            }
        }
        if(!fightMusicPlaying) {
            if(volume > 0.01f) {
                volume -= 0.001f;
                fightMusic.setVolume(volume);
            } else {
                if(fightMusic.getClip().isActive()) {
                    fightMusic.stop();
                }
            }
        } else {
            if(!fightMusic.getClip().isActive()) {
                fightMusic.setVolume(maxVolume);
                volume = maxVolume;
                fightMusic.getClip().setFramePosition(0);
                fightMusic.play();
            }
        }
    }
}
