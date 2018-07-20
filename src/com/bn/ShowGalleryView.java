package com.bn;
import static com.bn.Constant.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ShowGalleryView extends View
{
	private Paint paint;
	Bitmap bj;						//�ŧi�I���Ϥ�
	static int index;				//�Ŀ�Ϥ������ޭ�
	float prex;						//Ĳ���I�ex�y�Э�
	float prey;						//Ĳ���I�ey�y�Э�
	float xoffset=0;
	boolean isMove=false;			//�O�_�h���ЧӦ�
	boolean isSelect=false;			//�O�_�Ŀ�ЧӦ�
	Context context;
	
	float currV=0;					//�ثe�t��
	float acce=0;					//�[�t��
	float preXForV;					//���p��[�t�ת�x�b��
	boolean isAutoGo=false;			//�O�_�D�ʨ��ʼЧӦ�
	
	static boolean isLongClick=false;//�����ЧӦ�
	double hf;//���
	
	public ShowGalleryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		paint=new Paint();						//�إߵe��
		paint.setAntiAlias(true);	
	}
	public void onDraw(Canvas canvas)			//���s�w�q��onDraw��k
	{
		canvas.clipRect(new RectF(0,0,SCREEN_WIDTH,SCREEN_HEIGHT));//�i���@�į�
		float xMin=SCREEN_WIDTH-(bitmap_between_width+pic_w)*alr.size();
		if(xMin>0)
		{
			xMin=0;
		}
		float xMax=0;		
		if(xoffset>xMax)						//x�b�������q����j��0�A�]���j��0�ɬɭ����|���ťաA�ťեX�{�b����
		{
			xoffset=xMax;						//�N�����q�m��0
			isAutoGo=false;
		}		
		if(xoffset<xMin)
		{
			xoffset=xMin;						//�N�����q�m���̤p��
			isAutoGo=false;
		}
		canvas.drawBitmap(WELCOME_ARRAY[3],(SCREEN_WIDTH-WELCOME_ARRAY[3].getWidth())/2, 0, paint);//ø��@�~�������D�Ϥ�
		if(alr.size()>0){
			int c=0;			
			for(Record pa:alr){
				Bitmap bitmap=pa.bmResult;	
				Bitmap bg=BGCOLOR_ARRAY_BIG[pa.bgIndex];
				float xTemp=(bitmap_between_width+pic_w)*c;
				float yTemp=(this.getHeight()-pic_h)/2;
				float ratioTemp=pic_w/bitmap.getWidth();
				float xr=pic_w/bg.getWidth();
				float yr=pic_h/bg.getHeight();							
				if(!(isSelect&&index==alr.indexOf(pa)))
				{//ø��I���Ϥ��H�ΨF�e�Ϥ�
					canvas.save();
					canvas.translate(xTemp+xoffset, yTemp);
					canvas.scale(xr, yr);
					canvas.drawBitmap(bg,0,0, paint);					
					canvas.restore();
					
					canvas.save();
					canvas.translate(xTemp+xoffset, yTemp);
					canvas.scale(ratioTemp, ratioTemp);
					canvas.drawBitmap(bitmap,0,0, paint);
					canvas.restore();
					
					//ø�s�@�~�W
					paint.setColor(Color.WHITE);
					paint.setTextSize(20);
					paint.setAntiAlias(true);
					String nameTemp=aln.get(alr.indexOf(pa));
					float nl=paint.measureText(nameTemp);//�o��text���e��
					canvas.drawText(nameTemp, xTemp+xoffset+(pic_w-nl)/2.0f, yTemp+pic_h+20, paint);
					paint.reset();
				}				
				c++;
			}
			//ø��½���Ϥ�
			canvas.drawBitmap(WELCOME_ARRAY[4], 0, SCREEN_HEIGHT-WELCOME_ARRAY[4].getHeight(), paint);
			canvas.drawBitmap(WELCOME_ARRAY[5], SCREEN_WIDTH-WELCOME_ARRAY[5].getWidth(), SCREEN_HEIGHT-WELCOME_ARRAY[5].getHeight(), paint);
		}
		else{//�Y�G�F�e�����űN��ܡ��@�~�����š����Ϥ�
			canvas.drawBitmap(WELCOME_ARRAY[2], 
					(SCREEN_WIDTH-WELCOME_ARRAY[2].getWidth())/2, 
					(SCREEN_HEIGHT-WELCOME_ARRAY[2].getHeight())/2, paint);
		}
	}
	
	//Ĳ�N�ƥ��k
	public boolean onTouchEvent(MotionEvent e)
	{
		float x=e.getX();								//��o�ù��W�I����x�y��
		float y=e.getY();								//��o�ù��W�I����y�y��
		switch(e.getAction())
		{
			case MotionEvent.ACTION_DOWN:					//���U�ƥ�
				isAutoGo=false;
				
				isLongClick=false;
				prex=x;										//�N���y�ЭȽᤩprex
				prey=y;										//�N���y�ЭȽᤩprey
				preXForV=x;
				index=(int)((x-xoffset)/(pic_w+bitmap_between_width));//���o���U��m�ҦbpicAtom�Ϥ�������
				if(alr.size()==1&&x>=0&&x<=(pic_w)&&y>=(this.getHeight()-pic_h)/2&&y<=(this.getHeight()+pic_h)/2){
					index=0;//��u���@�T�Ϥ����I��Ϥ����a��ɬ��קK���~�ҥH�mindex��0�A����u���@�T�Ϥ�
				}
				if(alr.size()==1&&x>=(pic_w)||alr.size()<0){
					index=-1;//��u���@�T�Ϥ����I��ù��W�S���Ϥ����a��ɬ��קK���~�ҥH�mindex���t
				}
				
				if(y>((this.getHeight()-pic_h)/2)&&y<((this.getHeight()-pic_h)/2+pic_h))
				{//���F�קK�I��S���Ϥ����a��ҥH�]�w����Ϭ��Ϥ����ת��ϰ�
					isSelect=true;
				}
				if(x>=0&&x<=WELCOME_ARRAY[4].getWidth()
					&&y>=SCREEN_HEIGHT-WELCOME_ARRAY[4].getHeight()
					&&y<=SCREEN_HEIGHT)
				{//�I���W�@���Ϥ�
					((SandPaintingActivity)context).handler.sendEmptyMessage(10);//�V�T���B�z���ǰe�T��"�W�@��"					
				}
				else if(x<=SCREEN_WIDTH&&x>=SCREEN_WIDTH-WELCOME_ARRAY[5].getWidth()
						&&y>=SCREEN_HEIGHT-WELCOME_ARRAY[5].getHeight()
						&&y<=SCREEN_HEIGHT)
				{//�I���U�@���Ϥ�
					((SandPaintingActivity)context).handler.sendEmptyMessage(11);//�V�T���B�z���ǰe�T��"�U�@��"										
				}
				if(y>=(SCREEN_HEIGHT-pic_h)/2&&y<=(SCREEN_HEIGHT+pic_h)/2){
					//�Ұʪ�����ť
					new Thread()
					{
						public void run()
						{
							double tempHf=Math.random();
							hf=tempHf;
							try 
							{
								Thread.sleep(1500);
							} catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
							if(hf==tempHf&&(!isMove))
							{
								isLongClick=true;
								String nameTemp=aln.get(index);
								Bundle b=new Bundle();
								b.putString("name",nameTemp);
								Message msg=new Message();
								msg.what=9;
								msg.setData(b);
								((SandPaintingActivity)context).handler.sendMessage(msg);
							}
						}
					}.start();
				}
				
				
				return true;
			case MotionEvent.ACTION_MOVE:			//�h���ƥ�
				if(!isMove&&Math.abs(x-prex)>20)	//���A����ʵe�ɡA�B�ٰʪ��Z���j��]�w��10dip��
				{
					isMove=true;					//����ʵe�ЧӦ�]��true
				}
				if(isMove)							//����ʵe��
				{
					isSelect=false;					//�ܤƼЧӦ�]�w��false�A���ɤ���e�Ŀ�Ϥ�
					xoffset=(int)(xoffset+x-prex);	//�p��x�b�����q
					prex=x;							//�N���ɪ��y�ЭȽᤩprex
					prey=y;
					
					float dx=x-preXForV;
					currV=dx/2.0f;
					preXForV=x;
					
					this.postInvalidate();	//��ø
				}
				return true;
			case MotionEvent.ACTION_UP:				//��_��
				isMove=false;						//�ʵe�ЧӦ�]��false
				hf=Math.random();
				acce=10;
				if(currV>0)
				{
					acce=-10;
				}
				isAutoGo=true;
				
				new Thread()
				{
					public void run()
					{
						while(isAutoGo&&!isLongClick)
						{//�T�O�b�������ɭԨS���[�t���קK������Ϥ��Q��S�F
							if(currV>=0&&acce>0||currV<=0&&acce<0)
							{
								break;
							}
							xoffset=xoffset+currV;
							currV=currV+acce;							
							postInvalidate();
							
							try 
							{
								Thread.sleep(40);
							} catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
						}
					}
				}.start();				
				
				if(isSelect&&!isLongClick&&alr.size()>0&&index>=0)
				{
					hf=Math.random();
					isLongClick=false;
					Record pa=alr.get(index);
					Constant.alAction=pa.alAction;//�N�ثe��ܪ��̤p�ʧ@�m��������F�e�����ʧ@�T��
					Constant.allActiongroup=pa.allActiongroup;//�N�ثe��ܪ��ʧ@�s�ոm��������F�e�����ʧ@�s�հT��
					Constant.hmhb=pa.hmhb;//�Ϥ��֨��x�s��m�m��������Ϥ����T��
					Constant.bmBuff=pa.bmResult;//Bitmap�֨��Ψ��x�s�w�g�e�F���C�@���m��������Ϥ��������T��
					BgColorView.areaFlag=pa.bgIndex;//�ثe�Ϥ��I���]�w�����󤤪��T��			//�]�w�T������what��
					((SandPaintingActivity)context).handler.sendEmptyMessage(8);//�V�T���B�z���ǰe�T��				
				}	
			return true;
		}
		return false;
	}
}
