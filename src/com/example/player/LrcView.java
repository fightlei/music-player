package com.example.player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.domain.LrcObject;




import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;

import android.widget.TextView;
import android.widget.Toast;

public class LrcView extends TextView {
	
	private int index;
	List<LrcObject> lrcObjects ;
	private String path;
	

	public List<LrcObject> getLrcObjects() {
		return lrcObjects;
	}


	public void setLrcObjects(List<LrcObject> lrcObjects) {
		this.lrcObjects = lrcObjects;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path,String name) {
		this.path = path+"/"+name;
		lrcObjects = read(this.path);
	}


	public LrcView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	public LrcView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public LrcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void onDraw(Canvas canvas) {
		
		if(lrcObjects!=null&&!lrcObjects.isEmpty()){

		int offX;
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(15);
		int offY = getHeight()/2;
		float alpha = getAlpha();
		for (int i = index-1; i >=0 ; i--) {
			offX = getWidth()/2-lrcObjects.get(i).getContent().length()/2*15;
			alpha-=20;
			paint.setAlpha((int)alpha);
			offY-=30;
				canvas.drawText(lrcObjects.get(i).getContent(), offX, offY, paint);
				
		}
		paint.setColor(getResources().getColor(R.color.pink));
		paint.setTextSize(18);
		paint.setAlpha(225);
		alpha = 225;
		offY = getHeight()/2;
		offX = getWidth()/2-lrcObjects.get(index).getContent().length()/2*18;
		
		canvas.drawText(lrcObjects.get(index).getContent(), offX, offY, paint);
		paint.setColor(Color.WHITE);
		paint.setTextSize(15);
		for(int i =index+1 ; i<lrcObjects.size() ;i++){
			offX = getWidth()/2-lrcObjects.get(i).getContent().length()/2*15;
			offY+=30;
			alpha-=20;
			paint.setAlpha((int)alpha);
			canvas.drawText(lrcObjects.get(i).getContent(), offX, offY, paint);
			
		}
		}else{
			this.setText("好音质， 天天动听！");
			this.setTextSize(20);
			this.setTextColor(Color.WHITE);
			this.setGravity(Gravity.CENTER);
			
		}
		super.onDraw(canvas);
	}
	
	 private  List<LrcObject> read(String path) {
		 
			File lrcFile = new File(path);
			BufferedReader reader = null;
			List<LrcObject> lrcObjects = new ArrayList<LrcObject>();
			String reg = "^\\["+"\\d{2}"+":"+"\\d{2}"+"\\."+"\\d{2}"+"\\]";
			Pattern pattern = Pattern.compile(reg);
			
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(lrcFile), "UTF-8"));
				LrcObject lrcObject;
				LrcObject previousLrcObject = new LrcObject();
				String lrcLine;
				while((lrcLine=reader.readLine())!=null){
					Matcher matcher = pattern.matcher(lrcLine);
					
					if(matcher.find()){
						int minute = Integer.parseInt(lrcLine.substring(1, 3));
						int second = Integer.parseInt(lrcLine.substring(4, 6));
						int millsecond = Integer.parseInt(lrcLine.substring(7, 9));
						Integer startTime = Integer.valueOf(minute*60*1000+second*1000+millsecond);
						String content = lrcLine.substring(10);
						previousLrcObject.setEndTime(startTime-1);
						lrcObject = new LrcObject(startTime,content);
						previousLrcObject=lrcObject;
						lrcObjects.add(lrcObject);
					}
					
				}
			}catch(FileNotFoundException e){
			}catch (Exception e) {
				e.printStackTrace();
				
			}finally{
				try {
					if(reader!=null)
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
					
				}
			}
			return lrcObjects;
		}


	

}
