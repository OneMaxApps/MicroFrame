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
		
		setFrameRate(60);
		
		
	}

	@Override
	public void onRender() {
		background(0,1);
		
		stroke(255,0,0);
		point(getMouseX(),getMouseY());
		
		System.out.println(getMouseX() + " : " + getMouseY());
	}

	@Override
	public void onQuit() {
		
	}
	
	@Override
	public void onMousePressed() {
		setWindowSize(getWidth()+10,getHeight());

	}
	
}