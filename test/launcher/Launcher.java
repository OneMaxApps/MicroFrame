package launcher;

import java.awt.Color;
import java.awt.event.MouseEvent;

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

	}

	@Override
	public void onRender() {
		background(Color.BLACK);
		int w = getWidth();
		int h = getHeight();
		stroke(Color.RED);
		fillOff();
		rect((int) (w*.25f),(int) (h*.25f),(int) (w*.5f),(int) (h*.5f));
		
		
		fill(200);
		setTextSize(20);
		text("FPS: "+getFrameRate(),0,20);
		
		System.out.println(getWidth());
	}

	@Override
	public void onQuit() {
		
	}
	
	@Override
	public void onMousePressed(MouseEvent e) {
		if(e.getButton() == 3) {
			setResizeEnabled(true);
			setWindowSize(500, 500);
		}
	}
}