package microframe.util;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public final class ColorPool {
	private static final Map<Integer, Color> map = new HashMap<Integer, Color>();
	private static final int MIN_VALUE, MAX_VALUE;
	
	static {
		MIN_VALUE = 0;
		MAX_VALUE = 255;
	}
	
	private ColorPool() {
		
	}
	
	public static Color getColor(int red, int green, int blue, int alpha) {
		return createIfAbsent(red,green,blue,alpha);
	}
	
	private static Color createIfAbsent(int red, int green, int blue, int alpha) {
		final int key = ((alpha & 0xFF) << 24) |
				  ((red & 0xFF) << 16) |
				  ((green & 0xFF) << 8) |
				  (blue & 0xFF);
			
		if(map.size() >= 100) {
			map.clear();
		}
		
		final int r = max(MIN_VALUE,min(red,MAX_VALUE));
		final int g = max(MIN_VALUE,min(green,MAX_VALUE));
		final int b = max(MIN_VALUE,min(blue,MAX_VALUE));
		final int a = max(MIN_VALUE,min(alpha,MAX_VALUE));
		
		return map.computeIfAbsent(key, k -> new Color(r,g,b,a));
		
	}
}