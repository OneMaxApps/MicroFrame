package microframe.util;

import java.awt.BasicStroke;
import java.awt.Color;

public final class Style {
	private static Color stroke, fill;
	private static BasicStroke strokeWeight;
	
	static {
		stroke = ColorPool.getColor(0, 0, 0, 255);
		fill = ColorPool.getColor(200, 200, 200, 255);
		strokeWeight = new BasicStroke(1);
	}
	
	private Style() {
		
	}

	public static final Color getStroke() {
		return stroke;
	}

	public static final void setStroke(Color stroke) {
		Style.stroke = stroke;
	}

	public static final Color getFill() {
		return fill;
	}

	public static final void setFill(Color fill) {
		Style.fill = fill;
	}

	public static final BasicStroke getStrokeWeight() {
		return strokeWeight;
	}

	public static final void setStrokeWeight(BasicStroke strokeWeight) {
		Style.strokeWeight = strokeWeight;
	}
	
}