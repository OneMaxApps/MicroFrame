package launcher;

import microframe.core.MicroFrame;

public class Launcher extends MicroFrame {
	
	public static void main(String[] args) {
		new Launcher();
	}

	@Override
	public void onCreate() {
		setTitle("Launcher");
		setFrameRate(60);
		setSize(400,400);
	}

	@Override
	public void onRender() {
		stroke(200,0,200);
		fill(255,0,0);
		
		
		
		oval(getMouseX(),getMouseY(),100,100);
	}

	@Override
	public void onQuit() {
		
	}
	
}