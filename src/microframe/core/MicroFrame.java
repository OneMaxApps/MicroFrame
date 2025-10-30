package microframe.core;

import static java.util.Objects.requireNonNull;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import microframe.util.ColorPool;

public abstract class MicroFrame {
	private Frame frame;
	private Canvas canvas;
	private Graphics2D graphics;
	private String title;
	private Color colorStroke, colorFill;
	private int width, height;
	private int mouseX, mouseY;
	private int mouseButton;
	private int frameRate;

	private boolean running;
	private boolean mousePressed;

	public MicroFrame() {
		super();
		
		setTitle("MicroFrameWindow");
		setSize(400, 400);
		setFrameRate(60);
		
		onCreate();

		createWindow();

		launch();
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

	public final void setSize(int width, int height) {
		if (width < 100) {
			throw new IllegalArgumentException("Width cannot be less than 100");
		}

		if (height < 100) {
			throw new IllegalArgumentException("Height cannot be less than 100");
		}

		this.width = width;
		this.height = height;
	}

	public final String getTitle() {
		return title;
	}

	public final void setTitle(String title) {
		this.title = requireNonNull(title, "title");
	}

	public final int getFrameRate() {
		return frameRate;
	}

	public final void setFrameRate(int frameRate) {
		if (frameRate < 1) {
			throw new IllegalArgumentException("Frame rate cannot be less than 1");
		}
		this.frameRate = frameRate;
	}

	
	public final void line(int x, int y, int x1, int y1) {
		graphics.setColor(colorStroke);
		graphics.drawLine(x, y, x1, y1);
	}
	
	public final void rect(int x, int y, int width, int height) {		
		graphics.setColor(colorFill);
		graphics.fillRect(x, y, width, height);
		
		graphics.setColor(colorStroke);
		graphics.drawRect(x, y, width, height);
	}
	
	public final void oval(int x, int y, int width, int height) {
		graphics.setColor(colorFill);
		graphics.fillOval(x, y, width, height);
		
		graphics.setColor(colorStroke);
		graphics.drawOval(x, y, width, height);
	}
	
	public final void text(String text, int x, int y) {
		graphics.setColor(colorFill);
		graphics.drawString(text, x, y);
	}
	
	public final void fill(int red, int green, int blue, int alpha) {
		colorFill = ColorPool.getColor(red,green,blue,alpha);
	}
	
	public final void fill(int red, int green, int blue) {
		colorFill = ColorPool.getColor(red,green,blue,255);
	}
	
	public final void fill(int gray, int alpha) {
		colorFill = ColorPool.getColor(gray,gray,gray,alpha);
	}
	
	public final void fill(int gray) {
		colorFill = ColorPool.getColor(gray,gray,gray,255);
	}
	
	public final void stroke(int red, int green, int blue, int alpha) {
		colorStroke = ColorPool.getColor(red,green,blue,alpha);
	}
	
	public final void stroke(int red, int green, int blue) {
		colorStroke = ColorPool.getColor(red,green,blue,255);
	}
	
	public final void stroke(int gray, int alpha) {
		colorStroke = ColorPool.getColor(gray,gray,gray,alpha);
	}
	
	public final void stroke(int gray) {
		colorStroke = ColorPool.getColor(gray,gray,gray,255);
	}
	
	public final void strokeOff() {
		colorStroke = ColorPool.getColor(0,0,0,0);
	}
	
	public final void fillOff() {
		colorFill = ColorPool.getColor(0,0,0,0);
	}
	
	public abstract void onCreate();
	public abstract void onRender();
	public abstract void onQuit();
	
	public void onMousePressed() {}
	public void onMouseReleased() {}
	public void onMouseClicked() {}
	public void onMouseMoved() {}
	public void onMouseDragged() {}

	private void run() {
		while (running) {
			render();

			try {
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
		frame = new Frame(getTitle());
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