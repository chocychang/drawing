package com.bn;
import static com.bn.Constant.*;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainView extends SurfaceView implements SurfaceHolder.Callback{
	SandPaintingActivity sandPainting;
	MainViewDrawThread mainViewDrawThread;
	Paint paint;//畫筆
	public MainView(SandPaintingActivity sandPainting) 
	{
		super(sandPainting);
		getHolder().addCallback(this);
		this.sandPainting=sandPainting;
		initPaint();
		drawAllAction(canvasBuff,paint);	//繪制所有動作	
	}
	public void initPaint(){
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);//開啟抗鋸齒
		paint.setTextSize(18);
	}
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK); 
		for(int i=0;i<MAIN_AN_ARRAY.length;i++)
		{//繪制按鈕陣列
			canvas.drawBitmap(MAIN_AN_ARRAY[i],PIC_LOCATION_MSG[i][0],PIC_LOCATION_MSG[i][1],paint);		
		}	
		canvas.drawBitmap(BGCOLOR_ARRAY_BIG[BgColorView.areaFlag],PIC_LOCATION_MSG[5][0], PIC_LOCATION_MSG[5][1], paint);
		//繪制動作和背景
		canvas.drawBitmap(bmBuff, PIC_LOCATION_MSG[5][0],PIC_LOCATION_MSG[5][1], paint);
	}
	
	//繪制所有動作的方法
	public void drawAllAction(Canvas canvas,Paint paint)
	{
		Bitmap bmBuffTemp=Bitmap.createBitmap
		(//確定在繪制區繪制
			(int)(AREA_WIDTH),
			(int)(AREA_HEIGHT), 
			Bitmap.Config.ARGB_8888
		);		
		bmBuffTemp.eraseColor(Color.TRANSPARENT);//用指定彩色填充點陣圖的像素(目前為透明)
		Canvas canvasBuffTemp = new Canvas(bmBuffTemp); 
		synchronized(actionLock)
		{//繪制背景，每次繪制完成也可以再加			
			for(AtomAction aa:alAction)
			{//繪制每一筆相當於一幅圖片
				aa.drawSelf(canvasBuffTemp,paint);
			}
		}	
		if(bmBuff!=null)
		{
			bmBuff.recycle();
		}
		
		bmBuff=bmBuffTemp;
		canvasBuff=canvasBuffTemp;
	}
	
	//增量繪制一個動作
	public void drawSpecAction(Canvas canvas,Paint paint,AtomAction aa)
	{
		synchronized(actionLock)
		{			
			aa.drawSelf(canvas,paint);
		}
	}	
	
	float xPre=-1;
	float yPre=-1;
	float actionXPre=-1;
	float actionYPre=-1;
	boolean moveFlag=false;
	int touchArea=0; //0-填沙  1-清沙  2-取消  3-背景   4-設定  5-繪制區域
	
	ActionGroup ag;//動作排序群組
	//螢幕監聽方法
	public boolean onTouchEvent(MotionEvent event) 
	{
		float x = event.getX();
		float y = event.getY();
		
		//螢幕被按下事件
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			xPre=x;
			yPre=y;
			moveFlag=false;
			if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[0][1]					
					&&y<=PIC_LOCATION_MSG[0][1]+MAIN_AN_ARRAY[0].getHeight()
			)
			{//按下“填沙”按鈕
				touchArea=0;	
			}
			else if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[1][1]					
					&&y<=PIC_LOCATION_MSG[1][1]+MAIN_AN_ARRAY[0].getHeight()
			)
			{//按下“清沙”按鈕
				touchArea=1;
			}
			else if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[2][1]					
					&&y<=PIC_LOCATION_MSG[2][1]+MAIN_AN_ARRAY[0].getHeight()
			)
			{//按下“取消”按鈕
				touchArea=2;
			}
			else if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[3][1]					
					&&y<=PIC_LOCATION_MSG[3][1]+MAIN_AN_ARRAY[0].getHeight()
			){//按下“背景燈”按鈕
				touchArea=3;
				sandPainting.handler.sendEmptyMessage(4);
			}
			else if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[4][1]
					&&y<=PIC_LOCATION_MSG[4][1]+MAIN_AN_ARRAY[0].getHeight()
			){//按下“設定”按鈕跳躍到設定頁面進行實際的設定和選取，前往SetupView
				touchArea=4;
				sandPainting.handler.sendEmptyMessage(2);
			}
			else
			{
				touchArea=5;
				ag=new ActionGroup(hbSize);
			}
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE)
		{
			if(x-xPre>15||y-yPre>15)
			{//搬移超過15個像素單位才能認為是搬移了設定標志位為true
				moveFlag=true;
			}
			
			if(touchArea==5&&x<=PIC_LOCATION_MSG[5][2]&&x>=PIC_LOCATION_MSG[5][0]
			              &&y<=PIC_LOCATION_MSG[5][3]&&y>=PIC_LOCATION_MSG[5][1])
			{
				synchronized(actionLock)
				{
					if(actionXPre!=-1&&actionYPre!=-1)
					{
						if(state==0)
						{//填沙	
							AtomAction aa=new AtomAction(ActionType.TS,actionXPre,actionYPre,x,y,hbSize,ag.id);
							boolean flag=alAction.add(aa);
							if(flag)
							{
								ag.actionG.add(aa);	
								drawSpecAction(canvasBuff,paint,aa);
							}
						} 
						else if(state==1)
						{//清沙
							AtomAction aa=new AtomAction(ActionType.QS,actionXPre,actionYPre,x,y,hbSize,ag.id);
							boolean flag=alAction.add(aa);	
							if(flag)
							{
								ag.actionG.add(aa);	
								drawSpecAction(canvasBuff,paint,aa);
							}
						}
					} 
				}	
				actionXPre=x;
				actionYPre=y;
			}
		}
		else if(event.getAction() == MotionEvent.ACTION_UP)
		{
			if(!moveFlag)
			{
				switch(touchArea)
				{
					case 0://0-填沙     
						state=0;
					break;
					case 1://1-清沙
						state=1;
					break;
					case 2://2-取消
						synchronized(actionLock)
						{//加鎖，確保畫沙儲存的每一筆處於同步狀態避免遺失不加鎖將拋例外
							int delIndex=allActiongroup.size()-1;//
							if(delIndex>=0)
							{
								allActiongroup.remove(delIndex);
								alAction.clear();//清除目前的畫筆
								for(ActionGroup tag:allActiongroup)
								{//將剩余的每一筆重新裝進group
									for(AtomAction aa:tag.actionG)
									{
										alAction.add(aa);
									}
								}//繪制快取在canvas快取中的每一筆
								drawAllAction(canvasBuff,paint);
							}
						}	
					break;
					case 3://3-背景
						
					break;
					case 4://4-設定
						
					break;					
				}
			}
			else
			{
				switch(touchArea)
				{
					case 5://繪制完一筆填進畫筆的group
						allActiongroup.add(ag);						
					break;
				}
			}
			moveFlag=false;	//繪制完一筆後將move標志位設為false
			actionXPre=-1;	//繪制完一筆後將actionX位置設為起始位置
			actionYPre=-1;  //繪制完一筆後將actionY位置設為起始位置
		}		
		return true;
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{//啟動執行緒刷框
		mainViewDrawThread = new MainViewDrawThread(this);
		this.mainViewDrawThread.flag=true;
		mainViewDrawThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean flag = true;//循環標志位
		mainViewDrawThread.flag=false;//設定循環標志位
        while (flag) {//循環
            try {
            	mainViewDrawThread.join();//得到執行緒結束
            	flag = false;
            } 
            catch (Exception e) {
            	e.printStackTrace();
            }
        }
	}

}
