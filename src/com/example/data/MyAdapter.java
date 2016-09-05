package com.example.data;

import java.util.List;
import java.util.Map;

import com.example.player.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	
	private List<MusicInfo> list_data;
	private Context context;
	private LayoutInflater linflater;
	
	public MyAdapter(List<MusicInfo> list_data,Context context){
		this.list_data=list_data;
		this.context=context;
		this.linflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		
		return list_data.size();
	}

	@Override
	public Object getItem(int arg0) {
		
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		arg1=linflater.inflate(R.layout.listview_item, null);
		ImageView image=(ImageView) arg1.findViewById(R.id.iv_item_img);
		TextView textv=(TextView) arg1.findViewById(R.id.tv_item_text);
		image.setImageDrawable((BitmapDrawable) list_data.get(arg0).getMusic_map());
		textv.setText((CharSequence) list_data.get(arg0).getMusic_name()+"-"+list_data.get(arg0).getMusic_artist());
		return arg1;
	}

}
