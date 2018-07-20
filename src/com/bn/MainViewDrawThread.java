package com.bn;


import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainViewDrawThread extends Thread{
	MainView myMainView;
	private int sleepSpan = 40;
	boolean flag = true;//�`���Хܦ�
	private SurfaceHolder surfaceHolder;//surfaceHolder���Ѧ�
	public MainViewDrawThread(MainView myMainView)
	{
		this.myMainView = myMainView;
		surfaceHolder = myMainView.getHolder();
	}
	public void run() {//���s�w�q��run��k
		Canvas c;//�ŧi�e��
        while (this.flag) {//�`��
            c = null;
            try {//��w��ӵe��
                c = this.surfaceHolder.lockCanvas(null);
                synchronized (this.surfaceHolder) {//�P�B
                	myMainView.onDraw(c);//�I�sø���k
                }
            } finally {//��finally�T�O�@�w�Q����
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
	public void setFlag(boolean flag){//�`���Хܦ쪺set��k
		this.flag = flag;
	}
}
