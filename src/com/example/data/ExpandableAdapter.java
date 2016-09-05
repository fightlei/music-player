package com.example.data;

import java.util.List;

import com.example.player.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableAdapter extends BaseExpandableListAdapter {
	
	private List<MusicInfo> list_data_group;
	private List<List<String>> list_data_child;
	private Context context;
	private LayoutInflater linflater;
	
	public ExpandableAdapter(List<MusicInfo> list_data_group,List<List<String>> list_data_child,Context context){
		this.list_data_group=list_data_group;
		this.list_data_child=list_data_child;
		this.context=context;
		this.linflater=LayoutInflater.from(context);
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		
		return list_data_child.get(arg0).get(arg1);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {

		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		convertView=linflater.inflate(R.layout.child_item, null);
		TextView textView = (TextView) convertView.findViewById(R.id.expandable_child_tv);
		textView.setText(list_data_child.get(groupPosition).get(childPosition));
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		
		return list_data_child.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		
		return list_data_group.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		
		return list_data_group.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		convertView=linflater.inflate(R.layout.expandablelistview_item, null);
		TextView tv_expandable_item_song_count=(TextView) convertView.findViewById(R.id.tv_expandable_item_song_count);
		TextView tv_expandable_item_song_name=(TextView) convertView.findViewById(R.id.tv_expandable_item_song_name);
		TextView tv_expandable_item_song_artist=(TextView) convertView.findViewById(R.id.tv_expandable_item_song_artist);
		ImageView iv_expandable_item_arrow=(ImageView) convertView.findViewById(R.id.iv_expandable_item_arrow);
//		tv_expandable_item_song_count.setText(groupPosition);
		tv_expandable_item_song_count.setText(groupPosition+1+"");
		tv_expandable_item_song_name.setText(list_data_group.get(groupPosition).getMusic_name());
		tv_expandable_item_song_artist.setText(list_data_group.get(groupPosition).getMusic_artist());
		if(isExpanded){
			iv_expandable_item_arrow.setImageResource(R.drawable.up_dialog_standard_return_icon);
		}else{
			iv_expandable_item_arrow.setImageResource(R.drawable.down);
		}
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
	
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		
		return true;
	}

}
