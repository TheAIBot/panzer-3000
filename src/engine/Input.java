package engine;

public class Input {
	public int id;
	public boolean w;
	public boolean a;
	public boolean s;
	public boolean d;
	public boolean click;
	public double x;
	public double y;
	
	public Input(boolean w, boolean a, boolean s, boolean d, boolean click, double x, double y, int id) {
		this.w = w;
		this.a = a;
		this.s = s;
		this.d = d;
		this.click = click;
		this.x = x;
		this.y = y;
		this.id = id;
	}
	
	public Input() {
		id = 0; w = false; a = false; s = false; d = false; click = false; x = 0; y = 0;
	}
	
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append(w ? "w" : " ");
		b.append(a ? "a" : " ");
		b.append(s ? "s" : " ");
		b.append(d ? "d" : " ");
		b.append(click? "click": "     ");
		b.append(x);
		b.append(" ");
		b.append(y);
		
		return b.toString();
	}
	
	public boolean equals(Object o) {
		
		if (o == null || o.getClass() != this.getClass()) {
			return false;
		} else {
			Input input = (Input) o;
			if (this.id != input.id ||
				this.w != input.w ||
				this.a != input.a ||
				this.s != input.a ||
				this.d != input.a ||
				this.click != input.click ||
				this.x != input.x ||
				this.y != input.y) {
				return false;
			}
		}		
		return true;		
	}
	
}
