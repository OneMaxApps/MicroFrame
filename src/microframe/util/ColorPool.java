package microframe.util;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public final class ColorPool {
	private static final Map<Integer, Color> map = new HashMap<Integer, Color>();
	
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
		
		return map.computeIfAbsent(key, k -> new Color(red,green,blue,alpha));
	}
}