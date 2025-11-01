package launcher;

import static microframe.util.MathUtils.convert;

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
		setWindowSize(400,400);
		
		setFrameRate(60);

		setResizeEnabled(true);
	}

	@Override
	public void onRender() {
		background(Color.BLACK);
		
		for(int x = 0; x < getWidth(); x++) {
			for(int y = 0; y < getHeight(); y++) {
				stroke((int) convert(x, 0, getWidth(), 0,255),(int) convert(x, 0, getWidth(), 0,255),(int) convert(y, 0, getHeight(), 0,255));
				point(x,y);
			}
		}

		
	}

	@Override
	public void onQuit() {
		
	}
	
}