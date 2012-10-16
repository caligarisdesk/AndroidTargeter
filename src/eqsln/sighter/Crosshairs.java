package eqsln.sighter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;

public class Crosshairs {
	private Point p = new Point(100,100);
	private Paint paint = new Paint();
	
	public Crosshairs() {
		paint.setColor(Color.RED);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(5);
	}
	public void setMode(int mode){
		switch (mode)
		{
		case 0:
			paint.setColor(Color.RED);
			break;
		case 1:
			paint.setColor(Color.GREEN);
			break;
		default:
			break;
		}
	}
	public void OnDraw(Canvas c)
	{
		int radius_sm = (int)((double)c.getWidth()*0.1);
		int radius_lg = (int)((double)c.getWidth()*0.2);
		c.drawCircle(p.x, p.y, radius_sm, paint);
		c.drawCircle(p.x, p.y, radius_lg, paint);
		c.drawLine(0, p.y, c.getWidth(), p.y, paint);
		c.drawLine(p.x, 0, p.x, c.getHeight(), paint);
	}
	public void setCoord(int x, int y)
	{
		p.set(x, y);
	}
	public Point getCoord()
	{
		return p;
	}
	
}
