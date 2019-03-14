package Objects.Utility;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class SFXPlayer {
    private Clip clip;
    private boolean looping;

    public SFXPlayer(String sound, boolean looping) {
        try {
            this.looping = looping;
            AudioInputStream temp = AudioSystem.getAudioInputStream(new File(sound).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(temp);
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public void setVolume(float volume) {
        FloatControl gainControl = ((FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN));
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    public void play() {
        if(looping) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void stop() {
        clip.stop();
    }

    public Clip getClip() {
        return clip;
    }
}
