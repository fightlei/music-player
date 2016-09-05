package com.example.player;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.data.MusicInfo;
import com.example.server.StreamTools;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Start extends Activity {
	
	private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
	private boolean for_play_view_yes_no;
	public static List<MusicInfo> listdata_music =new ArrayList<MusicInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_start);
		startA();
		
	}

	void search_musicInfo() {
    	ContentResolver conResolver=getContentResolver();
		Cursor cursor=conResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
			MusicInfo musicInfo=new MusicInfo();
			String music_name=cursor.getString(cursor.getColumnIndex("_display_name"));
			String music_path=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
			String music_artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
			String music_special=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
			
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
			musicInfo.setMusic_special(music_special);
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


public void startA(){
	
	
	new Thread(){
		public void run(){
			search_musicInfo();
			MainActivity.listdata_music=listdata_music;
			
			
			try {
				String path = "http://192.168.214.1:8080/WebForXMLAndJSON/ServletFor";
				URL url;
				url = new URL(path);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setRequestMethod("POST");
				if(connection.getResponseCode()==200){
					InputStream inputStream  = connection.getInputStream();
					byte data [] =StreamTools.read(inputStream);
					JSONArray jsonArray = new JSONArray(new String(data,"UTF-8"));

					PlayerView.song_download = new ArrayList<MusicInfo>();
					
					PlayerView.maps = new ArrayList<HashMap<String,String>>();
					for(int i=0;i<jsonArray.length();i++){
						HashMap<String , String> map = new HashMap<String, String>();
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						map.put("songname", jsonObject.getString("songname"));
						map.put("songartist", jsonObject.getString("songartist"));
//						musicInfo.setMusic_path(music_path);
						PlayerView.maps.add(map);
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
					 
					 


								
								
				
					

			
			

			
			
			
			Intent intent=new Intent(Start.this,PlayerView.class);
			startActivity(intent);
			finish();
		}
		
	}.start();	
}


}
	

