package launcher;

import microframe.core.MicroFrame;
import microframe.graphics.Image;

public class Launcher extends MicroFrame {
	
	Image img;
	
	public static void main(String[] args) {
		new Launcher();
	}

	@Override
	public void onCreate() {
		setWindowTitle("Launcher");
		setWindowSize(400,400);
		setFrameRate(60);
		
		img = loadImage("C:\\Users\\002\\Desktop\\i.jpg");
	}

	@Override
	public void onRender() {
		background(0);
		fill(255);
		setTextSize(24);
		text("FPS: "+getFrameRate(),4,24);
		
		fill(255,0,0);
		image(img,getMouseX(),getMouseY());
	}

	@Override
	public void onQuit() {
		
	}
	
}