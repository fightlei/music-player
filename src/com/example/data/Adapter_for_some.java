package com.example.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.player.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Adapter_for_some extends BaseAdapter{

	private ArrayList<List<MusicInfo>> count;
	private Context context;
	private LayoutInflater linflater;
	
	//作家代号1，专辑代号2
	private int flag;
	
	public Adapter_for_some(ArrayList<List<MusicInfo>> count,Context context,int flag){
		this.count=count;
		this.context=context;
		this.flag=flag;
		this.linflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return count.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		arg1=linflater.inflate(R.layout.list_view_item_for_some, null);
		TextView tv_list_item_artist_name=(TextView) arg1.findViewById(R.id.tv_list_item_artist_name);
		TextView tv_list_item_songs_count=(TextView) arg1.findViewById(R.id.tv_list_item_songs_count);
		switch(flag){
		case 1:
			tv_list_item_artist_name.setText(count.get(arg0).get(0).getMusic_artist());
			tv_list_item_songs_count.setText(count.get(arg0).size()+"首歌曲");
			break;
		case 2:
			tv_list_item_artist_name.setText(count.get(arg0).get(0).getMusic_special());
			tv_list_item_songs_count.setText(count.get(arg0).size()+"首歌曲");
			break;
		}
		
		return arg1;
	}


}
