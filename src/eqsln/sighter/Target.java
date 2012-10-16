package eqsln.sighter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;

public class Target {
	private Point p = new Point(100,100);
	private Paint paint = new Paint();
	private int vidWidth, vidHeight;
	byte[] targetArea = new byte[441];
	Target()
	{
		paint.setColor(Color.GRAY);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(5);
	}
	public void setMode(int mode)
	{
		switch (mode)
		{
		case 0:
			paint.setColor(Color.GRAY);
			break;
		case 1:
			paint.setColor(Color.CYAN);
			break;
		case 2:
			paint.setColor(Color.GREEN);
			break;
		default:
			break;
		}
	}
	public void OnDraw(Canvas c)
	{
		c.drawRect(p.x-20, p.y+20, p.x+20, p.y-20, paint);
	}
	public void setCoord(int x, int y)
	{
		p.set(x, y);
	}
	public Point getCoord()
	{
		return p;
	}
	public void process(byte[] data, int width, int height) 
	{
		vidWidth = width;
		vidHeight = height;
		int length = width * height;
		double bestValue = 0;
		Point bestPoint = p;
		for(int n = Math.max(0, p.x-10); n <= Math.min(vidWidth-1, p.x+10); n++)
		{
			for(int m = Math.max(0, p.y-10); m <= Math.min(vidWidth-1, p.y+10); m++)
			{
				double value = convolve(data, new Point(n,m));
				if (value>bestValue)
				{
					bestPoint.x = n;
					bestPoint.y = m;
					bestValue = value;
				}
			}
		}
		setCoord(bestPoint.x, bestPoint.y);
		//System.arraycopy(data, 0, rawData, 0, length);
	}
	private double convolve(byte[] image, Point pc)
	{
		double answer = 0;
		int counter = 0;
		for(int n = pc.x-10; n <= pc.x+10; n++)
		{
			for(int m = pc.y-10; m <= pc.y+10; m++)
			{
				if((n < 0 || m < 0)|| (n >= vidWidth || m >= vidHeight)){}
				else
					answer += image[getPixel(n, m)]*targetArea[counter];
				counter++;
			}
		}
		return answer;
	}
	public int getPixel(int x, int y)
	{
		return y*vidWidth+x;
	}
	public int getPixel(int x, int y, int width)
	{
		return y*width+x;
	}
	public Point getPixel(int n)
	{
		int x = n % vidWidth;
		int y = n / vidWidth;
		return new Point(x,y);
	}
	public void setMask(byte[] data, int width, int height) {
		int counter = 0;
		vidWidth = width;
		vidHeight = height;
		for(int n = p.x-10; n <= p.x+10; n++)
		{
			for(int m = p.y-10; m <= p.y+10; m++)
			{
				if((p.y < 0 || p.x < 0)|| (p.x >= width || p.y >= height))
					targetArea[counter] = 0;
				else
					targetArea[counter] = data[getPixel(n,m)];
				counter++;
			}
		}
	}
}
