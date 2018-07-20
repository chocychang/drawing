package com.bn;

import static com.bn.Constant.*;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ActionGroup implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
	long id=System.nanoTime();
	ArrayList<AtomAction> actionG=new ArrayList<AtomAction>();
	float r;
	
	public ActionGroup(float r)
	{
		this.r=r;//噴砂半徑
		Bitmap bm=Bitmap.createBitmap
		(
				(int)(2*r),
				(int)(2*r), 
				Bitmap.Config.ARGB_8888
		);		//噴砂所形成的bitmap
		Canvas ct = new Canvas(bm); 
		Paint p=new Paint();
		p.setColor(Color.rgb(196, 165, 43));//設定畫筆彩色
		for(int i=0;i<tcl;i++)
		{//噴砂過程		
			float rt=(float) (r*Math.random());//噴砂的半徑
			double angle=2*Math.PI*Math.random();//噴砂的相對於水平位置的角度
			float xt=(float) (rt*Math.sin(angle));//噴砂的沙粒的x座標
			float yt=(float) (rt*Math.cos(angle));//噴砂的沙粒的y座標
            ct.drawPoint(xt+r, yt+r, p);            
		}
		hmhb.put(id, bm);//往儲存畫筆的hashmap加入資料
	}
}
