package kinectserver;

public class Forma {
	private int x, y, alt, lar;
	
	public Forma(int _x, int _y, int _alt, int _lar){
		x = _x;
		y = _y;
		alt = _alt;
		lar = _lar;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getAlt() {
		return alt;
	}

	public void setAlt(int alt) {
		this.alt = alt;
	}

	public int getLar() {
		return lar;
	}

	public void setLar(int lar) {
		this.lar = lar;
	}
	
	
}
