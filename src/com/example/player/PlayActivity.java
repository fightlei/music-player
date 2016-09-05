package com.example.player;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class PlayActivity extends Activity{
	
	float previousX;  //用于记录在viewFlipper中手指按下时的x坐标
	
	
	public static SeekBar sbar_music_progress;
	public static TextView tv_music_total_time;
	public static TextView tv_music_current_time;
	private Button  btn_play_back;
	private TextView  tv_songname_play;
	private TextView  tv_artist_play;
	private View ll_play_background;
	
	private Button  btn_play_mode;
	private Button  btn_play_last_song;
	private Button  btn_play_song;
	private Button  btn_play_next_song;
	private Button  btn_play_menupoint;
	
	private Button btn_btn_start_or_pause;
	private TextView  tv_music_artist;
	private TextView  tv_music_name;
	
	
	private int for_main_id;
	private String for_main_music_name;
	private String for_main_music_artist;
	

	private Timer play_activity_timer;
	
	private int position;
	
	private ViewFlipper viewFlipper;
	public static LrcView lrcView;
	
	private Handler handler;
	Runnable runable;
	
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_play);
		initialize();
		/*
		handler=new Handler();
		runable=new Runnable() {
			
			@Override
			public void run() {
				sbar_music_progress.setMax(MainActivity.mMediaPlayer.getDuration());
				
				sbar_music_progress.setProgress(MainActivity.mMediaPlayer.getCurrentPosition());
				String text;
				int time_minute=(MainActivity.mMediaPlayer.getDuration()/1000)/60;
				int time_second=MainActivity.mMediaPlayer.getDuration()/1000%60;
				if(time_second<10){
					text=time_minute+":"+"0"+time_second;
				}else{
				text=time_minute+":"+time_second;
				}
				tv_music_total_time.setText(text);
				
				time_minute=(MainActivity.mMediaPlayer.getCurrentPosition()/1000)/60;
				time_second=MainActivity.mMediaPlayer.getCurrentPosition()/1000%60;
				if(time_second<10){
					text=time_minute+":"+"0"+time_second;
				}else{
				text=time_minute+":"+time_second;
				}
				tv_music_current_time.setText(text);
				

				
				handler.postDelayed(runable, 500);
				
				
			}
		};
		handler.post(runable);
		*/
//		{
//
//			@Override
//			public void handleMessage(Message msg) {
//				if(msg.what==1){
//					tv_music_total_time.setText((CharSequence) msg.obj);
//				}
//				if(msg.what==2){
//					tv_music_current_time.setText((CharSequence) msg.obj);
//				}
//				super.handleMessage(msg);
//			}
//			
//		};
		viewFlipper.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					previousX = event.getX();
				}else if(event.getAction()==MotionEvent.ACTION_UP){
					float  x=event.getX();
					if((x-previousX)>100){
						viewFlipper.showNext();
					}else if((previousX-x)>100){
						viewFlipper.showPrevious();
					}
				}
				return true;
			}
			
		});
		
		btn_play_back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
//				handler.removeCallbacks(runable);
////				play_activity_timer.cancel();
//				PlayerView.main_activity.get_playactivity_data();
				PlayActivity.this.finish();
			}
			
		});
		btn_play_song.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(MainActivity.mMediaPlayer==null){
					btn_play_song.setBackgroundResource(R.drawable.icon_pause_normal);
					for_main_id=R.drawable.icon_pause_normal;
					MainActivity.mMediaPlayer =new MediaPlayer();
					try {
						MainActivity.mMediaPlayer.setDataSource(MainActivity.temp_music_data.get(0).getMusic_path());

						
					} catch (Exception e) {
						e.printStackTrace();
					} 
					try {
						MainActivity.mMediaPlayer.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					MainActivity.cursor_for_music=0;
					MainActivity.mMediaPlayer.start();
				}else{
					if(MainActivity.mMediaPlayer.isPlaying()){
						MainActivity.mMediaPlayer.pause();
						btn_play_song.setBackgroundResource(R.drawable.icon_play_normal);
						for_main_id=R.drawable.icon_play_normal;
					}else{
						btn_play_song.setBackgroundResource(R.drawable.icon_pause_normal);
						for_main_id=R.drawable.icon_pause_normal;
						MainActivity.mMediaPlayer.start();
						
					}
				}
			}
			
		});
		btn_play_mode.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				switch(MainActivity.PLAY_MODE){
				
				case 1:
					//顺序播放
					btn_play_mode.setBackgroundResource(R.drawable.icon_playing_mode_normal);
					MainActivity.PLAY_MODE=2;
					Toast.makeText(PlayActivity.this, "顺序播放", 1).show();
					break;
				case 2:
					//随机
					btn_play_mode.setBackgroundResource(R.drawable.icon_playing_mode_shuffle);
					MainActivity.PLAY_MODE=3;
					Toast.makeText(PlayActivity.this, "随机播放", 1).show();
					break;
				case 3:
					//单曲循环
					btn_play_mode.setBackgroundResource(R.drawable.icon_playing_mode_repeat_cur);
					MainActivity.PLAY_MODE=4;
					Toast.makeText(PlayActivity.this, "单曲循环", 1).show();
					break;
				case 4:
					//循环播放
					btn_play_mode.setBackgroundResource(R.drawable.icon_playing_mode_repeat_all);
					MainActivity.PLAY_MODE=1;
					Toast.makeText(PlayActivity.this, "循环播放", 1).show();
					break;
				}
			}
			
		});
		
		//上一曲代码
		
		
		//分离
		btn_play_last_song.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(MainActivity.mMediaPlayer==null){
					MainActivity.mMediaPlayer =new MediaPlayer();
					try {
						MainActivity.mMediaPlayer.setDataSource(MainActivity.temp_music_data.get(0).getMusic_path());

						
					} catch (Exception e) {
						e.printStackTrace();
					} 
					try {
						MainActivity.mMediaPlayer.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					MainActivity.cursor_for_music=0;
					for_main_id=R.drawable.icon_pause_normal;
					btn_play_song.setBackgroundResource(R.drawable.icon_pause_normal);
					
					for_main_music_artist=MainActivity.temp_music_data.get(0).getMusic_artist();
					tv_artist_play.setText(MainActivity.temp_music_data.get(0).getMusic_artist());
					for_main_music_name=MainActivity.temp_music_data.get(0).getMusic_name();
					tv_songname_play.setText(MainActivity.temp_music_data.get(0).getMusic_name());
					if(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).isPlay_view_yes_no()){

						ll_play_background.setBackgroundDrawable(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_map());
						}else{
							ll_play_background.setBackgroundResource(R.drawable.default_bg_hdpi);
						}
					MainActivity.mMediaPlayer.start();
		    	}else{
		    		
		    		if(MainActivity.PLAY_MODE==3){
		    			MainActivity.cursor_for_music=(int)(Math.random()*MainActivity.temp_music_data.size());
		    		}else{
		    			if(MainActivity.cursor_for_music<=0){
			    			MainActivity.cursor_for_music=MainActivity.temp_music_data.size()-1;
						}else{
							MainActivity.cursor_for_music--;
						}
		    		}
		    		MainActivity.mMediaPlayer.reset();
		    		
					try {
						MainActivity.mMediaPlayer.setDataSource(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_path());
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					try {
						MainActivity.mMediaPlayer.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					for_main_id=R.drawable.icon_pause_normal;
					btn_play_song.setBackgroundResource(R.drawable.icon_pause_normal);
					
					for_main_music_artist=MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_artist();
					for_main_music_name=MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_name();
					tv_songname_play.setText(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_name());
					tv_artist_play.setText(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_artist());
					if(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).isPlay_view_yes_no()){

						ll_play_background.setBackgroundDrawable(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_map());
						}else{
							ll_play_background.setBackgroundResource(R.drawable.default_bg_hdpi);
						}
					MainActivity.mMediaPlayer.start();
		    	}
				
			}
			
			
		});
		btn_play_next_song.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				
				if(MainActivity.mMediaPlayer==null){
					MainActivity.mMediaPlayer =new MediaPlayer();
					try {
						MainActivity.mMediaPlayer.setDataSource(MainActivity.temp_music_data.get(0).getMusic_path());

						
					} catch (Exception e) {
						e.printStackTrace();
					} 
					try {
						MainActivity.mMediaPlayer.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					MainActivity.cursor_for_music=0;
					for_main_id=R.drawable.icon_pause_normal;
					btn_play_song.setBackgroundResource(R.drawable.icon_pause_normal);
					
					for_main_music_artist=MainActivity.temp_music_data.get(0).getMusic_artist();
					tv_artist_play.setText(MainActivity.temp_music_data.get(0).getMusic_artist());
					for_main_music_name=MainActivity.temp_music_data.get(0).getMusic_name();
					tv_songname_play.setText(MainActivity.temp_music_data.get(0).getMusic_name());
					if(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).isPlay_view_yes_no()){

						ll_play_background.setBackgroundDrawable(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_map());
						}else{
							ll_play_background.setBackgroundResource(R.drawable.default_bg_hdpi);
						}
					MainActivity.mMediaPlayer.start();
		    	}else{
		    		
		    		if(MainActivity.PLAY_MODE==3){
		    			MainActivity.cursor_for_music=(int)(Math.random()*MainActivity.temp_music_data.size());
		    		}else{
		    			if(MainActivity.cursor_for_music>=MainActivity.temp_music_data.size()-1){
			    			MainActivity.cursor_for_music=0;
						}else{
							MainActivity.cursor_for_music++;
						}
		    		}
		    		MainActivity.mMediaPlayer.reset();
		    		
					try {
						MainActivity.mMediaPlayer.setDataSource(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_path());
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					try {
						MainActivity.mMediaPlayer.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					for_main_id=R.drawable.icon_pause_normal;
					btn_play_song.setBackgroundResource(R.drawable.icon_pause_normal);
					
					for_main_music_artist=MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_artist();
					for_main_music_name=MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_name();
					tv_songname_play.setText(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_name());
					tv_artist_play.setText(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_artist());
					if(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).isPlay_view_yes_no()){

						ll_play_background.setBackgroundDrawable(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_map());
						}else{
							ll_play_background.setBackgroundResource(R.drawable.default_bg_hdpi);
						}
					MainActivity.mMediaPlayer.start();
		    	}
				
			}
			
		});
		btn_play_next_song.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});

		sbar_music_progress.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				position=arg1;
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				MainActivity.mMediaPlayer.seekTo(position);
				
			}
			
		});
		
	}

	private void initialize() {
		
		viewFlipper=(ViewFlipper) findViewById(R.id.vf_two_page);
		lrcView=(LrcView) findViewById(R.id.lrcv_show_lrc);
		
		btn_play_back=(Button) findViewById(R.id.btn_play_back);
		tv_songname_play=(TextView) findViewById(R.id.tv_songname_play);


		tv_songname_play.setText(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_name());
		tv_artist_play=(TextView) findViewById(R.id.tv_artist_play);
		tv_artist_play.setText(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_artist());
		ll_play_background=findViewById(R.id.ll_play_background);
		if(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).isPlay_view_yes_no()){

		ll_play_background.setBackgroundDrawable(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_map());
		}else{
			ll_play_background.setBackgroundResource(R.drawable.default_bg_hdpi);
		}
		
		//seekbar
		sbar_music_progress=(SeekBar) findViewById(R.id.sbar_music_progress);
		tv_music_current_time=(TextView) findViewById(R.id.tv_music_current_time);
		tv_music_total_time=(TextView) findViewById(R.id.tv_music_total_time);
		
		//底部五控件
		btn_play_mode=(Button) findViewById(R.id.btn_play_mode);
		
		
		switch(MainActivity.PLAY_MODE){
		
		case 4:
			//顺序播放
			btn_play_mode.setBackgroundResource(R.drawable.icon_playing_mode_repeat_cur);
			break;
		case 1:
			//随机
			btn_play_mode.setBackgroundResource(R.drawable.icon_playing_mode_repeat_all);
			break;
		case 2:
			//单曲循环
			btn_play_mode.setBackgroundResource(R.drawable.icon_playing_mode_normal);
			
			break;
		case 3:
			//循环播放
			btn_play_mode.setBackgroundResource(R.drawable.icon_playing_mode_shuffle);
			

			break;
		}
		
		btn_play_last_song=(Button) findViewById(R.id.btn_play_last_song);
		btn_play_song=(Button) findViewById(R.id.btn_play_song);
		if(PlayerView.main_activity.mMediaPlayer.isPlaying()){
			btn_play_song.setBackgroundResource(R.drawable.icon_pause_normal);
		}else{
			btn_play_song.setBackgroundResource(R.drawable.icon_play_normal);
		}
		btn_play_next_song=(Button) findViewById(R.id.btn_play_next_song);
		btn_play_menupoint=(Button) findViewById(R.id.btn_play_menupoint);
		
		
		
		PlayerView.main_activity.get_playactivity_relation(btn_play_song,tv_songname_play,tv_artist_play,ll_play_background);
		
		PlayActivity.lrcView.setPath(Environment.getExternalStorageDirectory()+"",MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_name()+".lrc");
		
		
//		
		
		
//		play_activity_timer = new Timer();
//		TimerTask mTimerTask = new TimerTask() {
//			
//			@Override
//			public void run() {
//				
//				//through handler alter text
//				Message message=new Message();
//				sbar_music_progress.setMax(MainActivity.mMediaPlayer.getDuration());
//				
//				sbar_music_progress.setProgress(MainActivity.mMediaPlayer.getCurrentPosition());
//				
//				int time_minute=(MainActivity.mMediaPlayer.getDuration()/1000)/60;
//				int time_second=MainActivity.mMediaPlayer.getDuration()/1000%60;
//				String text=time_minute+":"+time_second;
//				message.what=1;
//				message.obj=text;
//				handler.sendMessage(message);
//				time_minute=(MainActivity.mMediaPlayer.getCurrentPosition()/1000)/60;
//				time_second=MainActivity.mMediaPlayer.getCurrentPosition()/1000%60;
//				text=time_minute+":"+time_second;
//				message.what=2;
//				message.obj=text;
//				handler.sendMessage(message);
//			}
//		};
//		play_activity_timer.schedule(mTimerTask, 0, 1000);
	}

	
	public void get_mainactivity_data(){
		if(tv_songname_play!=null&&tv_artist_play!=null){
		tv_songname_play.setText(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_name());
		tv_artist_play.setText(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_artist());}
	}

	@Override
	protected void onPause() {
//		play_activity_timer.cancel();
		PlayerView.main_activity.get_playactivity_data();
		super.onPause();
	}
	
	
	

}
