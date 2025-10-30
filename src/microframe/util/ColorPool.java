package microframe.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public final class ColorPool {
	private static final List<Color> list = new ArrayList<Color>();
	
	public static Color getColor(int red, int green, int blue, int alpha) {
		return createIfAbsent(red,green,blue,alpha);
	}
	
	private static Color createIfAbsent(int red, int green, int blue, int alpha) {
		
		for(int i = 0; i < list.size(); i++) {
			Color c = list.get(i);
			if(c.getRed() == red && c.getGreen() == green && c.getBlue() == blue && c.getAlpha() == alpha) {
				return c;
			}
		}
		
		Color newColor = new Color(red,green,blue,alpha);
		list.add(newColor);
		return newColor;
	}
}