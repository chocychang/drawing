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
		this.r=r;//�Q��b�|
		Bitmap bm=Bitmap.createBitmap
		(
				(int)(2*r),
				(int)(2*r), 
				Bitmap.Config.ARGB_8888
		);		//�Q��ҧΦ���bitmap
		Canvas ct = new Canvas(bm); 
		Paint p=new Paint();
		p.setColor(Color.rgb(196, 165, 43));//�]�w�e���m��
		for(int i=0;i<tcl;i++)
		{//�Q��L�{		
			float rt=(float) (r*Math.random());//�Q�⪺�b�|
			double angle=2*Math.PI*Math.random();//�Q�⪺�۹�������m������
			float xt=(float) (rt*Math.sin(angle));//�Q�⪺�F�ɪ�x�y��
			float yt=(float) (rt*Math.cos(angle));//�Q�⪺�F�ɪ�y�y��
            ct.drawPoint(xt+r, yt+r, p);            
		}
		hmhb.put(id, bm);//���x�s�e����hashmap�[�J���
	}
}
