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

enum WhichView {WELCOME_VIEW,MAIN_VIEW,SETUP_VIEW,BGCOLOR_VIEW,PARAMSSET_VIEW,GALLERY_VIEW}//�Ҧ�VIEW���M��
public class SandPaintingActivity extends Activity 
{

	AttributeSet attrs;
	private ShowGalleryView sgView;
	private WelcomeView myWelcomeView;	//�ŧi�w�ﭶ�Ѧ�
	private MainView myMainView;		//�ŧiø��Ѧ�
	private SetupView mySetupView;		//�ŧi�D�]�w���Ѧ�
	private BgColorView myBgColorView;	//�ŧi�I���]�w���Ѧ�
	WhichView curr;						//�ثe�Ҧbview
	Dialog nameInputdialog; 
	String name;						//�ŧi�ҿ�����F�e�W�Ѧ�
	//�ŧiDialog�Ѧ�
	Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			if(msg.what==1)
			{			//�q�w�ﭶ���D��D����
				if(myWelcomeView!=null)
				{
					myWelcomeView=null;
				}
				gotoMainView(); 
			}
			else if(msg.what==2)
			{		//�qmain�������D��D�]�w����
				if(myMainView!=null)
				{
					myMainView=null;
				}
				gotoSetupView(); 
			}
			else if(msg.what==3)
			{		//�q�I������������D��D����
				if(myMainView!=null)
				{
					myMainView=null;
				}
				gotoMainView(); 
			}
			else if(msg.what==4)
			{		//�z�Lmain���������I�������s���D��I���������
				if(myBgColorView!=null)
				{
					myBgColorView=null;
				}
				gotoBgColorView(); 
			}
			else if(msg.what==5)
			{		//�z�Lmain���������e���]�w�����s�i��e���M�Q��b�|�]�w���������D
				gotoParamsSetView(); 
			}
			else if(msg.what==6)
			{
				showSaveDiaolg();		//����x�s��͵���
				
			}
			else if(msg.what==7)
			{
				galleryPageNo=0;
				galleryPageCount=history.size()/galleryPageSpan;
				if(history.size()%galleryPageSpan!=0)
				{
					galleryPageCount=galleryPageCount+1;
				}
				gotoGalleryView();			//��ܵe�Y
			}
			else if(msg.what==8)
			{	//�q�@�~�������D��D����
				if(myMainView!=null)
				{
					myMainView=null;
				}
				gotoMainView();//���D����ܭ���
			}
			else if(msg.what==9)//�������w�@�~
			{
				Bundle b=msg.getData();
				name=b.getString("name");
				showDialog(DELETE_DIALOG_ID);
			}		
			else if(msg.what==10){//�I���@�~���W�@�����D
				prePage();				
			}
			else if(msg.what==11){//�I���@�~���W�@�����D
				nextPage();
			}
		}
	};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);          
        //�U��y���]�w���ù�
        requestWindowFeature(Window.FEATURE_NO_TITLE);         
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        if(dm.widthPixels>dm.heightPixels)
        {//�o���ڿù�width�Mheight�öi������ഫ
        	 SCREEN_WIDTH=dm.widthPixels;
             SCREEN_HEIGHT=dm.heightPixels;
        }
        else
        {
        	SCREEN_WIDTH=dm.heightPixels;
            SCREEN_HEIGHT=dm.widthPixels;
        }
		Constant.getRatio();			//�o���Y���		
		Constant.initPicture(this.getResources());//�B�z�Ϥ��H�A���ù�
		Constant.getPicLocationMsg();	//�_�l�ƹϤ��T�����tx,y,(w,h)

        
		gotoWelcomeView();				//���D���w�ﭶ
		myWelcomeView.requestFocus();	//���o�J�I
		myWelcomeView.setFocusableInTouchMode(true);//�]�w���iĲ��
    }
    
    public void gotoWelcomeView()
    {		//���D���w��ɭ�
    	if(null==myWelcomeView)
    	{
        	myWelcomeView= new WelcomeView(this);
    	}
        setContentView(myWelcomeView);
    	curr=WhichView.WELCOME_VIEW;
    }
    public void gotoMainView()
    {			//���D��Main�ɭ�
    	if(myMainView==null)
    	{
    		myMainView = new MainView(SandPaintingActivity.this);
    	}
		SandPaintingActivity.this.setContentView(myMainView);
		curr=WhichView.MAIN_VIEW;
    }
    public void gotoSetupView()
    {		//���D��]�w�ɭ�
    	if(null==mySetupView)
    	{
        	mySetupView = new SetupView(this);
    	}
		SandPaintingActivity.this.setContentView(mySetupView);
		curr=WhichView.SETUP_VIEW;
    }
    public void gotoBgColorView()
    {		//���D��I���ɭ�
    	if(null==myBgColorView)
    	{
        	myBgColorView = new BgColorView(this);
    	}
		SandPaintingActivity.this.setContentView(myBgColorView);
		curr=WhichView.BGCOLOR_VIEW;	//�]�w�ثeView���ѼƳ]�wView
    }
    public void showSaveDiaolg()
    {		//����x�s��͵���
    	showDialog(NAME_INPUT_DIALOG_ID);
    }	 
    
    public void nextPage()
    {//���D��U�@���A�`�����D�A��b�̫�@�����D�ɦ۰ʸ��D��Ĥ@��
    	galleryPageNo=(galleryPageNo+1)%galleryPageCount;
    	flushHistory();
    	sgView.postInvalidate();
    }
    
    public void prePage()
    {//���D��W�@���A�`�����D�A��q�Ĥ@�����D�ɡA�N�۰ʸ��D��̫�@��
    	galleryPageNo=(galleryPageNo-1+galleryPageCount)%galleryPageCount;
    	flushHistory();
    	sgView.postInvalidate();
    }    
    
    public void flushHistory()
    {//��s���v�O��
    	if(alr!=null)
		{//�o�̬O�^��bitmap���֨��A�o�˥i�H�ﵽ�t��
			for(Record r:alr)
			{//�Nrecord�x�s���w�g�e�F���C�@���^��
				r.bmResult.recycle();
			}
		}    	
    	int start=galleryPageNo*galleryPageSpan;//�p��C�@�����Ĥ@�T�Ϥ�������
    	int count=0;
		
		ArrayList<Record> alr=new ArrayList<Record>();//�ŧi�F�e�Ϥ��T��Record��ArrayList
		ArrayList<String> aln=new ArrayList<String>();//�ŧi�F�e�Ϥ��W��ArrayList
		Set<String> ks=history.keySet();
		for(String key:ks)
		{
			if(count>=start&&count<start+galleryPageSpan)
			{//�ھڭ��ƨ��X�������S�]���ϡA�C���u�֨��@�����Ϥ��A�o�˴N���|�y���O���鷸��
				byte[] data=history.get(key);
				Record rTemp=Record.fromBytesToRecord(data);
				if(rTemp.bmResult==null)
				{
					throw new RuntimeException("hua kong");
				}
				aln.add(key);//�N�ӭ�����Ʀs�i�W�٦C
				alr.add(rTemp);//�N�Ϥ���Ʀs�i�Ϥ��C
			}
			count++;
		}			
		Constant.alr=alr;//�N�䵹���ȵ��R�A���
		Constant.aln=aln;
    }
	
	public void gotoGalleryView()
	{
		flushHistory();
		SandPaintingActivity.this.setContentView(R.layout.showgallerymain);
        sgView=(ShowGalleryView)findViewById(R.id.sgView);//���o��ܹϤ���View������æb�䤤ø��Ϥ�     
		curr=WhichView.GALLERY_VIEW;
	}
	
    @Override
    public Dialog onCreateDialog(int id)//�إߥ�͵���
    {    	
        Dialog result=null;
    	switch(id)
    	{
	    	case NAME_INPUT_DIALOG_ID://�m�W��J��͵���
		    	nameInputdialog=new MyDialog(this); 	    
				result=nameInputdialog;				
			break;	
	    	case DELETE_DIALOG_ID://�����Ϥ��O�_�������A�Y�G����T�w�N���������F�e
	    		Builder b=new AlertDialog.Builder(this);  
	    		  b.setMessage("�O�_�����ӹϤ��H");//�]�w�T��
	    		  b.setPositiveButton//����͵����]�w���s
	    		  (
	    				"�T�w", 
	    				new DialogInterface.OnClickListener()
		        		{
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Toast.makeText(SandPaintingActivity.this, "�����Ŀ諸�M��"+name, Toast.LENGTH_SHORT).show();					
								history.remove(name);//�����o�@�W�r���F�e
								gotoGalleryView();
							}      			
		        		}
	    		  );
	    		  b.setNegativeButton
	    		  (
	    				"����",
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
    
    //�C���X�{��͵����ɳQ�^�եH�ʺA��s��͵������e����k
    @Override
    public void onPrepareDialog(int id, final Dialog dialog)
    {
    	//�Y���O���ݥ�͵����h�Ǧ^
    	switch(id)
    	{
    	   case NAME_INPUT_DIALOG_ID://�m�W��J��͵���
    		   //�T�w���s
    		   Button bjhmcok=(Button)nameInputdialog.findViewById(R.id.saveOk);
    		   //�������s
       		   Button bjhmccancel=(Button)nameInputdialog.findViewById(R.id.saveCancle);
       		   //���T�w���s�[�J��ť��
       		   bjhmcok.setOnClickListener
               (
    	          new OnClickListener()
    	          {
    				@Override
    				public void onClick(View v) 
    				{
    					//���o��͵����̪����e�å�Toast���
    					EditText et=(EditText)nameInputdialog.findViewById(R.id.etname);
    					
    					String name=et.getText().toString();    					
    					Record r=new Record();
    					history.put(name, r.toBytes()); 
    					Toast.makeText
    					(
    						SandPaintingActivity.this,
    						"���\�x�s�i�w�R�I", 
    						Toast.LENGTH_SHORT
    					).show(); 
    					nameInputdialog.cancel();
    					
    					//�C���٭�۰�Ū��
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
    			    			"���ॿ�TŪ�gSD�d�A�{�����}��\n��Ʀ��i��|�򥢡I", 
    			    			Toast.LENGTH_SHORT
    			    		).show();
    			    	}
    				}        	  
    	          }
    	        );   
       		    //���������s�[�J��ť��
       		    bjhmccancel.setOnClickListener
	            (
	 	          new OnClickListener()
	 	          {
	 				@Override
	 				public void onClick(View v) 
	 				{
	 					//������͵���
	 					nameInputdialog.cancel();					
	 				}        	  
	 	          }
	 	        );   
    	   break;	
    	}
    }    
    public void gotoParamsSetView()
    {	//���D��ѼƳ]�w�����A�ϥ�xml�s�զ�
		this.setContentView(R.layout.paramset);
		SeekBar sbHB=(SeekBar)this.findViewById(R.id.SeekBar01);//�o��e���b�|SeekBar�Ѧ�			
        int currValue=(int) ((hbSize-HBMIN)/(HBMAX-HBMIN)*100);
		sbHB.setProgress(currValue);
		sbHB.setOnSeekBarChangeListener(//���e���b�|SeekBar�]�w��ť
            new SeekBar.OnSeekBarChangeListener()
            {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) 
				{//�N�e���b�|�]�w����ԫ᪺��	
					float f=progress;
					hbSize=(float) (HBMIN+(HBMAX-HBMIN)*f/100.0f);
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {	}
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) { }            	
            }
        );	        
        SeekBar sbTCL=(SeekBar)this.findViewById(R.id.SeekBar02);//�o���R�vSeekBar�Ѧ�	
        currValue=(int) ((tcl-TCLMIN)/(TCLMAX-TCLMIN)*100);
		sbTCL.setProgress(currValue);
        sbTCL.setOnSeekBarChangeListener(//����R�vSeekBar�]�w��ť
            new SeekBar.OnSeekBarChangeListener()
            {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) 
				{//�N��R�v�]�w����ԫ᪺��					
					float f=progress;
					tcl=(float) (TCLMIN+(TCLMAX-TCLMIN)*f/100.0f);
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {	}
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) { }            	
            }
        );	    
		curr=WhichView.PARAMSSET_VIEW;		//�]�w�ثeView���ѼƳ]�wView
    }
    public boolean onKeyDown(int keyCode,KeyEvent e)
    {//�I���Ǧ^��i����D    	
    	if(keyCode==4)
    	{	
    		if(curr==WhichView.BGCOLOR_VIEW){//�q�I���]�wView���D��ø��View
    			gotoMainView();
			}
    		else if(curr==WhichView.SETUP_VIEW){//�q�D�]�wView���D��ø��View
    			gotoMainView();
    		}
    		else if(curr==WhichView.PARAMSSET_VIEW){//�q�ѼƳ]�wView���D��ø��View
    			gotoMainView();
    		}
    		else if(curr==WhichView.GALLERY_VIEW){//�q�e�YView���D��ø��View
    			gotoMainView();
    		}
    		else{							//���}���������
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
		//�C���Ȱ��۰ʦs��
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
		//�C���٭�۰�Ū��
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