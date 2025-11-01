package launcher;

import java.awt.Color;

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
		stroke(Color.RED);
		background(Color.BLACK);
		
		line(100,100,300,300);
		
		System.out.println(getMouseX() + " : " + getMouseY());
	}

	@Override
	public void onQuit() {
		
	}
	
}