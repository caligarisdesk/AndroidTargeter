package eqsln.sighter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SightView extends View implements PreviewCallback, OnTouchListener{
	private Crosshairs crosshairs =  new Crosshairs();
	//private Target target = new Target();
	private boolean cameraPreviewValid = false;
	private final Lock cameraPreviewLock = new ReentrantLock();
	public InputType mode = InputType.TARGET;
	public int nextSet = 0;
	public SightView(Context context) 
	{
		super(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		if (cameraPreviewLock.tryLock()) 
		{
			try 
			{				
				if (cameraPreviewValid) 
				{
					//int d = distance(crosshairs.getCoord(), target.getCoord());
					int d = 200;
					if(d<100 && d>20)
					{
						//target.setMode(1);
						crosshairs.setMode(0);
					}
					else if(d<20)
					{
						//target.setMode(2);
						crosshairs.setMode(1);
					}
					else
					{
						//target.setMode(0);
						crosshairs.setMode(0);
					}
					cameraPreviewValid = false;
					crosshairs.OnDraw(canvas);
					//target.OnDraw(canvas);
					/*
					if(mode==InputType.TARGET)
					{
						canvas.drawText("Target", 20, 20, paint);
					}
					else
						canvas.drawText("Crosshairs", 20, 20, paint);
					*/
				}
			}
			finally 
			{
				cameraPreviewLock.unlock();
			}
		}
	}
	private int distance(Point a, Point b){
		return (int)Math.sqrt((a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y));
	}
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) 
	{
		if (cameraPreviewLock.tryLock())
		{
			try
			{
				if(!cameraPreviewValid) 
				{
					cameraPreviewValid = true;
				}
			}
			finally 
			{
				cameraPreviewLock.unlock();
				postInvalidate();
			}
			/*
			switch (nextSet){
				case 0:
					break;
				case 1:
					try
					{
						Size s = camera.getParameters().getPreviewSize();
						int width = s.width;
						int height = s.height;
						target.process(data, width, height);
					}
					catch(Exception e) {;}
					break;
				case 2:
					nextSet = 1;
					Size s = camera.getParameters().getPreviewSize();
					int width = s.width;
					int height = s.height;
					target.setMask(data, width, height);
					break;
				default:
					break;
			}*/
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		/*if(mode==InputType.TARGET)
		{
			target.setCoord((int)event.getX(), (int)event.getY());
			nextSet = 2;
		}
		else*/
			crosshairs.setCoord((int)event.getX(), (int)event.getY());
		return false;
	}
	
}
