package microframe.core;

import static java.util.Objects.requireNonNull;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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

public abstract class MicroFrame {
	private final Frame frame = new Frame("");
	private Canvas canvas;
	private Graphics2D graphics;
	private String title;
	private Color stroke, fill;
	private BasicStroke strokeWeight;

	private long frameCount;

	private int width, height;
	private int mouseX, mouseY;
	private int mouseButton;
	private int frameRate;

	private boolean running;
	private boolean mousePressed;
	private boolean windowSizeInitialized;

	public MicroFrame() {
		super();

		setWindowTitle("MicroFrameWindow");
		width = height = 400;
		setFrameRate(60);

		onCreate();

		createWindow();

		launch();
	}

	public final float getStrokeWeight() {
		return strokeWeight.getLineWidth();
	}

	public final void setStrokeWeight(float weight) {
		if (weight < 1) {
			throw new IllegalArgumentException("Stroke weight cannot be less than 1");
		}

		if (strokeWeight == null) {
			strokeWeight = new BasicStroke(weight);
		}

		if (strokeWeight.getLineWidth() != weight) {
			strokeWeight = new BasicStroke(weight);
		}

		graphics.setStroke(this.strokeWeight);
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}

	public final int getMouseX() {
		return mouseX;
	}

	public final int getMouseY() {
		return mouseY;
	}

	public final int getMouseButton() {
		return mouseButton;
	}

	public final boolean isMousePressed() {
		return mousePressed;
	}

	public final void setWindowSize(int width, int height) {
		if (windowSizeInitialized) {
			throw new IllegalStateException("Window size already initialized");
		}

		if (width < 100) {
			throw new IllegalArgumentException("Width cannot be less than 100");
		}

		if (height < 100) {
			throw new IllegalArgumentException("Height cannot be less than 100");
		}

		this.width = width;
		this.height = height;

		windowSizeInitialized = true;
	}
	
	public final void setFullScreen() {
		final int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		final int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
		frame.setUndecorated(true);
		
		setWindowSize(width,height);
	}

	public final String getTitle() {
		return title;
	}

	public final void setWindowTitle(String title) {
		this.title = requireNonNull(title, "title");
	}

	public final double getFrameRate() {
		return FramePerSecond.getFrameRate();
	}

	public final void setFrameRate(int frameRate) {
		if (frameRate < 1) {
			throw new IllegalArgumentException("Frame rate cannot be less than 1");
		}
		this.frameRate = frameRate;
	}

	public long getFrameCount() {
		return frameCount;
	}

	public final void line(int x, int y, int x1, int y1) {
		graphics.setColor(stroke);
		graphics.drawLine(x, y, x1, y1);
	}

	public final void rect(int x, int y, int width, int height) {
		graphics.setColor(fill);
		graphics.fillRect(x, y, width, height);

		graphics.setColor(stroke);
		graphics.drawRect(x, y, width, height);
	}

	public final void oval(int x, int y, int width, int height) {
		graphics.setColor(fill);
		graphics.fillOval(x, y, width, height);

		graphics.setColor(stroke);
		graphics.drawOval(x, y, width, height);
	}

	public final void text(String text, int x, int y) {
		graphics.setColor(fill);
		graphics.drawString(text, x, y);
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
		if(image == null) {
			throw new IllegalArgumentException("Image cannot be null");
		}
		
		graphics.drawImage(image.getBuffer(),x,y,w,h,null);
		
	}
	
	public final void image(Image image, int x, int y) {
		if(image == null) {
			throw new IllegalArgumentException("Image cannot be null");
		}
		
		graphics.drawImage(image.getBuffer(),x,y,null);
		
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
		fill = ColorPool.getColor(red, green, blue, alpha);
	}

	public final void fill(int red, int green, int blue) {
		fill = ColorPool.getColor(red, green, blue, 255);
	}

	public final void fill(int gray, int alpha) {
		fill = ColorPool.getColor(gray, gray, gray, alpha);
	}

	public final void fill(int gray) {
		fill = ColorPool.getColor(gray, gray, gray, 255);
	}

	public final void stroke(int red, int green, int blue, int alpha) {
		stroke = ColorPool.getColor(red, green, blue, alpha);
	}

	public final void stroke(int red, int green, int blue) {
		stroke = ColorPool.getColor(red, green, blue, 255);
	}

	public final void stroke(int gray, int alpha) {
		stroke = ColorPool.getColor(gray, gray, gray, alpha);
	}

	public final void stroke(int gray) {
		stroke = ColorPool.getColor(gray, gray, gray, 255);
	}

	public final void background(int red, int green, int blue, int alpha) {
		fill = ColorPool.getColor(red, green, blue, alpha);

		strokeOff();

		rect(0, 0, getWidth(), getHeight());
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

	public final void strokeOff() {
		stroke = ColorPool.getColor(0, 0, 0, 0);
	}

	public final void fillOff() {
		fill = ColorPool.getColor(0, 0, 0, 0);
	}

	public final Image loadImage(String path) {
		requireNonNull(path,"path");
		
		BufferedImage bi = null;

		try {
			bi = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new Image(bi);
	}

	public abstract void onCreate();

	public abstract void onRender();

	public abstract void onQuit();

	public void onMousePressed() {
	}

	public void onMouseReleased() {
	}

	public void onMouseClicked() {
	}

	public void onMouseMoved() {
	}

	public void onMouseDragged() {
	}

	private void run() {

		while (running) {

			FramePerSecond.update();

			render();

			try {
				frameCount++;
				Thread.sleep(1000 / frameRate);
			} catch (InterruptedException e) {

			}

		}

	}

	private void render() {
		BufferStrategy bs = canvas.getBufferStrategy();

		if (bs == null) {
			return;
		}

		graphics = (Graphics2D) bs.getDrawGraphics();

		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, getWidth(), getHeight());

		onRender();

		graphics.dispose();

		bs.show();
	}

	private void createWindow() {
		frame.setTitle(title);
		frame.setSize(getWidth(), getHeight());

		canvas = new Canvas();
		canvas.setSize(getWidth(), getHeight());

		frame.add(canvas);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(false);

		canvas.createBufferStrategy(2);

		setupListeners();
	}

	private void setupListeners() {

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				onQuit();
				stop();
			}
		});

		canvas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					onQuit();
					stop();
				}
			}
		});

		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				mousePressed = true;
				mouseButton = e.getButton();
				onMousePressed();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				mousePressed = false;
				mouseButton = e.getButton();
				onMouseReleased();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				onMouseClicked();
			}

		});

		canvas.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				onMouseMoved();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				onMouseDragged();
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