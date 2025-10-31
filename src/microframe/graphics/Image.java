package microframe.graphics;

import static java.util.Objects.requireNonNull;

import java.awt.image.BufferedImage;

public final class Image {
	private final BufferedImage buffer;

	public Image(BufferedImage buffer) {
		super();
		this.buffer = requireNonNull(buffer,"buffer");
	}

	public final BufferedImage getBuffer() {
		return buffer;
	}
	
}