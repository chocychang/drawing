package com.bn;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class WelcomeViewDrawThread extends Thread {
	boolean flag = true;
	int sleepSpan = 100;
	WelcomeView myWelcomeView;
	SurfaceHolder surfaceHolder;
	public  void setFlag(boolean flag){
		this.flag=flag;
	}
	public WelcomeViewDrawThread(WelcomeView myWelcomeView){
		this.myWelcomeView = myWelcomeView;
		this.surfaceHolder = myWelcomeView.getHolder();
	}
	public void run(){
		Canvas c;
		for(int i=255;i>-10;i=i-20)
		{//動態變更圖片的透明度值並不斷重繪	
			WelcomeView.currentAlpha=i;
			if(WelcomeView.currentAlpha<0)
			{
				WelcomeView.currentAlpha=0;
			}
            c = null;
            try {
            	// 鎖定整個畫布
                c = this.surfaceHolder.lockCanvas(null);
                synchronized (this.surfaceHolder) {
                	myWelcomeView.onDraw(c);//繪制
                }
            } finally {
                if (c != null) {
                	//釋放鎖
                    this.surfaceHolder.unlockCanvasAndPost(c);
                }
            }
            try{
            	Thread.sleep(10);//睡眠指定毫秒數
            }
            catch(Exception e){
            	e.printStackTrace();
            }
		}
		WelcomeView.sandPainting.handler.sendEmptyMessage(1);
	}
}
