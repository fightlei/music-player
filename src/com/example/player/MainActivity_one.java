package com.example.player;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.data.MusicInfo;
import com.example.data.MyAdapter;
import com.example.player.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity_one extends Activity {
	
	public static List<MusicInfo> listdata_music =new ArrayList<MusicInfo>();
	
	private ListView lv_music;
	private static TextView tv_music_name;
	private static TextView tv_music_artist;
	public static MediaPlayer mMediaPlayer;
	private static Button btn_btn_start_or_pause;
	private Button btn_next_songs;
	public static int cursor_for_music; 
	private View bottom_view;

	private ImageView iv_bottom_map;
	
	public static int PLAY_MODE=1;
	
	private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
	private boolean for_play_view_yes_no;
	
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        search_musicInfo();
        initialize();
        
        tv_music_name.setText(listdata_music.get(0).getMusic_name());
        tv_music_artist.setText(listdata_music.get(0).getMusic_artist());
        iv_bottom_map.setBackgroundDrawable(listdata_music.get(0).getMusic_map());
        
        
        MyAdapter adapter=new MyAdapter(listdata_music,this);
        lv_music.setAdapter(adapter);
        lv_music.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				if(mMediaPlayer==null){
					mMediaPlayer =new MediaPlayer();
					try {
						mMediaPlayer.setDataSource(listdata_music.get(arg2).getMusic_path());
						
						
					} catch (Exception e) {
						e.printStackTrace();
					} 
					try {
						mMediaPlayer.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					cursor_for_music=arg2;
					btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_pause_normal);
					tv_music_artist.setText(listdata_music.get(arg2).getMusic_artist());
					tv_music_name.setText(listdata_music.get(arg2).getMusic_name());
					iv_bottom_map.setBackgroundDrawable(listdata_music.get(arg2).getMusic_map());
					mMediaPlayer.start();
				}else{
					if(cursor_for_music==arg2){
						if(mMediaPlayer.isPlaying()){
						mMediaPlayer.pause();
						btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_play_normal);
						}else{
							mMediaPlayer.start();
							btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_pause_normal);
						}
					}else{
					mMediaPlayer.reset();
					try {
						mMediaPlayer.setDataSource(listdata_music.get(arg2).getMusic_path());
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					try {
						mMediaPlayer.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					cursor_for_music=arg2;
					btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_pause_normal);
					iv_bottom_map.setBackgroundDrawable(listdata_music.get(arg2).getMusic_map());
					tv_music_artist.setText(listdata_music.get(arg2).getMusic_artist());
					tv_music_name.setText(listdata_music.get(arg2).getMusic_name());
					mMediaPlayer.start();
					
				}
				
				
				}
			}
        	
        });
        btn_btn_start_or_pause.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				convert_start_or_pause();
			}
        	
        });
        btn_next_songs.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				next_song();
				
			}
        	
        });
        bottom_view.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
////				Intent intent=new Intent(MainActivity.this,PlayActivity.class);
//				startActivity(intent);
			}
        	
        });
        if(mMediaPlayer==null){
        	mMediaPlayer =new MediaPlayer();
        	try {
				mMediaPlayer.setDataSource(listdata_music.get(0).getMusic_path());
			} catch (Exception e) {
			
				e.printStackTrace();
			}
        }
        mMediaPlayer.setOnCompletionListener(new OnCompletionListener(){

    			@Override
    			public void onCompletion(MediaPlayer arg0) {
    				play_mode();

    			}
            	
            });
     
        
        
    }
    
    public void play_mode(){
    	
    	 switch(PLAY_MODE){	
			case 3:
				//ÀÊª˙
				cursor_for_music=(int)(Math.random()*listdata_music.size());
				cursor_for_music--;
				next_song();
				break;
			case 4:
				//µ•«˙—≠ª∑
				cursor_for_music--;
				next_song();
				break;
			case 1://¥˝≤‚+
				//—≠ª∑≤•∑≈
				if(cursor_for_music==listdata_music.size()-1){
					cursor_for_music=-1;
				}
				next_song();
				
				break;
			case 2://¥˝≤‚
				//À≥–Ú≤•∑≈
				if(cursor_for_music==listdata_music.size()-1){
					mMediaPlayer.reset();
				}
				next_song();
				break;
			}
			
    }


    public void next_song() {
    	if(mMediaPlayer==null){
			mMediaPlayer =new MediaPlayer();
			try {
				mMediaPlayer.setDataSource(listdata_music.get(0).getMusic_path());

				
			} catch (Exception e) {
				e.printStackTrace();
			} 
			try {
				mMediaPlayer.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			cursor_for_music=0;
			btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_pause_normal);
			iv_bottom_map.setBackgroundDrawable(listdata_music.get(0).getMusic_map());
			tv_music_artist.setText(listdata_music.get(0).getMusic_artist());
			tv_music_name.setText(listdata_music.get(0).getMusic_name());
			mMediaPlayer.start();
    	}else{
    		mMediaPlayer.reset();
    		if(cursor_for_music>=listdata_music.size()-1){
    			cursor_for_music=0;
			}else{
			cursor_for_music++;
			}
			try {
				mMediaPlayer.setDataSource(listdata_music.get(cursor_for_music).getMusic_path());
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			try {
				mMediaPlayer.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_pause_normal);
			iv_bottom_map.setBackgroundDrawable(listdata_music.get(cursor_for_music).getMusic_map());
			tv_music_artist.setText(listdata_music.get(cursor_for_music).getMusic_artist());
			tv_music_name.setText(listdata_music.get(cursor_for_music).getMusic_name());
			mMediaPlayer.start();
    	}
		
	}


	protected void convert_start_or_pause() {
		if(mMediaPlayer==null){
			btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_pause_normal);
			mMediaPlayer =new MediaPlayer();
			try {
				mMediaPlayer.setDataSource(listdata_music.get(0).getMusic_path());

				
			} catch (Exception e) {
				e.printStackTrace();
			} 
			try {
				mMediaPlayer.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			cursor_for_music=0;
			mMediaPlayer.start();
		}else{
			if(mMediaPlayer.isPlaying()){
				mMediaPlayer.pause();
				btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_play_normal);
			}else{
				btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_pause_normal);
				mMediaPlayer.start();
				
			}
		}
		
	}


	private void initialize() {
    	lv_music=(ListView) findViewById(R.id.lv_music);
    	btn_btn_start_or_pause=(Button) findViewById(R.id.btn_btn_start_or_pause);
    	btn_next_songs=(Button) findViewById(R.id.btn_next_songs);
    	bottom_view=findViewById(R.id.ll_view_bottom);
    	tv_music_artist=(TextView) findViewById(R.id.tv_music_artist);
    	
    	tv_music_name=(TextView) findViewById(R.id.tv_music_name);
    	iv_bottom_map=(ImageView) findViewById(R.id.iv_bottom_map);
    	
	}


	


	


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public static void get_playactivity_data(int id,String music_name,String music_artist){
    	btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_pause_normal);
    	tv_music_name.setText(music_name);
    	tv_music_artist.setText(music_artist);
    	if(mMediaPlayer.isPlaying()){
    		btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_pause_normal);
    	}else{
    		btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_play_normal);
    	}
    }
    void search_musicInfo() {
    	ContentResolver conResolver=getContentResolver();
		Cursor cursor=conResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
			MusicInfo musicInfo=new MusicInfo();
			String music_name=cursor.getString(cursor.getColumnIndex("_display_name"));
			String music_path=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
			String music_artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
			
			
			//≤‚ ‘“Ù¿÷√˚
			if(music_name.endsWith(".mp3"))
			musicInfo.setMusic_name(music_name.replace(".mp3", ""));
			if(music_name.endsWith(".wav"))
			musicInfo.setMusic_name(music_name.replace(".wav", ""));	
				if(music_name.endsWith(".flac"))
				musicInfo.setMusic_name(music_name.replace(".flac", ""));
					if(music_name.endsWith(".m4a"))
					musicInfo.setMusic_name(music_name.replace(".m4a", ""));						
						if(music_name.endsWith(".amr"))
						musicInfo.setMusic_name(music_name.replace(".amr", ""));
			
			
			musicInfo.setMusic_path(music_path);
			musicInfo.setMusic_map(getBitmap(cursor));
			musicInfo.setPlay_view_yes_no(for_play_view_yes_no);
			musicInfo.setMusic_artist(music_artist);
			listdata_music.add(musicInfo);
		
		}
	}
private BitmapDrawable getBitmap(Cursor cursor) {
		
		Bitmap bmap=null;
		 long songid = -1,albumid = -1;
	     songid = cursor.getLong(0);
	     albumid = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
	        if (albumid < 0 && songid < 0) {
	            throw new IllegalArgumentException("");
	        }
	        try {
	            if (albumid < 0) {
	                Uri uri = Uri.parse("content://media/external/audio/media/" + songid + "/albumart");
	                ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "r");
	                if (pfd != null) {
	                    FileDescriptor fd = pfd.getFileDescriptor();
	                    bmap = BitmapFactory.decodeFileDescriptor(fd);
	                }
	            } else {
	                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
	                ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "r");
	                if (pfd != null) {
	                    FileDescriptor fd = pfd.getFileDescriptor();
	                    bmap = BitmapFactory.decodeFileDescriptor(fd);
	                }
	            }
	        } catch (FileNotFoundException ex) {
	            //
	        }
	        BitmapDrawable bmpDraw;
	        if(bmap==null){
	        	 bmpDraw= (BitmapDrawable) getResources().getDrawable(R.drawable.img_album_background);
	        	 for_play_view_yes_no=false;
			}else{
				 bmpDraw = new BitmapDrawable(bmap);
				 for_play_view_yes_no=true;
			}
		return bmpDraw;
		
	}
}
