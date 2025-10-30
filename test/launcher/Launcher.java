package launcher;

import java.awt.Color;

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
		
		stroke(Color.CYAN);
		rect(getMouseX(),getMouseY(),100,100);
		
		rect(200,200,100,100);
		
	}
	
}