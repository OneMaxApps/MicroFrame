package launcher;

import microframe.core.MicroFrame;

public class Launcher extends MicroFrame {
	
	public static void main(String[] args) {
		new Launcher();
	}

	@Override
	public void onCreate() {
		setTitle("Launcher");
		setFrameRate(120);
		setSize(400,400);
	}

	@Override
	public void onRender() {
		background(200);
		
		stroke(200,0,200);
		fill(255,0,0);
		
		setTextSize(getMouseX()+1);
		text(getFrameRate(),100,100);
	}

	@Override
	public void onQuit() {
		
	}
	
}