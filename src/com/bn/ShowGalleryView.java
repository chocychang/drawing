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
	Bitmap bj;						//宣告背景圖片
	static int index;				//勾選圖片的索引值
	float prex;						//觸控點前x座標值
	float prey;						//觸控點前y座標值
	float xoffset=0;
	boolean isMove=false;			//是否搬移標志位
	boolean isSelect=false;			//是否勾選標志位
	Context context;
	
	float currV=0;					//目前速度
	float acce=0;					//加速度
	float preXForV;					//為計算加速度的x軸值
	boolean isAutoGo=false;			//是否慣性卷動標志位
	
	static boolean isLongClick=false;//長按標志位
	double hf;//虎符
	
	public ShowGalleryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		paint=new Paint();						//建立畫筆
		paint.setAntiAlias(true);	
	}
	public void onDraw(Canvas canvas)			//重新定義的onDraw方法
	{
		canvas.clipRect(new RectF(0,0,SCREEN_WIDTH,SCREEN_HEIGHT));//可提昇效能
		float xMin=SCREEN_WIDTH-(bitmap_between_width+pic_w)*alr.size();
		if(xMin>0)
		{
			xMin=0;
		}
		float xMax=0;		
		if(xoffset>xMax)						//x軸的偏移量不能大於0，因為大於0時界面中會有空白，空白出現在左側
		{
			xoffset=xMax;						//將偏移量置為0
			isAutoGo=false;
		}		
		if(xoffset<xMin)
		{
			xoffset=xMin;						//將偏移量置為最小值
			isAutoGo=false;
		}
		canvas.drawBitmap(WELCOME_ARRAY[3],(SCREEN_WIDTH-WELCOME_ARRAY[3].getWidth())/2, 0, paint);//繪制“作品集”標題圖片
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
				{//繪制背景圖片以及沙畫圖片
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
					
					//繪製作品名
					paint.setColor(Color.WHITE);
					paint.setTextSize(20);
					paint.setAntiAlias(true);
					String nameTemp=aln.get(alr.indexOf(pa));
					float nl=paint.measureText(nameTemp);//得到text的寬度
					canvas.drawText(nameTemp, xTemp+xoffset+(pic_w-nl)/2.0f, yTemp+pic_h+20, paint);
					paint.reset();
				}				
				c++;
			}
			//繪制翻頁圖片
			canvas.drawBitmap(WELCOME_ARRAY[4], 0, SCREEN_HEIGHT-WELCOME_ARRAY[4].getHeight(), paint);
			canvas.drawBitmap(WELCOME_ARRAY[5], SCREEN_WIDTH-WELCOME_ARRAY[5].getWidth(), SCREEN_HEIGHT-WELCOME_ARRAY[5].getHeight(), paint);
		}
		else{//若果沙畫集為空將顯示“作品集為空”的圖片
			canvas.drawBitmap(WELCOME_ARRAY[2], 
					(SCREEN_WIDTH-WELCOME_ARRAY[2].getWidth())/2, 
					(SCREEN_HEIGHT-WELCOME_ARRAY[2].getHeight())/2, paint);
		}
	}
	
	//觸摸事件方法
	public boolean onTouchEvent(MotionEvent e)
	{
		float x=e.getX();								//獲得螢幕上點擊的x座標
		float y=e.getY();								//獲得螢幕上點擊的y座標
		switch(e.getAction())
		{
			case MotionEvent.ACTION_DOWN:					//按下事件
				isAutoGo=false;
				
				isLongClick=false;
				prex=x;										//將此座標值賦予prex
				prey=y;										//將此座標值賦予prey
				preXForV=x;
				index=(int)((x-xoffset)/(pic_w+bitmap_between_width));//取得按下位置所在picAtom圖片的索引
				if(alr.size()==1&&x>=0&&x<=(pic_w)&&y>=(this.getHeight()-pic_h)/2&&y<=(this.getHeight()+pic_h)/2){
					index=0;//當只有一幅圖片時點選圖片的地方時為避免錯誤所以置index為0，表明只有一幅圖片
				}
				if(alr.size()==1&&x>=(pic_w)||alr.size()<0){
					index=-1;//當只有一幅圖片時點選螢幕上沒有圖片的地方時為避免錯誤所以置index為負
				}
				
				if(y>((this.getHeight()-pic_h)/2)&&y<((this.getHeight()-pic_h)/2+pic_h))
				{//為了避免點到沒有圖片的地方所以設定選取區為圖片高度的區域
					isSelect=true;
				}
				if(x>=0&&x<=WELCOME_ARRAY[4].getWidth()
					&&y>=SCREEN_HEIGHT-WELCOME_ARRAY[4].getHeight()
					&&y<=SCREEN_HEIGHT)
				{//點擊上一頁圖片
					((SandPaintingActivity)context).handler.sendEmptyMessage(10);//向訊息處理器傳送訊息"上一頁"					
				}
				else if(x<=SCREEN_WIDTH&&x>=SCREEN_WIDTH-WELCOME_ARRAY[5].getWidth()
						&&y>=SCREEN_HEIGHT-WELCOME_ARRAY[5].getHeight()
						&&y<=SCREEN_HEIGHT)
				{//點擊下一頁圖片
					((SandPaintingActivity)context).handler.sendEmptyMessage(11);//向訊息處理器傳送訊息"下一頁"										
				}
				if(y>=(SCREEN_HEIGHT-pic_h)/2&&y<=(SCREEN_HEIGHT+pic_h)/2){
					//啟動長按監聽
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
			case MotionEvent.ACTION_MOVE:			//搬移事件
				if(!isMove&&Math.abs(x-prex)>20)	//當不再播放動畫時，且抹動的距離大於設定值10dip時
				{
					isMove=true;					//播放動畫標志位設為true
				}
				if(isMove)							//播放動畫時
				{
					isSelect=false;					//變化標志位設定為false，此時不能畫勾選圖片
					xoffset=(int)(xoffset+x-prex);	//計算x軸偏移量
					prex=x;							//將此時的座標值賦予prex
					prey=y;
					
					float dx=x-preXForV;
					currV=dx/2.0f;
					preXForV=x;
					
					this.postInvalidate();	//重繪
				}
				return true;
			case MotionEvent.ACTION_UP:				//抬起時
				isMove=false;						//動畫標志位設成false
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
						{//確保在長按的時候沒有加速度避免選取的圖片被刷沒了
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
					Constant.alAction=pa.alAction;//將目前顯示的最小動作置為選取的沙畫中的動作訊息
					Constant.allActiongroup=pa.allActiongroup;//將目前顯示的動作群組置為選取的沙畫中的動作群組訊息
					Constant.hmhb=pa.hmhb;//圖片快取儲存位置置為選取的圖片的訊息
					Constant.bmBuff=pa.bmResult;//Bitmap快取用來儲存已經畫了的每一筆置為選取的圖片的對應訊息
					BgColorView.areaFlag=pa.bgIndex;//目前圖片背景設定為物件中的訊息			//設定訊息物件的what值
					((SandPaintingActivity)context).handler.sendEmptyMessage(8);//向訊息處理器傳送訊息				
				}	
			return true;
		}
		return false;
	}
}
