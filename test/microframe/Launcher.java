package microframe;

public class Launcher extends MicroFrame {

	public static void main(String[] args) {
		new Launcher();
	}

	@Override
	public void onCreate() {
		setSize(800,800);
	}
	
	@Override
	public void onRender() {
		line(100,100,200,200);
		
	}
	
}