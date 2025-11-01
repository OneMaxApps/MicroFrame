package microframe.core;

import static java.util.Objects.requireNonNull;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import microframe.graphics.Image;
import microframe.util.ColorPool;
import microframe.util.FramePerSecond;
import microframe.util.Style;

public abstract class MicroFrame {
	private final Frame frame = new Frame("");
	private Canvas canvas;
	private Graphics2D graphics;
	private String title;
	private long lastTime = System.nanoTime();
	private long frameCount,deltaFrameTime;

	private int width,height;
	private int mouseX, mouseY;
	private int lastPressedMouseButton;

	private char pressedKey;
	
	private boolean running;
	private boolean mousePressed;
	private boolean sizeOfWindowWasInits;

	public MicroFrame() {
		super();

		setWindowTitle("MicroFrameWindow");
		frame.setSize(400,400);
		frame.setResizable(false);
		setFrameRate(60);
		
		onCreate();

		createWindow();

		launch();
	}

	
	// == Life-cycle API ==
	public abstract void onCreate();

	public abstract void onRender();

	public abstract void onQuit();

	
	// == System API ==
	public final void quit() {
		stop();
	}

	public final double getFrameRate() {
		return FramePerSecond.getFrameRate();
	}

	public final void setFrameRate(int frameRate) {
		if (frameRate < 1) {
			throw new IllegalArgumentException("Frame rate cannot be less than 1");
		}
		deltaFrameTime = 1_000_000_000L / frameRate;
	}

	public long getFrameCount() {
		return frameCount;
	}

	public final Image loadImage(String path) {
		requireNonNull(path, "path");

		BufferedImage bi = null;

		try {
			bi = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new Image(bi);
	}

	
	// == Window API ==
	public final void setResizeEnabled(boolean enabled) {
		if(frame.isResizable() == enabled) {
			return;
		}
		
		frame.setResizable(enabled);
		
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}

	public final void setWindowSize(int width, int height) {
		if(sizeOfWindowWasInits && !frame.isResizable()) {
			throw new IllegalStateException("Window not resizable");
		}
		
		if(this.width == width && this.height == height) {
			return;
		}
		
		if (width < 10) {
			throw new IllegalArgumentException("Width cannot be less than 10");
		}

		if (height < 10) {
			throw new IllegalArgumentException("Height cannot be less than 10");
		}
		
		this.width = width;
		this.height = height;

		final Insets ins = frame.getInsets();
		final int w = width + ins.left + ins.right;
		final int h = height + ins.top + ins.bottom;
		
		frame.setSize(w,h);
		
		sizeOfWindowWasInits = true;
	}

	public final void setFullScreen() {
		final int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		final int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

		frame.setUndecorated(true);

		setWindowSize(width, height);
	}

	public final String getWindowTitle() {
		return title;
	}

	public final void setWindowTitle(String title) {
		this.title = requireNonNull(title, "title");
	}

	
	// == Style API ==
	public final float getStrokeWeight() {
		return Style.getStrokeWeight().getLineWidth();
	}

	public final void setStrokeWeight(float weight) {
		float sw = Style.getStrokeWeight().getLineWidth();

		if (weight < 1) {
			throw new IllegalArgumentException("Stroke weight cannot be less than 1");
		}

		if (sw != weight) {
			Style.setStrokeWeight(new BasicStroke(weight));
		}

		graphics.setStroke(Style.getStrokeWeight());
	}

	public final void setTextSize(int textSize) {
		if (graphics.getFont().getSize() == textSize) {
			return;
		}

		if (textSize < 1) {
			throw new IllegalArgumentException("Text size cannot be less than 1");
		}

		graphics.setFont(graphics.getFont().deriveFont((float) textSize));

	}

	public final void fill(int red, int green, int blue, int alpha) {
		Style.setFill(ColorPool.getColor(red, green, blue, alpha));
	}

	public final void fill(int red, int green, int blue) {
		fill(red, green, blue, 255);
	}

	public final void fill(int gray, int alpha) {
		fill(gray, gray, gray, alpha);
	}

	public final void fill(int gray) {
		fill(gray, 255);
	}

	public final void fill(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("Color cannot be null");
		}

		fill(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public final void stroke(int red, int green, int blue, int alpha) {
		Style.setStroke(ColorPool.getColor(red, green, blue, alpha));
	}

	public final void stroke(int red, int green, int blue) {
		stroke(red, green, blue, 255);
	}

	public final void stroke(int gray, int alpha) {
		stroke(gray, gray, gray, alpha);
	}

	public final void stroke(int gray) {
		stroke(gray, 255);
	}

	public final void stroke(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("Color cannot be null");
		}
		stroke(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public final void background(int red, int green, int blue, int alpha) {
		final Color fillOld = Style.getFill();
		final Color strokeOld = Style.getStroke();

		strokeOff();
		fill(red, green, blue, alpha);
		rect(0, 0, getWidth(), getHeight());

		stroke(strokeOld);
		fill(fillOld);
	}

	public final void background(int red, int green, int blue) {
		background(red, green, blue, 255);
	}

	public final void background(int gray, int alpha) {
		background(gray, gray, gray, alpha);
	}

	public final void background(int gray) {
		background(gray, gray, gray, 255);
	}

	public final void background(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("Color cannot be null");
		}
		background(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public final void strokeOff() {
		stroke(0, 0);
	}

	public final void fillOff() {
		fill(0, 0);
	}

	
	// == Interaction API ==
	public final int getMouseX() {
		return mouseX;
	}

	public final int getMouseY() {
		return mouseY;
	}

	public final int getLastPressedMouseButton() {
		return lastPressedMouseButton;
	}
	
	public final char getPressedKey() {
		return pressedKey;
	}

	public final boolean isMousePressed() {
		return mousePressed;
	}
	
	public void onMousePressed(MouseEvent e) {
	}
	
	public void onMousePressed() {
	}

	public void onMouseReleased(MouseEvent e) {
	}
	
	public void onMouseReleased() {
	}

	public void onMouseClicked(MouseEvent e) {
	}
	
	public void onMouseClicked() {
	}

	public void onMouseMoved(MouseEvent e) {
	}
	
	public void onMouseMoved() {
	}

	public void onMouseDragged(MouseEvent e) {
	}
	
	public void onMouseDragged() {
	}

	public void onMouseWheelMoved(MouseWheelEvent e) {
	}
	
	public void onMouseWheelMoved() {
	}

	public void onKeyTyped(KeyEvent e) {
	}
	
	public void onKeyTyped() {
	}

	public void onKeyPressed(KeyEvent e) {
	}
	
	public void onKeyPressed() {
	}

	public void onKeyReleased(KeyEvent e) {
	}
	
	public void onKeyReleased() {
	}

	
	// == Rendering Graphics ==
	public final void line(int x, int y, int x1, int y1) {
		if (Style.getStroke().getAlpha() != 0) {
			graphics.setColor(Style.getStroke());
			graphics.drawLine(x, y, x1, y1);
		}
	}

	public final void rect(int x, int y, int width, int height) {
		if (Style.getFill().getAlpha() != 0) {
			graphics.setColor(Style.getFill());
			graphics.fillRect(x, y, width, height);
		}

		if (Style.getStroke().getAlpha() != 0) {
			graphics.setColor(Style.getStroke());
			graphics.drawRect(x, y, width, height);
		}

	}

	public final void point(int x, int y) {
		if (Style.getStroke().getAlpha() != 0) {
			graphics.setColor(Style.getStroke());
			final int size = (int) Style.getStrokeWeight().getLineWidth();

			graphics.drawRect(x, y, size, size);
		}
	}

	public final void oval(int x, int y, int width, int height) {
		if (Style.getFill().getAlpha() != 0) {
			graphics.setColor(Style.getFill());
			graphics.fillOval(x, y, width, height);
		}

		if (Style.getStroke().getAlpha() != 0) {
			graphics.setColor(Style.getStroke());
			graphics.drawOval(x, y, width, height);
		}
	}

	public final void text(String text, int x, int y) {
		if (text == null) {
			throw new IllegalArgumentException("Text cannot be null");
		}

		if (Style.getFill().getAlpha() != 0) {
			graphics.setColor(Style.getFill());
			graphics.drawString(text, x, y);
		}

	}

	public final void text(int number, int x, int y) {
		text(String.valueOf(number), x, y);
	}

	public final void text(float number, int x, int y) {
		text(String.valueOf(number), x, y);
	}

	public final void text(long number, int x, int y) {
		text(String.valueOf(number), x, y);
	}

	public final void text(double number, int x, int y) {
		text(String.valueOf(number), x, y);
	}

	public final void image(Image image, int x, int y, int w, int h) {
		if (image == null) {
			throw new IllegalArgumentException("Image cannot be null");
		}

		graphics.drawImage(image.getBuffer(), x, y, w, h, null);

	}

	public final void image(Image image, int x, int y) {
		if (image == null) {
			throw new IllegalArgumentException("Image cannot be null");
		}

		graphics.drawImage(image.getBuffer(), x, y, null);

	}
	
	private void run() {
		
		while (running) {
			
			final long now = System.nanoTime();
			
			if(now-lastTime >= deltaFrameTime) {
				frameCount++;
				FramePerSecond.update();
				render();
				lastTime = now;
			} else {
				try {	
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}

	}

	private void render() {
		final BufferStrategy bs = canvas.getBufferStrategy();

		if (bs == null) {
			return;
		}

		graphics = (Graphics2D) bs.getDrawGraphics();
		
		onRender();
		graphics.dispose();
		
		try {
			bs.show();
		} catch(Exception e) {
			// When user will close application, it's will'be ignored
		}
	}

	private void createWindow() {
		frame.setTitle(title);
		frame.setVisible(true);

		final Insets ins = frame.getInsets();

		final int correctFrameWidth = ins.left + ins.right + getWidth();
		final int correctFrameHeight = ins.top + ins.bottom + getHeight();

		frame.setSize(correctFrameWidth, correctFrameHeight);

		canvas = new Canvas();
		canvas.setSize(getWidth(), getHeight());

		frame.add(canvas);
		frame.setLocationRelativeTo(null);

		canvas.createBufferStrategy(2);

		initListeners();
	}

	private void initListeners() {

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				onQuit();
				stop();
			}
			
		});

		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				mousePressed = true;
				lastPressedMouseButton = e.getButton();
				onMousePressed(e);
				onMousePressed();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				mousePressed = false;
				lastPressedMouseButton = e.getButton();
				onMouseReleased(e);
				onMouseReleased();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				onMouseClicked(e);
				onMouseClicked();
			}

		});

		canvas.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				onMouseMoved(e);
				onMouseMoved();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				onMouseDragged(e);
				onMouseDragged();
			}

		});

		canvas.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				onMouseWheelMoved(e);
				onMouseWheelMoved();
			}

		});

		canvas.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				onKeyTyped(e);
				onKeyTyped();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					
					onQuit();
					stop();
				}
				
				pressedKey = e.getKeyChar();
				
				onKeyPressed(e);
				onKeyPressed();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				onKeyReleased(e);
				onKeyReleased();
			}
		});

		canvas.setFocusable(true);
		canvas.requestFocus();
	}

	private void launch() {
		running = true;
		run();
	}

	private void stop() {
		running = false;
		frame.dispose();
		System.exit(0);
	}
}