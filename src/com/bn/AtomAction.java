package com.bn;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import static com.bn.Constant.*;

enum ActionType{TS,QS};//��F|�M�F

public class AtomAction implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
	//�ʧ@���A
	ActionType at;
	//�ʧ@�_�lxy��m
	float xStart;
	float yStart;
	float xEnd;
	float yEnd;
	//�ʧ@�b�|
	float r;
	//�̤p���|����
	double length;
	//�e��ID
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
		length=Math.sqrt(xSpan*xSpan+ySpan*ySpan);		//�Y���Q�⪺����
	}
	
	public void drawSelf(Canvas canvas,Paint paint)
	{		
		paint.reset();//���s�]�w�e��
		if(at==ActionType.QS)
		{//�ʧ@���M�F
			paint.setColor(0xFF000000);//�]�w�e���m�� 	(�b�ѨM�g���I���o�Ӱ��D���_�@��)
			paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
			paint.setStyle(Style.STROKE);//�]�wpaint�����欰���Ťߡ�
			paint.setStrokeWidth(2*r);	 //�]�w��e��		
			canvas.drawLine(xStart, yStart, xEnd, yEnd, paint);	//ø��
			paint.reset();
		}
		else if(at==ActionType.TS)
		{
			paint.setStyle(Style.FILL);//�]�w�e����R�Ҧ������
			paint.setColor(Color.rgb(SANDCOLOR[0],SANDCOLOR[1],SANDCOLOR[2]));//�]�w�F�l�m��				 
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