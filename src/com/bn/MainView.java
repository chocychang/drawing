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
	Paint paint;//�e��
	public MainView(SandPaintingActivity sandPainting) 
	{
		super(sandPainting);
		getHolder().addCallback(this);
		this.sandPainting=sandPainting;
		initPaint();
		drawAllAction(canvasBuff,paint);	//ø��Ҧ��ʧ@	
	}
	public void initPaint(){
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);//�}�ҧܿ���
		paint.setTextSize(18);
	}
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK); 
		for(int i=0;i<MAIN_AN_ARRAY.length;i++)
		{//ø����s�}�C
			canvas.drawBitmap(MAIN_AN_ARRAY[i],PIC_LOCATION_MSG[i][0],PIC_LOCATION_MSG[i][1],paint);		
		}	
		canvas.drawBitmap(BGCOLOR_ARRAY_BIG[BgColorView.areaFlag],PIC_LOCATION_MSG[5][0], PIC_LOCATION_MSG[5][1], paint);
		//ø��ʧ@�M�I��
		canvas.drawBitmap(bmBuff, PIC_LOCATION_MSG[5][0],PIC_LOCATION_MSG[5][1], paint);
	}
	
	//ø��Ҧ��ʧ@����k
	public void drawAllAction(Canvas canvas,Paint paint)
	{
		Bitmap bmBuffTemp=Bitmap.createBitmap
		(//�T�w�bø���ø��
			(int)(AREA_WIDTH),
			(int)(AREA_HEIGHT), 
			Bitmap.Config.ARGB_8888
		);		
		bmBuffTemp.eraseColor(Color.TRANSPARENT);//�Ϋ��w�m���R�I�}�Ϫ�����(�ثe���z��)
		Canvas canvasBuffTemp = new Canvas(bmBuffTemp); 
		synchronized(actionLock)
		{//ø��I���A�C��ø����]�i�H�A�[			
			for(AtomAction aa:alAction)
			{//ø��C�@���۷��@�T�Ϥ�
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
	
	//�W�qø��@�Ӱʧ@
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
	int touchArea=0; //0-��F  1-�M�F  2-����  3-�I��   4-�]�w  5-ø��ϰ�
	
	ActionGroup ag;//�ʧ@�ƧǸs��
	//�ù���ť��k
	public boolean onTouchEvent(MotionEvent event) 
	{
		float x = event.getX();
		float y = event.getY();
		
		//�ù��Q���U�ƥ�
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
			{//���U����F�����s
				touchArea=0;	
			}
			else if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[1][1]					
					&&y<=PIC_LOCATION_MSG[1][1]+MAIN_AN_ARRAY[0].getHeight()
			)
			{//���U���M�F�����s
				touchArea=1;
			}
			else if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[2][1]					
					&&y<=PIC_LOCATION_MSG[2][1]+MAIN_AN_ARRAY[0].getHeight()
			)
			{//���U�����������s
				touchArea=2;
			}
			else if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[3][1]					
					&&y<=PIC_LOCATION_MSG[3][1]+MAIN_AN_ARRAY[0].getHeight()
			){//���U���I���O�����s
				touchArea=3;
				sandPainting.handler.sendEmptyMessage(4);
			}
			else if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[4][1]
					&&y<=PIC_LOCATION_MSG[4][1]+MAIN_AN_ARRAY[0].getHeight()
			){//���U���]�w�����s���D��]�w�����i���ڪ��]�w�M����A�e��SetupView
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
			{//�h���W�L15�ӹ������~��{���O�h���F�]�w�ЧӦ쬰true
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
						{//��F	
							AtomAction aa=new AtomAction(ActionType.TS,actionXPre,actionYPre,x,y,hbSize,ag.id);
							boolean flag=alAction.add(aa);
							if(flag)
							{
								ag.actionG.add(aa);	
								drawSpecAction(canvasBuff,paint,aa);
							}
						} 
						else if(state==1)
						{//�M�F
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
					case 0://0-��F     
						state=0;
					break;
					case 1://1-�M�F
						state=1;
					break;
					case 2://2-����
						synchronized(actionLock)
						{//�[��A�T�O�e�F�x�s���C�@���B��P�B���A�קK�򥢤��[��N�ߨҥ~
							int delIndex=allActiongroup.size()-1;//
							if(delIndex>=0)
							{
								allActiongroup.remove(delIndex);
								alAction.clear();//�M���ثe���e��
								for(ActionGroup tag:allActiongroup)
								{//�N�ѧE���C�@�����s�˶igroup
									for(AtomAction aa:tag.actionG)
									{
										alAction.add(aa);
									}
								}//ø��֨��bcanvas�֨������C�@��
								drawAllAction(canvasBuff,paint);
							}
						}	
					break;
					case 3://3-�I��
						
					break;
					case 4://4-�]�w
						
					break;					
				}
			}
			else
			{
				switch(touchArea)
				{
					case 5://ø��@����i�e����group
						allActiongroup.add(ag);						
					break;
				}
			}
			moveFlag=false;	//ø��@����Nmove�ЧӦ�]��false
			actionXPre=-1;	//ø��@����NactionX��m�]���_�l��m
			actionYPre=-1;  //ø��@����NactionY��m�]���_�l��m
		}		
		return true;
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{//�Ұʰ�������
		mainViewDrawThread = new MainViewDrawThread(this);
		this.mainViewDrawThread.flag=true;
		mainViewDrawThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean flag = true;//�`���ЧӦ�
		mainViewDrawThread.flag=false;//�]�w�`���ЧӦ�
        while (flag) {//�`��
            try {
            	mainViewDrawThread.join();//�o����������
            	flag = false;
            } 
            catch (Exception e) {
            	e.printStackTrace();
            }
        }
	}

}
