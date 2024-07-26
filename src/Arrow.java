import java.awt.*;


public class Arrow {
	private int x=SortVisualizer.getXi(), y;
	private static final int tw=SortVisualizer.getS(), th=2*tw, rw=tw/2-4, rh=28;
	private Color c;
	
	public Color getC() {
		return c;
	}
	public void setC(Color c) {
		this.c = c;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public static int getTw() {
		return tw;
	}
	public static int getTh() {
		return th;
	}

	public static int getRw() {
		return rw;
	}
	public static int getRh() {
		return rh;
	}
	public int getY() {
		return y;
	} 
	public Arrow(int yc, Color color) {
		y = yc;
		c = color;
	}
	
	public void draw(Graphics g) {
		g.setColor(c);
		g.fillPolygon(new int[] {x, x+tw/2, x+tw}, new int[] {y, y-th/2, y}, 3);
		g.fillRect(x+(tw-rw)/2, y, rw, rh);
	}
	
	
}