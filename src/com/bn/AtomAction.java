package com.bn;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import static com.bn.Constant.*;

enum ActionType{TS,QS};//填沙|清沙

public class AtomAction implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
	//動作型態
	ActionType at;
	//動作起始xy位置
	float xStart;
	float yStart;
	float xEnd;
	float yEnd;
	//動作半徑
	float r;
	//最小路徑長度
	double length;
	//畫筆ID
	long hbid;
	
	public AtomAction(ActionType at,float xStart,float yStart,float xEnd,float yEnd,float r,long hbid)
	{
		this.at=at;
		this.xStart=xStart;
		this.yStart=yStart;
		this.xEnd=xEnd;
		this.yEnd=yEnd;
		this.r=r;		
		this.hbid=hbid;
		float xSpan=xEnd-xStart;
		float ySpan=yEnd-yStart;
		length=Math.sqrt(xSpan*xSpan+ySpan*ySpan);		//某次噴砂的長度
	}
	
	public void drawSelf(Canvas canvas,Paint paint)
	{		
		paint.reset();//重新設定畫筆
		if(at==ActionType.QS)
		{//動作為清沙
			paint.setColor(0xFF000000);//設定畫筆彩色 	(在解決搽除背景這個問題中起作用)
			paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
			paint.setStyle(Style.STROKE);//設定paint的風格為“空心”
			paint.setStrokeWidth(2*r);	 //設定其寬度		
			canvas.drawLine(xStart, yStart, xEnd, yEnd, paint);	//繪制
			paint.reset();
		}
		else if(at==ActionType.TS)
		{
			paint.setStyle(Style.FILL);//設定畫筆填充模式為實心
			paint.setColor(Color.rgb(SANDCOLOR[0],SANDCOLOR[1],SANDCOLOR[2]));//設定沙子彩色				 
			int steps=(int)(length/hbSize)*4;
			float xSpan=xEnd-xStart;  
			float ySpan=yEnd-yStart;
			float xStep=xSpan/steps;
			Bitmap bm=hmhb.get(hbid);
			for(int i=-1;i<=steps;i++)
			{
				float xc=xStart+i*xStep;
				float yc=ySpan*(xc-xStart)/xSpan+yStart;
				canvas.drawBitmap(bm, xc-r,yc-r, paint);
			}
		}
	}   
	
	@Override
	public int hashCode()
	{
		if(at==ActionType.TS)
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o==null)
		{
			return false;
		}
		
		if(!(o instanceof AtomAction))
		{
			return false;
		}
		AtomAction aa=(AtomAction)o;
		if(this.at==aa.at&&this.xStart==aa.xStart&&this.xEnd==aa.xEnd
						 &&this.yStart==aa.yStart&&this.yEnd==aa.yEnd&&this.r==aa.r)
		{
			return true;
		}
		
		return false;
	}
}
