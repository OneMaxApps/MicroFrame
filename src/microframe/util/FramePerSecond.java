package microframe.util;

public final class FramePerSecond {
	private static double fps;
	private static long lastTime = System.nanoTime();
	private static int frameCount;
	
	
	private FramePerSecond() {
		
	}
	
	public static void update() {
		frameCount++;
		
		long currentTime = System.nanoTime();
		
		if (currentTime - lastTime >= 1_000_000_000L) {
			fps = frameCount;
			frameCount = 0;
			lastTime = currentTime;
		}
	}
	
	public static double getFrameRate() {
		return fps;
	}
	
}