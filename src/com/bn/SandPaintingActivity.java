package com.bn;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import static com.bn.Constant.*;

enum WhichView {WELCOME_VIEW,MAIN_VIEW,SETUP_VIEW,BGCOLOR_VIEW,PARAMSSET_VIEW,GALLERY_VIEW}//所有VIEW的清單
public class SandPaintingActivity extends Activity 
{

	AttributeSet attrs;
	private ShowGalleryView sgView;
	private WelcomeView myWelcomeView;	//宣告歡迎頁參考
	private MainView myMainView;		//宣告繪制頁參考
	private SetupView mySetupView;		//宣告主設定頁參考
	private BgColorView myBgColorView;	//宣告背景設定頁參考
	WhichView curr;						//目前所在view
	Dialog nameInputdialog; 
	String name;						//宣告所選取的沙畫名參考
	//宣告Dialog參考
	Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			if(msg.what==1)
			{			//從歡迎頁跳躍到主頁面
				if(myWelcomeView!=null)
				{
					myWelcomeView=null;
				}
				gotoMainView(); 
			}
			else if(msg.what==2)
			{		//從main頁面跳躍到主設定頁面
				if(myMainView!=null)
				{
					myMainView=null;
				}
				gotoSetupView(); 
			}
			else if(msg.what==3)
			{		//從背景選取頁面跳躍到主頁面
				if(myMainView!=null)
				{
					myMainView=null;
				}
				gotoMainView(); 
			}
			else if(msg.what==4)
			{		//透過main頁面的“背景”按鈕跳躍到背景選取頁面
				if(myBgColorView!=null)
				{
					myBgColorView=null;
				}
				gotoBgColorView(); 
			}
			else if(msg.what==5)
			{		//透過main頁面的“畫筆設定”按鈕進行畫筆和噴砂半徑設定頁面的跳躍
				gotoParamsSetView(); 
			}
			else if(msg.what==6)
			{
				showSaveDiaolg();		//顯示儲存交談視窗
				
			}
			else if(msg.what==7)
			{
				galleryPageNo=0;
				galleryPageCount=history.size()/galleryPageSpan;
				if(history.size()%galleryPageSpan!=0)
				{
					galleryPageCount=galleryPageCount+1;
				}
				gotoGalleryView();			//顯示畫廊
			}
			else if(msg.what==8)
			{	//從作品集頁跳躍到主頁面
				if(myMainView!=null)
				{
					myMainView=null;
				}
				gotoMainView();//跳躍到顯示頁面
			}
			else if(msg.what==9)//移除指定作品
			{
				Bundle b=msg.getData();
				name=b.getString("name");
				showDialog(DELETE_DIALOG_ID);
			}		
			else if(msg.what==10){//點擊作品集上一頁跳躍
				prePage();				
			}
			else if(msg.what==11){//點擊作品集上一頁跳躍
				nextPage();
			}
		}
	};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);          
        //下兩句為設定全螢幕
        requestWindowFeature(Window.FEATURE_NO_TITLE);         
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        if(dm.widthPixels>dm.heightPixels)
        {//得到實際螢幕width和height並進行切屏轉換
        	 SCREEN_WIDTH=dm.widthPixels;
             SCREEN_HEIGHT=dm.heightPixels;
        }
        else
        {
        	SCREEN_WIDTH=dm.heightPixels;
            SCREEN_HEIGHT=dm.widthPixels;
        }
		Constant.getRatio();			//得到縮放比		
		Constant.initPicture(this.getResources());//處理圖片以適應螢幕
		Constant.getPicLocationMsg();	//起始化圖片訊息內含x,y,(w,h)

        
		gotoWelcomeView();				//跳躍到歡迎頁
		myWelcomeView.requestFocus();	//取得焦點
		myWelcomeView.setFocusableInTouchMode(true);//設定為可觸控
    }
    
    public void gotoWelcomeView()
    {		//跳躍到歡迎界面
    	if(null==myWelcomeView)
    	{
        	myWelcomeView= new WelcomeView(this);
    	}
        setContentView(myWelcomeView);
    	curr=WhichView.WELCOME_VIEW;
    }
    public void gotoMainView()
    {			//跳躍到Main界面
    	if(myMainView==null)
    	{
    		myMainView = new MainView(SandPaintingActivity.this);
    	}
		SandPaintingActivity.this.setContentView(myMainView);
		curr=WhichView.MAIN_VIEW;
    }
    public void gotoSetupView()
    {		//跳躍到設定界面
    	if(null==mySetupView)
    	{
        	mySetupView = new SetupView(this);
    	}
		SandPaintingActivity.this.setContentView(mySetupView);
		curr=WhichView.SETUP_VIEW;
    }
    public void gotoBgColorView()
    {		//跳躍到背景界面
    	if(null==myBgColorView)
    	{
        	myBgColorView = new BgColorView(this);
    	}
		SandPaintingActivity.this.setContentView(myBgColorView);
		curr=WhichView.BGCOLOR_VIEW;	//設定目前View為參數設定View
    }
    public void showSaveDiaolg()
    {		//顯示儲存交談視窗
    	showDialog(NAME_INPUT_DIALOG_ID);
    }	 
    
    public void nextPage()
    {//跳躍到下一頁，循環跳躍，當在最後一頁跳躍時自動跳躍到第一頁
    	galleryPageNo=(galleryPageNo+1)%galleryPageCount;
    	flushHistory();
    	sgView.postInvalidate();
    }
    
    public void prePage()
    {//跳躍到上一頁，循環跳躍，當從第一頁跳躍時，將自動跳躍到最後一頁
    	galleryPageNo=(galleryPageNo-1+galleryPageCount)%galleryPageCount;
    	flushHistory();
    	sgView.postInvalidate();
    }    
    
    public void flushHistory()
    {//更新歷史記錄
    	if(alr!=null)
		{//這裡是回收bitmap的快取，這樣可以改善系統
			for(Record r:alr)
			{//將record儲存的已經畫了的每一筆回收
				r.bmResult.recycle();
			}
		}    	
    	int start=galleryPageNo*galleryPageSpan;//計算每一頁的第一幅圖片的索引
    	int count=0;
		
		ArrayList<Record> alr=new ArrayList<Record>();//宣告沙畫圖片訊息Record的ArrayList
		ArrayList<String> aln=new ArrayList<String>();//宣告沙畫圖片名的ArrayList
		Set<String> ks=history.keySet();
		for(String key:ks)
		{
			if(count>=start&&count<start+galleryPageSpan)
			{//根據頁數取出對應的沒夜的圖，每次只快取一頁的圖片，這樣就不會造成記憶體溢位
				byte[] data=history.get(key);
				Record rTemp=Record.fromBytesToRecord(data);
				if(rTemp.bmResult==null)
				{
					throw new RuntimeException("hua kong");
				}
				aln.add(key);//將該頁的資料存進名稱列
				alr.add(rTemp);//將圖片資料存進圖片列
			}
			count++;
		}			
		Constant.alr=alr;//將其給予值給靜態資料
		Constant.aln=aln;
    }
	
	public void gotoGalleryView()
	{
		flushHistory();
		SandPaintingActivity.this.setContentView(R.layout.showgallerymain);
        sgView=(ShowGalleryView)findViewById(R.id.sgView);//取得顯示圖片的View的物件並在其中繪制圖片     
		curr=WhichView.GALLERY_VIEW;
	}
	
    @Override
    public Dialog onCreateDialog(int id)//建立交談視窗
    {    	
        Dialog result=null;
    	switch(id)
    	{
	    	case NAME_INPUT_DIALOG_ID://姓名輸入交談視窗
		    	nameInputdialog=new MyDialog(this); 	    
				result=nameInputdialog;				
			break;	
	    	case DELETE_DIALOG_ID://長按圖片是否移除它，若果選取確定將移除對應沙畫
	    		Builder b=new AlertDialog.Builder(this);  
	    		  b.setMessage("是否移除該圖片？");//設定訊息
	    		  b.setPositiveButton//為交談視窗設定按鈕
	    		  (
	    				"確定", 
	    				new DialogInterface.OnClickListener()
		        		{
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Toast.makeText(SandPaintingActivity.this, "移除勾選的專案"+name, Toast.LENGTH_SHORT).show();					
								history.remove(name);//移除這一名字的沙畫
								gotoGalleryView();
							}      			
		        		}
	    		  );
	    		  b.setNegativeButton
	    		  (
	    				"取消",
	    				new DialogInterface.OnClickListener()
	    				{
	    					public void onClick(DialogInterface dialog, int which){}
						}
	    		  );
	    		  result=b.create();
	    	break;
    	}   
		return result;
    }
    
    //每次出現交談視窗時被回調以動態更新交談視窗內容的方法
    @Override
    public void onPrepareDialog(int id, final Dialog dialog)
    {
    	//若不是等待交談視窗則傳回
    	switch(id)
    	{
    	   case NAME_INPUT_DIALOG_ID://姓名輸入交談視窗
    		   //確定按鈕
    		   Button bjhmcok=(Button)nameInputdialog.findViewById(R.id.saveOk);
    		   //取消按鈕
       		   Button bjhmccancel=(Button)nameInputdialog.findViewById(R.id.saveCancle);
       		   //給確定按鈕加入監聽器
       		   bjhmcok.setOnClickListener
               (
    	          new OnClickListener()
    	          {
    				@Override
    				public void onClick(View v) 
    				{
    					//取得交談視窗裡的內容並用Toast顯示
    					EditText et=(EditText)nameInputdialog.findViewById(R.id.etname);
    					
    					String name=et.getText().toString();    					
    					Record r=new Record();
    					history.put(name, r.toBytes()); 
    					Toast.makeText
    					(
    						SandPaintingActivity.this,
    						"成功儲存進緩沖！", 
    						Toast.LENGTH_SHORT
    					).show(); 
    					nameInputdialog.cancel();
    					
    					//每次還原自動讀取
    					try
    			    	{
    			    		File f=new File("/sdcard/sp.data");
    			    		FileInputStream fin=new FileInputStream(f);    			    		
    			    		fin.close();
    			    	}
    			    	catch(Exception e)
    			    	{
    			    		Toast.makeText
    			    		(
    			    			SandPaintingActivity.this, 
    			    			"不能正確讀寫SD卡，程式離開後\n資料有可能會遺失！", 
    			    			Toast.LENGTH_SHORT
    			    		).show();
    			    	}
    				}        	  
    	          }
    	        );   
       		    //給取消按鈕加入監聽器
       		    bjhmccancel.setOnClickListener
	            (
	 	          new OnClickListener()
	 	          {
	 				@Override
	 				public void onClick(View v) 
	 				{
	 					//關閉交談視窗
	 					nameInputdialog.cancel();					
	 				}        	  
	 	          }
	 	        );   
    	   break;	
    	}
    }    
    public void gotoParamsSetView()
    {	//跳躍到參數設定頁面，使用xml群組成
		this.setContentView(R.layout.paramset);
		SeekBar sbHB=(SeekBar)this.findViewById(R.id.SeekBar01);//得到畫筆半徑SeekBar參考			
        int currValue=(int) ((hbSize-HBMIN)/(HBMAX-HBMIN)*100);
		sbHB.setProgress(currValue);
		sbHB.setOnSeekBarChangeListener(//為畫筆半徑SeekBar設定監聽
            new SeekBar.OnSeekBarChangeListener()
            {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) 
				{//將畫筆半徑設定為拖拉後的值	
					float f=progress;
					hbSize=(float) (HBMIN+(HBMAX-HBMIN)*f/100.0f);
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {	}
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) { }            	
            }
        );	        
        SeekBar sbTCL=(SeekBar)this.findViewById(R.id.SeekBar02);//得到填充率SeekBar參考	
        currValue=(int) ((tcl-TCLMIN)/(TCLMAX-TCLMIN)*100);
		sbTCL.setProgress(currValue);
        sbTCL.setOnSeekBarChangeListener(//為填充率SeekBar設定監聽
            new SeekBar.OnSeekBarChangeListener()
            {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) 
				{//將填充率設定為拖拉後的值					
					float f=progress;
					tcl=(float) (TCLMIN+(TCLMAX-TCLMIN)*f/100.0f);
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {	}
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) { }            	
            }
        );	    
		curr=WhichView.PARAMSSET_VIEW;		//設定目前View為參數設定View
    }
    public boolean onKeyDown(int keyCode,KeyEvent e)
    {//點擊傳回鍵進行跳躍    	
    	if(keyCode==4)
    	{	
    		if(curr==WhichView.BGCOLOR_VIEW){//從背景設定View跳躍到繪制View
    			gotoMainView();
			}
    		else if(curr==WhichView.SETUP_VIEW){//從主設定View跳躍到繪制View
    			gotoMainView();
    		}
    		else if(curr==WhichView.PARAMSSET_VIEW){//從參數設定View跳躍到繪制View
    			gotoMainView();
    		}
    		else if(curr==WhichView.GALLERY_VIEW){//從畫廊View跳躍到繪制View
    			gotoMainView();
    		}
    		else{							//離開殺死執行緒
    			this.finish();
    			new Thread()
    			{
    				public void run()
    				{
    					try {
							Thread.sleep(1000);
						} catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
    					System.exit(0); 
    				}
    			}.start();	    		  
			} 		
    	}    	
    	return true;
    }

	@Override
	protected void onPause() 
	{
		super.onPause();
		//每次暫停自動存碟
		try
    	{
    		File f=new File("/sdcard/sp.data");
    		FileOutputStream fout=new FileOutputStream(f);
    		ObjectOutputStream oout=new ObjectOutputStream(fout);
    		oout.writeObject(Constant.history);
    		oout.close();
    		fout.close();
    	}
    	catch(Exception e)
    	{    		
    		e.printStackTrace();
    	}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() 
	{
		super.onResume();
		//每次還原自動讀取
		try
    	{
    		File f=new File("/sdcard/sp.data");
    		FileInputStream fin=new FileInputStream(f);
    		ObjectInputStream oin=new ObjectInputStream(fin);    		
    		Constant.history=(LinkedHashMap<String,byte[]>)oin.readObject();
    		oin.close();
    		fin.close();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
	}
   
    
    
}
