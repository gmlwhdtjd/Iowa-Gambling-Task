package IGT;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundClip {
	
	private Clip clip;
	
	public SoundClip(final String url) {
		try {
			InputStream iStream = getClass().getResourceAsStream(url);
			InputStream bufferedIn = new BufferedInputStream(iStream);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
			
			clip = AudioSystem.getClip(); 
			clip.open(audioInputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void play() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				clip.setFramePosition(0);
				clip.start();
			}
		}).start();
	}
}