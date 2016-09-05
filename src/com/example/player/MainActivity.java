package com.example.player;


import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.data.Adapter_for_some;
import com.example.data.ExpandableAdapter;
import com.example.data.MusicInfo;

import com.example.player.R;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * MainActivity完成音乐播放，控件监听等诸多功能的工具类
 */
public class MainActivity {
	
	RelativeLayout rl_visibility_player_view;
	
	public static List<MusicInfo> listdata_music =new ArrayList<MusicInfo>();
	
	

	private ExpandableListView elv_show_song_expandable_list;
	private List<List<String>> list_data_child=new ArrayList<List<String>>();
	private ListView lv_music;
	private  TextView tv_music_name;
	private  TextView tv_music_artist;
	private   Button btn_btn_start_or_pause;
	private Button btn_next_songs;
	
	
	public static MediaPlayer mMediaPlayer;
	
	public static int cursor_for_music; 
	private static View bottom_view;

	private ImageView iv_bottom_map;
	
	public static int PLAY_MODE=1;
	
	private boolean for_play_view_yes_no;
	
	//PlayActivity控件
	Button btn_play_song;
	TextView tv_songname_play;
	TextView tv_artist_play;
	View ll_play_background;
	
	private ArrayList<List<MusicInfo>> count=new ArrayList();
	
	public static List<MusicInfo> temp_music_data;
	private List<MusicInfo> last_temp_music_data;
	
	
	float x;
	float y;
	
	private Context context;
	
	

	public ExpandableListView getElv_show_song_expandable_list() {
		return elv_show_song_expandable_list;
	}



	public void setElv_show_song_expandable_list(
			ExpandableListView elv_show_song_expandable_list) {
		this.elv_show_song_expandable_list = elv_show_song_expandable_list;
	}



	public ListView getLv_music() {
		return lv_music;
	}



	public void setLv_music(ListView lv_music) {
		this.lv_music = lv_music;
	}


//构造函数MainActivity
	public MainActivity(TextView tv_music_name, TextView tv_music_artist,
			 Button btn_btn_start_or_pause,Button btn_next_songs, ImageView iv_bottom_map,View bottom_view,Context context,RelativeLayout rl_visibility_player_view){
		this.rl_visibility_player_view=rl_visibility_player_view;
		this.context=context;
		this.bottom_view=bottom_view;
		this.tv_music_name=tv_music_name;
		this.tv_music_artist=tv_music_artist;
		this.btn_btn_start_or_pause=btn_btn_start_or_pause;
		this.btn_next_songs=btn_next_songs;
		this.iv_bottom_map=iv_bottom_map;
		if(listdata_music!=null&&!listdata_music.isEmpty()){
		tv_music_name.setText(listdata_music.get(0).getMusic_name());
		tv_music_artist.setText(listdata_music.get(0).getMusic_artist());
		iv_bottom_map.setBackgroundDrawable(listdata_music.get(0).getMusic_map());
		}
		
		temp_music_data=listdata_music;
		
		monitor_event();
	}
	

	
	//为本地音乐expandable_listview设置相应的adapter
	
	/*
	 * //type_flag 
	 * 0表示主要本地音乐
	 * 1表示经歌手或专辑列表进入expandable_listview
	 * 
	 * group_position表示listview的被点击项目数
	 */
	public  void set_local_music_Value(Context context,int type_flag ,int group_position){
		
//		tv_music_name.setText(listdata_music.get(0).getMusic_name());
//		tv_music_artist.setText(listdata_music.get(0).getMusic_artist());
//		iv_bottom_map.setBackgroundDrawable(listdata_music.get(0).getMusic_map());
		switch(type_flag){
		case 0:
			temp_music_data=listdata_music;
			for(int i=0;i<listdata_music.size();i++){
				ArrayList<String> one=new ArrayList<String>();
				one.add("播放");
				one.add("添加到我的最爱");
				one.add("删除");
				one.add("歌曲信息");
				list_data_child.add(one);}
				ExpandableAdapter adapter=new ExpandableAdapter(listdata_music,list_data_child,context);
				elv_show_song_expandable_list.setGroupIndicator(null);
				elv_show_song_expandable_list.setAdapter(adapter);
			break;
		case 1:
			temp_music_data=count.get(group_position);
			for(int i=0;i<listdata_music.size();i++){
				ArrayList<String> one=new ArrayList<String>();
				one.add("播放");
				one.add("添加到我的最爱");
				one.add("删除");
				one.add("歌曲信息");
				list_data_child.add(one);}
			ExpandableAdapter adapter1=new ExpandableAdapter(count.get(group_position),list_data_child,context);
			elv_show_song_expandable_list.setGroupIndicator(null);
			elv_show_song_expandable_list.setAdapter(adapter1);
			break;
		case 3:  //代表我的最爱
			List<MusicInfo> data_myfavorite = new ArrayList<MusicInfo>();
			SharedPreferences preferences = context.getSharedPreferences("favorite", Context.MODE_PRIVATE);
			for(MusicInfo single_music : listdata_music){
				if(preferences.getString(single_music.getMusic_name(), "").equals(single_music.getMusic_name())){
					data_myfavorite.add(single_music);
				}
			}
			for(int i=0;i<listdata_music.size();i++){
				ArrayList<String> one=new ArrayList<String>();
				one.add("播放");
				one.add("删除");
				one.add("歌曲信息");
				list_data_child.add(one);}
				ExpandableAdapter adapter3=new ExpandableAdapter(data_myfavorite,list_data_child,context);
				elv_show_song_expandable_list.setGroupIndicator(null);
				elv_show_song_expandable_list.setAdapter(adapter3);
			break;
		case 4:  //代表我的下载，即下载列表
			for(int i=0;i<listdata_music.size();i++){
				ArrayList<String> one=new ArrayList<String>();
				one.add("播放");
				one.add("添加到我的最爱");
				one.add("删除");
				one.add("歌曲信息");
				list_data_child.add(one);}
			ExpandableAdapter adapter4=new ExpandableAdapter(PlayerView.song_download,list_data_child,context);
			elv_show_song_expandable_list.setGroupIndicator(null);
			elv_show_song_expandable_list.setAdapter(adapter4);
			break;
		}
		monetor_for_expandable_list();
		}
	
	//为歌手listview设置相应的adapter
	public void set_artist_widget(PlayerView p){
		
		count.clear();
		for(int i=0;i<listdata_music.size();i++){
			boolean flag=true;
			for(int k=0;k<count.size();k++){
				if(count.get(k).get(0).getMusic_artist().equals((listdata_music.get(i).getMusic_artist()))){
					flag=false;
					break;
				}
			}
			if(flag||i==0){
				List<MusicInfo> temp=new ArrayList();
				temp.add(listdata_music.get(i));
			for(int j=i+1;j<listdata_music.size();j++){
				if(listdata_music.get(i).getMusic_artist().equalsIgnoreCase(listdata_music.get(j).getMusic_artist())){
					temp.add(listdata_music.get(j));
				}
				
			}
			count.add(temp);
			}
			
		}

		Adapter_for_some adapter=new Adapter_for_some(count,p,1);
		lv_music.setAdapter(adapter);
		
	}
	
	//为专辑listview设置数据
	public void set_special_widget(PlayerView p) {

		count.clear();
		for(int i=0;i<listdata_music.size();i++){
			boolean flag=true;
			for(int k=0;k<count.size();k++){
				if(count.get(k).get(0).getMusic_special().equals((listdata_music.get(i).getMusic_special()))){
					flag=false;
					break;
				}
			}
			if(flag||i==0){
				List<MusicInfo> temp=new ArrayList();
				temp.add(listdata_music.get(i));
			for(int j=i+1;j<listdata_music.size();j++){
				if(listdata_music.get(i).getMusic_special().equalsIgnoreCase(listdata_music.get(j).getMusic_special())){
					temp.add(listdata_music.get(j));
				}
				
			}
			count.add(temp);
			}
			
		}
		Adapter_for_some adapter=new Adapter_for_some(count,p,2);
		lv_music.setAdapter(adapter);
		
	}
	
	
		
	
	private void monetor_for_expandable_list(){

		elv_show_song_expandable_list.setOnGroupClickListener(new OnGroupClickListener(){

			@Override
			public boolean onGroupClick(ExpandableListView arg0, View arg1,
					int arg2, long arg3) {

				if(x>380){
//					if(x>964){
					return false;
				}else{
				play_music(arg2,temp_music_data);
				return true;
				}
				
				
			}
    		
    	});
		elv_show_song_expandable_list.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				x=arg1.getX();
				y=arg1.getY();
//				System.out.println("X坐标"+arg1.getX());
//				System.out.println("Y坐标"+arg1.getY());
				return false;
			}
			
		});
		elv_show_song_expandable_list.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int groupId, int childId, long arg4) {
				switch (childId) {
						case 0:

							break;
						case 1:

							SharedPreferences preferences = context.getSharedPreferences("favorite", Context.MODE_PRIVATE);
							Editor editor = preferences.edit();
							editor.putString(temp_music_data.get(groupId).getMusic_name(), temp_music_data.get(groupId).getMusic_name());
							editor.commit();
							ImageView imageView = new ImageView(context);
							RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
							layoutParams.leftMargin=220;
							layoutParams.topMargin=300;
							imageView.setBackgroundResource(R.drawable.icon_flaying_favorite);
							rl_visibility_player_view.addView(imageView,layoutParams);
							
							Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoomin);
							animation.setFillAfter(true);
							imageView.startAnimation(animation);
							
							//实现添加到我的最爱
							break;
						case 2:

							break;
						case 3:

							break;
				}
				return false;
			}
			
		});

	}

    private void monitor_event() {


        btn_btn_start_or_pause.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				convert_start_or_pause(temp_music_data);	
			}
        	
        });
        btn_next_songs.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				next_song();
				
			}
        	
        });

        
        //处理刚启动时mMediaPlayer为空的情况
        if(mMediaPlayer==null){
        	mMediaPlayer =new MediaPlayer();
        	try {
				mMediaPlayer.setDataSource(temp_music_data.get(0).getMusic_path());
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
		mMediaPlayer.pause();
        
        }
        mMediaPlayer.setOnCompletionListener(new OnCompletionListener(){

    			@Override
    			public void onCompletion(MediaPlayer arg0) {
    				play_mode();

    			}
            	
            });
       
		
    }
    
    public void play_music(int arg2,List<MusicInfo> listdata_music){
    	
    	

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
			if(cursor_for_music==arg2&&last_temp_music_data==temp_music_data){
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
    	last_temp_music_data=listdata_music;
    }
    



	public  void play_mode(){
    	
    	 switch(PLAY_MODE){	
			case 3:
				//随机
				cursor_for_music=(int)(Math.random()*temp_music_data.size());
				tv_songname_play.setText(temp_music_data.get(cursor_for_music).getMusic_name());
		    	 tv_artist_play.setText(temp_music_data.get(cursor_for_music).getMusic_artist());
		    	 if(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).isPlay_view_yes_no()){

						ll_play_background.setBackgroundDrawable(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_map());
						}else{
							ll_play_background.setBackgroundResource(R.drawable.default_bg_hdpi);
						}
				play_music_next(cursor_for_music, temp_music_data);
				break;
			case 4:
				//单曲循环
				tv_songname_play.setText(temp_music_data.get(cursor_for_music).getMusic_name());
		    	 tv_artist_play.setText(temp_music_data.get(cursor_for_music).getMusic_artist());
		    	 if(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).isPlay_view_yes_no()){

						ll_play_background.setBackgroundDrawable(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_map());
						}else{
							ll_play_background.setBackgroundResource(R.drawable.default_bg_hdpi);
						}
				play_music_next(cursor_for_music, temp_music_data);
				break;
			case 1://待测+
				//循环播放
				if(cursor_for_music>=temp_music_data.size()-1){
					cursor_for_music=-1;
				}
				cursor_for_music++;
				tv_songname_play.setText(temp_music_data.get(cursor_for_music).getMusic_name());
		    	 tv_artist_play.setText(temp_music_data.get(cursor_for_music).getMusic_artist());
		    	 if(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).isPlay_view_yes_no()){

						ll_play_background.setBackgroundDrawable(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_map());
						}else{
							ll_play_background.setBackgroundResource(R.drawable.default_bg_hdpi);
						}
				play_music_next(cursor_for_music, temp_music_data);
				
				break;
			case 2://待测
				//顺序播放
				if(cursor_for_music>=temp_music_data.size()-1){
					mMediaPlayer.stop();
					btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_play_normal);
					btn_play_song.setBackgroundResource(R.drawable.icon_play_normal);
				}else{
					cursor_for_music++;
					tv_songname_play.setText(temp_music_data.get(cursor_for_music).getMusic_name());
			    	 tv_artist_play.setText(temp_music_data.get(cursor_for_music).getMusic_artist());
			    	 if(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).isPlay_view_yes_no()){

							ll_play_background.setBackgroundDrawable(MainActivity.temp_music_data.get(MainActivity.cursor_for_music).getMusic_map());
							}else{
								ll_play_background.setBackgroundResource(R.drawable.default_bg_hdpi);
							}
					play_music_next(cursor_for_music, temp_music_data);
				}
				break;
			}
//    		btn_play_song
//        	tv_songname_play
//        	tv_artist_play
//        	ll_play_background
    	 
		
    }


    public  void next_song() {
    	
    	if(PLAY_MODE==3){
    		play_mode();
    	}else{
    		if(cursor_for_music>=temp_music_data.size()-1){
    			cursor_for_music=0;
			}else{
				cursor_for_music++;
			}
    	play_music_next(cursor_for_music, temp_music_data);
    	
    	
//    	if(mMediaPlayer==null){
//			mMediaPlayer =new MediaPlayer();
//			try {
//				mMediaPlayer.setDataSource(temp_music_data.get(0).getMusic_path());
//
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			} 
//			try {
//				mMediaPlayer.prepare();
//			} catch (IllegalStateException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			cursor_for_music=0;
//			btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_pause_normal);
//			iv_bottom_map.setBackgroundDrawable(temp_music_data.get(0).getMusic_map());
//			tv_music_artist.setText(temp_music_data.get(0).getMusic_artist());
//			tv_music_name.setText(temp_music_data.get(0).getMusic_name());
//			mMediaPlayer.start();
//    	}else{
//    		mMediaPlayer.reset();
//    		if(cursor_for_music>=temp_music_data.size()-1){
//    			cursor_for_music=0;
//			}else{
//			cursor_for_music++;
//			}
//			try {
//				mMediaPlayer.setDataSource(temp_music_data.get(cursor_for_music).getMusic_path());
//			} catch (Exception e) {
//				
//				e.printStackTrace();
//			}
//			try {
//				mMediaPlayer.prepare();
//			} catch (IllegalStateException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_pause_normal);
//			iv_bottom_map.setBackgroundDrawable(temp_music_data.get(cursor_for_music).getMusic_map());
//			tv_music_artist.setText(temp_music_data.get(cursor_for_music).getMusic_artist());
//			tv_music_name.setText(temp_music_data.get(cursor_for_music).getMusic_name());
//			mMediaPlayer.start();
//    	}
		
    	}
	}
    
    private void play_music_next(int arg2, List<MusicInfo> listdata_music){
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


	protected  void convert_start_or_pause(List<MusicInfo> listdata_music) {
		
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


//	private void initialize() {
//    	
//    	btn_btn_start_or_pause=(Button) findViewById(R.id.btn_btn_start_or_pause);
//    	btn_next_songs=(Button) findViewById(R.id.btn_next_songs);
//    	bottom_view=findViewById(R.id.ll_view_bottom);
//    	tv_music_artist=(TextView) findViewById(R.id.tv_music_artist);
//    	
//    	tv_music_name=(TextView) findViewById(R.id.tv_music_name);
//    	iv_bottom_map=(ImageView) findViewById(R.id.iv_bottom_map);
//    	
//	}


	


	



    
    public  void get_playactivity_data(){
    	iv_bottom_map.setBackgroundDrawable(temp_music_data.get(cursor_for_music).getMusic_map());
    	tv_music_name.setText(temp_music_data.get(cursor_for_music).getMusic_name());
    	tv_music_artist.setText(temp_music_data.get(cursor_for_music).getMusic_artist());
    	if(mMediaPlayer.isPlaying()){
    		btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_pause_normal);
    	}else{
    		btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_play_normal);
    	}
    }
    
    public void get_playactivity_relation(Button btn_play_song,TextView tv_songname_play,TextView tv_artist_play,View ll_play_background){
    	this.btn_play_song=btn_play_song;
    	this.tv_songname_play=tv_songname_play;
    	
    	this.tv_artist_play=tv_artist_play;
    	this.ll_play_background=ll_play_background;
    }



	

}


