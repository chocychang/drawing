package com.bn;


import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainViewDrawThread extends Thread{
	MainView myMainView;
	private int sleepSpan = 40;
	boolean flag = true;//循環標示位
	private SurfaceHolder surfaceHolder;//surfaceHolder的參考
	public MainViewDrawThread(MainView myMainView)
	{
		this.myMainView = myMainView;
		surfaceHolder = myMainView.getHolder();
	}
	public void run() {//重新定義的run方法
		Canvas c;//宣告畫布
        while (this.flag) {//循環
            c = null;
            try {//鎖定整個畫布
                c = this.surfaceHolder.lockCanvas(null);
                synchronized (this.surfaceHolder) {//同步
                	myMainView.onDraw(c);//呼叫繪制方法
                }
            } finally {//用finally確保一定被執行
                if (c != null) {                	
                    this.surfaceHolder.unlockCanvasAndPost(c);
                }
            }
            try{
            	Thread.sleep(sleepSpan);
            }catch(Exception e){
            	e.printStackTrace();
            }
        }
	}
	public void setFlag(boolean flag){//循環標示位的set方法
		this.flag = flag;
	}
}
