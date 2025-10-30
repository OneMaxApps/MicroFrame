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

public class MicroFrame {
	private Frame frame;
	private Canvas canvas;
	private Graphics2D graphics;
	private String title;
	private int width, height;
	private int mouseX, mouseY;
	private int mouseButton;
	private int frameRate;

	private boolean running;
	private boolean mousePressed;

	public MicroFrame() {
		super();

		onCreate();

		createWindow();

		launch();
	}

	public static void main(String[] args) {
		new MicroFrame();
	}

	// for overriding
	public void onCreate() {
		setTitle("MicroFrameWindow");
		setSize(400, 400);
		setFrameRate(60);
	}

	// for overriding
	public void onRender() {
		graphics.setColor(Color.GREEN);
		graphics.drawRect(mouseX, mouseY, 100, 100);
		line(100,100,200,200);
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
		graphics.drawLine(x, y, x1, y1);
	}

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
				stop();
			}
		});

		canvas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
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
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				mousePressed = false;
				mouseButton = e.getButton();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}

		});

		canvas.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
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