package eqsln.sighter;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;


public class SightHelperActivity extends Activity implements SurfaceHolder.Callback {

	private Camera camera;
	private SurfaceView cameraView;
	private FrameLayout frameLayout;
	private SightView targetView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the entire screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		targetView = new SightView(this);
		
		// Create the surface for the camera to draw its preview on
		cameraView = new SurfaceView(this);
		cameraView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		cameraView.getHolder().addCallback(this);
		
		// Setup the layout where the cameraView is completely obscured by the edgeView
		frameLayout = new FrameLayout(this);
		frameLayout.addView(cameraView);
		frameLayout.addView(targetView);
		setContentView(frameLayout);
		frameLayout.setOnTouchListener(targetView);
		// Prevent camera preview from showing up first
		targetView.postInvalidate();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	
	@Override
	protected void onPause() {
		stopCameraPreview();
		this.moveTaskToBack(true);
		super.onPause();
	}
	
	
	private void stopCameraPreview() {
		if (camera != null) 
		{
			camera.setPreviewCallback(null);
			camera.stopPreview();
			camera.release();
			camera = null;
		}
	}
	
	private void startCameraPreview() {
		camera = Camera.open();
		try 
		{
			camera.setPreviewDisplay(cameraView.getHolder());
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		camera.setPreviewCallback(targetView);
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{
		Camera.Parameters parameters = camera.getParameters();
		parameters.setPreviewSize(width, height); // TODO: check that width, height are a valid camera preview size
		camera.setParameters(parameters);
		camera.startPreview();
	}


	public void surfaceCreated(SurfaceHolder holder) 
	{
		startCameraPreview();
	}
	
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		stopCameraPreview();
	}
	
	@Override
	public void onStop()
	{
		this.moveTaskToBack(true);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode==KeyEvent.KEYCODE_CAMERA || keyCode==KeyEvent.KEYCODE_SEARCH) 
		{
			if (targetView.mode==InputType.TARGET) targetView.mode = InputType.SIGHT;
			else targetView.mode = InputType.TARGET;
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_BACK){
			this.moveTaskToBack(true);
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_HOME){
			this.moveTaskToBack(true);
			return true;
		}
		else
			return false;
	}
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		super.onCreateOptionsMenu(menu); 
	    menu.add("Reset");
	    
		return true;
	}*/
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if("Reset".equals(item.getTitle()))
		{
			targetView.nextSet = 0;
		}
		return true;
	}
}