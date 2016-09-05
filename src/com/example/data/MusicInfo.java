package com.example.data;

import android.graphics.drawable.BitmapDrawable;

public class MusicInfo {
	
	private String music_name;
	private String music_path;
	private String music_artist;
	private BitmapDrawable music_map;
	private String music_duration;
	private String music_special;
	private boolean play_view_yes_no;
	
	public String getMusic_special() {
		return music_special;
	}
	public void setMusic_special(String music_special) {
		this.music_special = music_special;
	}
	public boolean isPlay_view_yes_no() {
		return play_view_yes_no;
	}
	public void setPlay_view_yes_no(boolean play_view_yes_no) {
		this.play_view_yes_no = play_view_yes_no;
	}
	public String getMusic_name() {
		return music_name;
	}
	public void setMusic_name(String music_name) {
		this.music_name = music_name;
	}
	public String getMusic_path() {
		return music_path;
	}
	public void setMusic_path(String music_path) {
		this.music_path = music_path;
	}
	public String getMusic_artist() {
		return music_artist;
	}
	public void setMusic_artist(String music_artist) {
		this.music_artist = music_artist;
	}
	public BitmapDrawable getMusic_map() {
		return music_map;
	}
	public void setMusic_map(BitmapDrawable music_map) {
		this.music_map = music_map;
	}

	
}
