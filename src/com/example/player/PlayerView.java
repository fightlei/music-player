package com.example.player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.data.MusicInfo;
import com.example.server.StreamTools;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.ViewFlipper;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerView extends TabActivity {
	

	
	public static List<MusicInfo> song_download;
	
	
	//tab第三页
	Button btn_search;
	EditText et_search_song;
	ListView lv_search_result;
	
	//tabhost第二项“推荐”界面控件
	public static ListView lv_music_recommend;
	public static List<HashMap<String, String>> maps;

	Context context ;
	public Context getContext(){
		context =this;
		return context;
	}

	
	ViewFlipper viewFlipper ;
	PopupWindow popupWindow ;
	boolean popflag = false;
	
	Handler handler;
	Runnable runable;
	
	private Timer mTimer;
	
	View ll_btn_local_music;
	View ll_btn_artist;
	View ll_btn_special;
	View ll_btn_my_favorite;
	View ll_btn_my_dowload;
	View ll_btn_recent_play;
	
	static TabHost tabhost;
	
	View bottom_view;
	public static ProgressBar pbar_music_progress;
	ImageView iv_bottom_map;
	static TextView tv_music_name;
	static TextView tv_music_artist;
	static Button btn_btn_start_or_pause;
	static Button btn_next_songs;
	
	//布局Id
	RelativeLayout rl_visibility_player_view;//第一界面的总布局
	LinearLayout ll_titile;
	LinearLayout ll_visibility_tabhost;
	LinearLayout layout;//要动态加载或删除的布局
	
	
	
	//传递给MainActivity的控件信息
	public static MainActivity main_activity;
	ListView lv_music;
	ExpandableListView elv_show_song_expandable_list;
	
	
	private int flag_judge_which_view;//判断当前处于第几界面
	
	
	GestureDetector gestureDetector;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_player);
		initialize();

		createMenu();
		
		music_recommend();
		
		
//		gestureDetector =new GestureDetector(this);
////		gestureDetector=new GestureDetector.SimpleOnGestureListener();
//		bottom_view.setOnTouchListener(this);
		
		
		
		
		
		
		btn_search.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
						List<Map<String , Object >> mapp = new ArrayList<Map<String,Object>>();
						for(MusicInfo musicInfo : MainActivity.listdata_music){
							if(musicInfo.getMusic_name().equals(et_search_song.getText().toString())){
								Map<String, Object > map = new HashMap<String, Object>();
								map.put("songname", musicInfo.getMusic_name());
								map.put("songartist", musicInfo.getMusic_artist());

								mapp.add(map);
							}
						}
						for(Map mm : maps){
							if(mm.get("songname").equals(et_search_song.getText().toString())){
								Map<String, Object > map = new HashMap<String, Object>();
								map.put("songname", mm.get("songname"));
								map.put("songartist", mm.get("songartist"));
								map.put("image1", R.drawable.uuu);
								map.put("image2", R.drawable.icon_download);
								mapp.add(map);
							}
						}
						if(mapp.size()>0){
							SimpleAdapter adapter = new SimpleAdapter(PlayerView.this,mapp,R.layout.expandablelistview_child_item,new String[]{"songname","songartist","image1","image2"},new int[]{R.id.expandable_child_tv,R.id.tv_search_songartist,R.id.iv_local_or_net,R.id.iv_dowload_search});
							lv_search_result.setAdapter(adapter);
							lv_search_result.setOnItemClickListener(new OnItemClickListener(){

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									music_search_download(arg1,arg2,2);
									
								}
								
							});
						}else{
							List<HashMap<String , Object >> mapmap = new ArrayList<HashMap<String,Object>>();
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("name", "没有搜索到歌曲");
							mapmap.add(map);
							SimpleAdapter adapter = new SimpleAdapter(PlayerView.this,mapmap,R.layout.expandablelistview_child_item,new String[]{"name"},new int[]{R.id.expandable_child_tv});
							lv_search_result.setAdapter(adapter);
						}
						
//						try {
//							String path = "http://192.168.214.1:8080/WebForXMLAndJSON/ServletFor";
//							URL url;
//							url = new URL(path);
//							HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//							connection.setConnectTimeout(5000);
//							connection.setRequestMethod("GET");
//							if(connection.getResponseCode()==200){
//								System.out.println("此处");
//								InputStream inputStream  = connection.getInputStream();
//								byte data [] =StreamTools.read(inputStream);
//								System.out.println("长度"+data.length);
//								JSONArray jsonArray = new JSONArray(new String(data,"UTF-8"));
//
//								List<HashMap<String, String>> maps = new ArrayList<HashMap<String,String>>();
//								for(int i=0;i<jsonArray.length();i++){
//									HashMap<String , String> map = new HashMap<String, String>();
//									JSONObject jsonObject = jsonArray.getJSONObject(i);
//									map.put("songname", jsonObject.getString("songname"));
//									map.put("songartist", jsonObject.getString("songartist"));
//									maps.add(map);
//								}
//								System.out.println("maps长度"+maps.size());
//								SimpleAdapter adapter = new SimpleAdapter(PlayerView.this,maps,R.layout.recommend_item,new String[]{"songname","songartist"},new int[]{R.id.tv_recommend_item_songname,R.id.tv_recommend_item_artist});
//								 lv_music_recommend.setAdapter(adapter);
//							}
//
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
						
				
			
				
			}
			
		});
		
		
		
		
		
		
		
		
	
		
		ll_btn_local_music.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				flag_judge_which_view=1;
//				Intent intent=new Intent(PlayerView.this,MainActivity.class);
//				startActivity(intent);
//				ll_player_view.setVisibility(View.GONE);
				LayoutInflater inflater=LayoutInflater.from(PlayerView.this);
//				layout=(LinearLayout) inflater.inflate(R.layout.activity_main, null).findViewById(R.id.ll_visibility_local_music);
						
				layout=(LinearLayout) inflater.inflate(R.layout.activity_expandable_listview, null).findViewById(R.id.ll_visibility_expandable_local_music);
//				rl_visibility_player_view.removeView(ll_titile);
////				rl_visibility_player_view.removeView(ll_visibility_tabhost);
//				rl_visibility_player_view.removeView(PlayerView.tabhost);
				ll_titile.setVisibility(View.GONE);
				PlayerView.tabhost.setVisibility(View.GONE);
				ll_visibility_tabhost.addView(layout);
				initialize_local_music_widget(0,0);
				
				
			}
			
		});
		ll_btn_artist.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				flag_judge_which_view=1;

				LayoutInflater inflater=LayoutInflater.from(PlayerView.this);
				layout=(LinearLayout) inflater.inflate(R.layout.activity_main, null).findViewById(R.id.ll_visibility_local_music);
				ll_titile.setVisibility(View.GONE);
				PlayerView.tabhost.setVisibility(View.GONE);
//				ll_visibility_tabhost.setVisibility(View.GONE);
				ll_visibility_tabhost.addView(layout);
				initialize_artist_widget();
			}
			
		});
		ll_btn_special.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				flag_judge_which_view=1;

				LayoutInflater inflater=LayoutInflater.from(PlayerView.this);
				layout=(LinearLayout) inflater.inflate(R.layout.activity_main, null).findViewById(R.id.ll_visibility_local_music);
				ll_titile.setVisibility(View.GONE);
				PlayerView.tabhost.setVisibility(View.GONE);
//				ll_visibility_tabhost.setVisibility(View.GONE);
				ll_visibility_tabhost.addView(layout);
				initialize_special_widget();
				
			}
			
		});
        bottom_view.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(PlayerView.this,PlayActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.down_to_up_in,R.anim.up_to_down_out);
			}
        	
        });
        
        ll_btn_my_favorite.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				flag_judge_which_view=1;
//				Intent intent=new Intent(PlayerView.this,MainActivity.class);
//				startActivity(intent);
//				ll_player_view.setVisibility(View.GONE);
				LayoutInflater inflater=LayoutInflater.from(PlayerView.this);
//				layout=(LinearLayout) inflater.inflate(R.layout.activity_main, null).findViewById(R.id.ll_visibility_local_music);
						
				layout=(LinearLayout) inflater.inflate(R.layout.activity_expandable_listview, null).findViewById(R.id.ll_visibility_expandable_local_music);
//				rl_visibility_player_view.removeView(ll_titile);
////				rl_visibility_player_view.removeView(ll_visibility_tabhost);
//				rl_visibility_player_view.removeView(PlayerView.tabhost);
				ll_titile.setVisibility(View.GONE);
				PlayerView.tabhost.setVisibility(View.GONE);
				ll_visibility_tabhost.addView(layout);
				initialize_local_music_widget(3,0);
				
				
			}
			
		});
       
        ll_btn_my_dowload.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				flag_judge_which_view=1;
//				Intent intent=new Intent(PlayerView.this,MainActivity.class);
//				startActivity(intent);
//				ll_player_view.setVisibility(View.GONE);
				LayoutInflater inflater=LayoutInflater.from(PlayerView.this);
//				layout=(LinearLayout) inflater.inflate(R.layout.activity_main, null).findViewById(R.id.ll_visibility_local_music);
						
				layout=(LinearLayout) inflater.inflate(R.layout.activity_expandable_listview, null).findViewById(R.id.ll_visibility_expandable_local_music);
//				rl_visibility_player_view.removeView(ll_titile);
////				rl_visibility_player_view.removeView(ll_visibility_tabhost);
//				rl_visibility_player_view.removeView(PlayerView.tabhost);
				ll_titile.setVisibility(View.GONE);
				PlayerView.tabhost.setVisibility(View.GONE);
				ll_visibility_tabhost.addView(layout);
				initialize_local_music_widget(4,0);
			}
        	
        });
        

        
	}
	protected void initialize_special_widget() {
		lv_music=(ListView) layout.findViewById(R.id.lv_music);
		main_activity.setLv_music(lv_music);
		/*
		 * cfgggggfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff
		 */
		
		main_activity.set_special_widget(this);
		lv_music.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				layout.setVisibility(View.GONE);
				LayoutInflater inflater=LayoutInflater.from(PlayerView.this);
				layout=(LinearLayout) inflater.inflate(R.layout.activity_expandable_listview, null).findViewById(R.id.ll_visibility_expandable_local_music);
				ll_visibility_tabhost.addView(layout);
				initialize_local_music_widget(1,arg2);
			}
			
		});
		
		
	}
	protected void initialize_artist_widget() {
		lv_music=(ListView) layout.findViewById(R.id.lv_music);
		main_activity.setLv_music(lv_music);
		
		main_activity.set_artist_widget(this);
		
		lv_music.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				layout.setVisibility(View.GONE);
				LayoutInflater inflater=LayoutInflater.from(PlayerView.this);
				layout=(LinearLayout) inflater.inflate(R.layout.activity_expandable_listview, null).findViewById(R.id.ll_visibility_expandable_local_music);
				ll_visibility_tabhost.addView(layout);
				initialize_local_music_widget(1,arg2);
			}
			
		});
		
	}

	protected void initialize_local_music_widget(int type_flag,int group_position) {
//		lv_music=(ListView) layout.findViewById(R.id.lv_music);
		elv_show_song_expandable_list=(ExpandableListView) layout.findViewById(android.R.id.list);
		main_activity.setElv_show_song_expandable_list(elv_show_song_expandable_list);
		main_activity.set_local_music_Value(this,type_flag,group_position);
		
	}

	private void initialize() {
		tabhost=getTabHost();
		TabWidget tabwidget=tabhost.getTabWidget();
		TabSpec page1=tabhost.newTabSpec("page1").setIndicator("我的").setContent(R.id.ll_local_music);
		tabhost.addTab(page1);
		TabSpec page2=tabhost.newTabSpec("page2").setIndicator("推荐").setContent(R.id.ll_net_music);
		tabhost.addTab(page2);
		TabSpec page3=tabhost.newTabSpec("page3").setIndicator("发现").setContent(R.id.ll_test);
		tabhost.addTab(page3);
		for(int i=0;i<tabwidget.getChildCount();i++){
			tabhost.setPadding(tabhost.getPaddingLeft(), 
					tabhost.getPaddingTop(), tabhost.getPaddingRight(), tabhost.getPaddingBottom()-5);
			View v=tabwidget.getChildAt(i);
			TextView tv1=(TextView) v.findViewById(android.R.id.title);
			tv1.setTextColor(android.graphics.Color.WHITE);
			
		}
		
		//推荐界面控件
		lv_music_recommend=(ListView) findViewById(R.id.lv_recommend);
		
		//搜索界面控件
		btn_search=(Button) findViewById(R.id.btn_search);
		 et_search_song  = (EditText) findViewById(R.id.et_search_song);
		 lv_search_result= (ListView) findViewById(R.id.lv_search_result);
		
		viewFlipper = (ViewFlipper) findViewById(R.id.palyview_viewflipper);
		//第一界面布局按钮初始化
		ll_btn_local_music=findViewById(R.id.ll_btn_local_music);
		ll_btn_artist=findViewById(R.id.ll_btn_artist);
		ll_btn_special=findViewById(R.id.ll_btn_special);
		ll_btn_my_favorite=findViewById(R.id.ll_btn_my_favorite);
		ll_btn_my_dowload=findViewById(R.id.ll_btn_my_dowload);
		ll_btn_recent_play=findViewById(R.id.ll_btn_recent_play);
		
		//底部显示
		bottom_view=findViewById(R.id.ll_view_bottom);
		pbar_music_progress=(ProgressBar) findViewById(R.id.pbar_music_progress);
		iv_bottom_map=(ImageView) findViewById(R.id.iv_bottom_map);
		tv_music_name=(TextView) findViewById(R.id.tv_music_name);
		tv_music_artist=(TextView) findViewById(R.id.tv_music_artist);
		btn_btn_start_or_pause=(Button) findViewById(R.id.btn_btn_start_or_pause);
		btn_next_songs=(Button) findViewById(R.id.btn_next_songs);
		
		
		//布局初始化
		rl_visibility_player_view=(RelativeLayout) findViewById(R.id.rl_visibility_player_view);
		ll_titile=(LinearLayout) findViewById(R.id.ll_titile);
		ll_visibility_tabhost=(LinearLayout) findViewById(R.id.ll_visibility_tabhost_test);
		
		//初始化mainACtivity
		main_activity=new MainActivity(tv_music_name,tv_music_artist,btn_btn_start_or_pause,
				btn_next_songs,iv_bottom_map,bottom_view,this,rl_visibility_player_view);
		
		
		
		
		handler=new Handler();
		runable=new Runnable() {

			@Override
			public void run() {
				pbar_music_progress.setMax(MainActivity.mMediaPlayer.getDuration());
				pbar_music_progress.setProgress(MainActivity.mMediaPlayer.getCurrentPosition());
				if(PlayActivity.sbar_music_progress!=null&&PlayActivity.tv_music_total_time!=null&&
						PlayActivity.tv_music_current_time!=null){
				
				PlayActivity.sbar_music_progress.setMax(MainActivity.mMediaPlayer.getDuration());
				
				PlayActivity.sbar_music_progress.setProgress(MainActivity.mMediaPlayer.getCurrentPosition());
				String text;
				int time_minute=(MainActivity.mMediaPlayer.getDuration()/1000)/60;
				int time_second=MainActivity.mMediaPlayer.getDuration()/1000%60;
				if(time_second<10){
					text=time_minute+":"+"0"+time_second;
				}else{
				text=time_minute+":"+time_second;
				}
				PlayActivity.tv_music_total_time.setText(text);
				
				time_minute=(MainActivity.mMediaPlayer.getCurrentPosition()/1000)/60;
				time_second=MainActivity.mMediaPlayer.getCurrentPosition()/1000%60;
				if(time_second<10){
					text=time_minute+":"+"0"+time_second;
				}else{
				text=time_minute+":"+time_second;
				}
				PlayActivity.tv_music_current_time.setText(text);}
				if(PlayActivity.lrcView!=null&&
						PlayActivity.lrcView.getLrcObjects()!=null&&!PlayActivity.lrcView.getLrcObjects().isEmpty()){
				for(int i=PlayActivity.lrcView.getIndex();i<PlayActivity.lrcView.getLrcObjects().size();i++){
					if(MainActivity.mMediaPlayer.isPlaying()&&PlayActivity.lrcView.getLrcObjects().get(i).getStartTime()<MainActivity.mMediaPlayer.getCurrentPosition()&&PlayActivity.lrcView.getLrcObjects().get(i).getEndTime()>MainActivity.mMediaPlayer.getCurrentPosition()){
						PlayActivity.lrcView.setIndex(i);
						PlayActivity.lrcView.invalidate();
					}
				}
				}
				
				
				

				handler.postDelayed(runable, 200);
			}
		
		};
		handler.post(runable);
		
		
		
		
		
		
		
		
		
		
		
		
		
		//监听播放进度以更新pbar_music_progress  在playActivity一同监听
		/*
	        mTimer=new Timer();
	        TimerTask mTimerTask=new TimerTask() {
				
				@Override
				public void run() {
					
					pbar_music_progress.setMax(MainActivity.mMediaPlayer.getDuration());
					pbar_music_progress.setProgress(MainActivity.mMediaPlayer.getCurrentPosition());
					if(PlayActivity.sbar_music_progress!=null&&PlayActivity.tv_music_total_time!=null&&
							PlayActivity.tv_music_current_time!=null){
					
					PlayActivity.sbar_music_progress.setMax(MainActivity.mMediaPlayer.getDuration());
					
					PlayActivity.sbar_music_progress.setProgress(MainActivity.mMediaPlayer.getCurrentPosition());
					String text;
					int time_minute=(MainActivity.mMediaPlayer.getDuration()/1000)/60;
					int time_second=MainActivity.mMediaPlayer.getDuration()/1000%60;
					if(time_second<10){
						text=time_minute+":"+"0"+time_second;
					}else{
					text=time_minute+":"+time_second;
					}
					PlayActivity.tv_music_total_time.setText(text);
					
					time_minute=(MainActivity.mMediaPlayer.getCurrentPosition()/1000)/60;
					time_second=MainActivity.mMediaPlayer.getCurrentPosition()/1000%60;
					if(time_second<10){
						text=time_minute+":"+"0"+time_second;
					}else{
					text=time_minute+":"+time_second;
					}
					PlayActivity.tv_music_current_time.setText(text);}
					
				}
			};
			mTimer.schedule(mTimerTask, 0, 200);*/
		
	}
    public static void get_playactivity_data(String music_name,String music_artist){
    	tv_music_name.setText(music_name);
    	tv_music_artist.setText(music_artist);
    	if(MainActivity.mMediaPlayer.isPlaying()){
    		btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_pause_normal);
    	}else{
    		btn_btn_start_or_pause.setBackgroundResource(R.drawable.icon_play_normal);
    	}
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		
		switch(flag_judge_which_view){
		case 1:
			
//			rl_visibility_player_view.removeView(layout);
			layout.setVisibility(View.GONE);
//			ll_visibility_tabhost.addView(ll_titile);
//			ll_visibility_tabhost.addView(PlayerView.tabhost);
			ll_titile.setVisibility(View.VISIBLE);
			PlayerView.tabhost.setVisibility(View.VISIBLE);
			flag_judge_which_view=0;
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 0:
			MainActivity.mMediaPlayer=null;

			handler.removeCallbacks(runable);
			
			System.out.println("清空资源");
			return super.onKeyDown(keyCode, event);
			
		}
		return true;
	}
	
	public void createMenu(){

		
		View view = getLayoutInflater().inflate(R.layout.menu, null);
		GridView gridView = (GridView) view.findViewById(R.id.menu);
		List<Map<String,Object>> maps =new ArrayList<Map<String,Object>>();
		String menuData [] =new String[]{"背景","转屏","音量","刷新","设置","快速搜索","检查更新","退出"};
		int [] id = new int[]{R.drawable.screen,R.drawable.iphone,
				R.drawable.graph1,R.drawable.livejournal,R.drawable.setting,
				R.drawable.search,R.drawable.foursquare,R.drawable.blinklist};
		for(int i = 0 ;i<8;i++){
		HashMap<String, Object > map = new HashMap<String, Object>();
		
		map.put("image", id[i]);
		map.put("name", menuData[i]);
		
		maps.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, maps, R.layout.menu_item, new String[]{"image","name"}, new int[]{R.id.iv_itme,R.id.tv_item});
		gridView.setAdapter(adapter);
		popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		popupWindow.setAnimationStyle(R.style.poppupWindowStyle);
		
		gridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				popupWindow.dismiss();
				Toast.makeText(PlayerView.this, arg2+"", 1).show();
				switch(arg2){
				
				case 0:
					viewFlipper.showNext();
					ImageView image1 = (ImageView) findViewById(R.id.iv_bag1);
					ImageView image2 = (ImageView) findViewById(R.id.iv_bag2);
					ImageView image3 = (ImageView) findViewById(R.id.iv_bag3);
					ImageView image4 = (ImageView) findViewById(R.id.iv_bag4);
					ImageView image5 = (ImageView) findViewById(R.id.iv_default);
					Listener listener =new Listener();
					image1.setOnClickListener(listener);
					image2.setOnClickListener(listener);
					image3.setOnClickListener(listener);
					image4.setOnClickListener(listener);
					image5.setOnClickListener(listener);
					
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					break;
				case 7:
					break;
				case 8:
					break;
				
				}
			}
			
		});
		}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {

		popupWindow.showAtLocation(rl_visibility_player_view, Gravity.BOTTOM, 0, 0);
		return false;
	}


	class Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			viewFlipper.showPrevious();
			switch(arg0.getId()){
			
			case R.id.iv_bag1:
				rl_visibility_player_view.setBackgroundResource(R.drawable.back1);
				
				break;
			case R.id.iv_bag2:
				rl_visibility_player_view.setBackgroundResource(R.drawable.back2);
				break;
			case R.id.iv_bag3:
				rl_visibility_player_view.setBackgroundResource(R.drawable.back3);
				break;
			case R.id.iv_bag4:
				rl_visibility_player_view.setBackgroundResource(R.drawable.back4);
				break;
			case R.id.iv_default:
				rl_visibility_player_view.setBackgroundResource(R.drawable.default_bg_hdpi);
				break;
				
			}
			
		}
		
	}
	
	public void music_recommend(){
			 
						 
//						 lv_music_recommend.setOnItemClickListener(new OnItemClickListener(){
//
//							@Override
//							public void onItemClick(AdapterView<?> arg0,
//									View arg1, int arg2, long arg3) {
//								System.out.println("吃出");
//								try {
//									String path = "http://192.168.214.1:8080/WebForXMLAndJSON/";
//									URL url = new URL("");
//									HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//									connection.setRequestMethod("GET");
//									connection.setConnectTimeout(5000);
//									if(connection.getResponseCode()==200){
//										System.out.println("这里");
//										InputStream inputStream = connection.getInputStream();
//										byte [] data = StreamTools.read(inputStream);
//										File file = PlayerView.this.getExternalFilesDir("rwd");
//										Toast.makeText(getApplicationContext(), file.getAbsolutePath(), 1).show();
//									}
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//								
//							}
//							 
//						 });
		if(maps!=null&&!maps.isEmpty()){
		
		SimpleAdapter adapter = new SimpleAdapter(this,maps,R.layout.recommend_item,new String[]{"songname","songartist"},new int[]{R.id.tv_recommend_item_songname,R.id.tv_recommend_item_artist});
		lv_music_recommend.setAdapter(adapter);
		}
		lv_music_recommend.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0,
					View arg1, int arg2, long arg3) {
				music_search_download(arg1,arg2,1);
				
			}
			 
		 });							
		
	}
	
	public void music_search_download(View arg1,int arg2,int flag){
		File file = new File(Environment.getExternalStorageDirectory()+"/Download");
		if(!file.exists()){
			file.mkdirs();
		}
		MusicInfo info = new MusicInfo();
		info.setMusic_name(maps.get(arg2).get("songname"));
		info.setMusic_artist(maps.get(arg2).get("songartist"));
		info.setMusic_path(Environment.getExternalStorageDirectory()+"/Download");
		
		try {
			String path = "http://192.168.214.1:8080/WebForXMLAndJSON/"+maps.get(arg2).get("songname")+".mp3";
			System.out.println(path);
			
			URL url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			int code=connection.getResponseCode();
			if(code==200||code==505){
				System.out.println("这里222");

//				InputStream inputStream = connection.getInputStream();
//				byte [] data = StreamTools.read(inputStream);
//				FileOutputStream outputStream = new FileOutputStream(file);
//				outputStream.write(data,0,data.length);
				if(flag==1){
					Button button = (Button) arg1.findViewById(R.id.btn_recommend_download);
					button.setBackgroundResource(R.drawable.skin_ico_checked);
				}
				if(flag==2){
					ImageView image = (ImageView) arg1.findViewById(R.id.iv_dowload_search);
					image.setImageResource(R.drawable.skin_ico_checked);
				}

//				outputStream.close();
				song_download.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
		


}
