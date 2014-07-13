package com.example.audioreco;

import android.os.Bundle;
import android.media.*;
import android.media.MediaRecorder.*;
import android.content.*;
import android.graphics.*;
import android.app.Activity;
import android.view.Menu;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.pm.*;

public class AudReco extends Activity implements Button.OnClickListener{
boolean isr=true;
Handler handler=new Handler();
MediaRecorder mr;
MediaPlayer mp;
int state=0;
Bitmap b;
float t=0,k=0;
//boolean isr=true;
Thread t1;
MyHandler myh;
SurfaceHolder sh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aud_reco);
		b=BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
		Button b1=(Button)findViewById(R.id.start);
		Button b2=(Button)findViewById(R.id.stop);
		Button b3=(Button)findViewById(R.id.play);
		b1.setOnClickListener(this);
		b2.setOnClickListener(this);
		b3.setOnClickListener(this);
		SurfaceView sv=(SurfaceView)findViewById(R.id.surfaceView1);
		sh=sv.getHolder();
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		myh=new MyHandler();
		t1=new Thread(myh);
		t1.start();
	}
	public void onPause()
	{
		super.onPause();
		isr=false;
		while(true)
		{
			try
			{
				t1.join();
				break;
			}
			catch(Exception ex)
			{
				
			}
		}
	}
	public void onResume()
	{
		super.onResume();
		isr=true;
		t1=new Thread(myh);
		t1.start();
	}
		public void onClick(View v)
		{
			switch(v.getId())
			{
			case R.id.start:
				try
				{
					mr=new MediaRecorder();
					mr.setAudioSource(AudioSource.MIC);
					mr.setOutputFormat(OutputFormat.MPEG_4);
					mr.setOutputFile("sdcard/mre.mp4");
					mr.setAudioEncoder(AudioEncoder.AMR_NB);
					
					mr.prepare();
					mr.start();
					state=1;
					k=10;
				}
				catch(Exception ex)
				{
					
				}
				break;
			case R.id.stop:
				try
				{
					if(state==1)
					{
					mr.stop();
					mr.release();
					k=0;
					state=3;
					}
				}
				catch(Exception ex)
				{
					
				}
				break;
			case R.id.play:
				try
				{
					if(state==3)
					{
					mp=new MediaPlayer();
					mp.setDataSource("sdcard/mre.mp4");
					mp.prepare();
					mp.start();
					state=2;
					k=5;
					mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
						

						@Override
						public void onCompletion(MediaPlayer mp1) {
							mp.stop();
							mp.release();
							t=0;k=0;
							state=0;
						}
						
					});
					}	
				}
				catch(Exception ex)
				{
					
				}
				
				break;
				default:
					break;
			}
		}
	
		class MyHandler implements Runnable
		{
		Canvas canvas;
		Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
			public void run()
			{
				while(isr)
				{
					if(!sh.getSurface().isValid())
						continue;
					canvas=sh.lockCanvas();
					canvas.drawColor(Color.WHITE);
					canvas.save();
					canvas.translate(100, 100);
					canvas.rotate(t);
					canvas.drawBitmap(b,new Rect(0,0,b.getWidth(),b.getHeight()),new RectF(-50,-50,50,50),p);
					canvas.restore();
					t+=k;
					if(state==2)
					{
						if(t>=20 || t<=20)
							k*=-1;
					}
					sh.unlockCanvasAndPost(canvas);
					try
					{
						Thread.sleep(100);
					}
					catch(Exception ex)
					{
						
					}
				}
			}
			
		}
}

