package com.bn;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Constant 
{
	public static final int NAME_INPUT_DIALOG_ID=0;//沙畫名稱輸入交談視窗id
	public static final int DELETE_DIALOG_ID=1;
	public static final float hs=480; 	//手機螢幕標准寬度
	public static final float ws=800; 	//手機螢幕標准高度
	public static float SCREEN_WIDTH;	//螢幕寬度
	public static float SCREEN_HEIGHT;	//螢幕高度
	public static float SCALE;	        //使用的縮放比例
	public static float[][] PIC_LOCATION_MSG;
	public static int [] SANDCOLOR={196, 165, 43};//沙子彩色陣列在AtomAction中取得
	public static int state=0;			//目前繪制狀態   0-填沙  1-清沙
	public static float hbSize=10;		//畫筆半徑
	public static float tcl=40;			//填充率  
	public static final float STANDARD_LENGTH=30; //畫筆每 筆的標准單位長度
	public final static float TCLMAX=100;//填充率的最大值
	public final static float TCLMIN=10;//填充率的最小值
	public final static float HBMAX=20;	//畫筆半徑的最大值
	public final static float HBMIN=5;	//畫筆半徑的最小值
	public static float AREA_WIDTH;	//沙畫區域寬度
	public static float AREA_HEIGHT;//沙畫區域高度
	public static float pic_w;		//小圖片寬
	public static float pic_h;		//小圖片高
	public static float bitmap_between_width=5;//小圖片間隔
	public static Object actionLock=new Object();//鎖物件
	
	public static int galleryPageNo=0;//目前沙畫頁數
	public static int galleryPageSpan=6;//每頁顯示數
	public static int galleryPageCount=0;//沙畫的數量
	
	//動作清單
	static LinkedHashSet<AtomAction> alAction=new LinkedHashSet<AtomAction>();//最小動作
	static ArrayList<ActionGroup> allActiongroup=new ArrayList<ActionGroup>();//動作群組合
	static HashMap<Long,Bitmap> hmhb=new HashMap<Long,Bitmap>();//圖片快取儲存位置
	//目前圖形緩沖
	static Bitmap bmBuff;//Bitmap快取用來儲存已經畫了的每一筆
	static Canvas canvasBuff; 
	
	//歷史記錄清單
	static LinkedHashMap<String,byte[]> history=new LinkedHashMap<String,byte[]>();
	public static ArrayList<Record> alr;//用來儲存沙畫記錄
	static ArrayList<String> aln;//用來儲存沙畫名
	public static int[] SETUP_AN_ID = new int[]
  	{//SETUP中按鈕圖片資源ID
  		R.drawable.setup0,R.drawable.setup1,
  		R.drawable.setup2,R.drawable.setup3,
  		R.drawable.setup4,R.drawable.s5
  	};
	public static int[] BG_PIC_ID = new int[]
	{//背景圖片資源ID
		R.drawable.bj0,R.drawable.bj1,
		R.drawable.bj2,R.drawable.bj3,
		R.drawable.bj4,R.drawable.bj5,
		R.drawable.bj6,R.drawable.bj7
	};	
	public static int[] MAIN_AN_ID = new int[]
	{//MAINVIEW中按鈕圖片資源ID
		R.drawable.a1,R.drawable.a2,
		R.drawable.a3,R.drawable.a4,
		R.drawable.a5
	};
	public static int[] WELCOME_PIC_ID = new int[]{//歡迎頁面圖片資源ID
		R.drawable.z6,R.drawable.choicebg,
		R.drawable.zpjkong,R.drawable.zpj,
		R.drawable.prepage,R.drawable.nextpage
	};

	public static void getRatio(){		//求出在實際使用時的縮放比
		float wratio=SCREEN_WIDTH/ws;				//水平座標比例	
		float hratio=SCREEN_HEIGHT/hs;				//垂直座標比例	
		if(wratio<hratio){
			SCALE=wratio;
		}else{
			SCALE=hratio; 
		}
		hbSize=SCALE*hbSize;		//畫筆半徑實際值
	}
	public static Bitmap []BGCOLOR_ARRAY;//設定頁面圖片資源陣列
	public static Bitmap []BGCOLOR_ARRAY_BIG;//設定頁面圖片資源陣列
	public static Bitmap [] SETUP_ARRAY;//設定頁面圖片資源陣列
	public static Bitmap [] WELCOME_ARRAY;//歡迎頁面圖片資源陣列
	public static Bitmap [] MAIN_AN_ARRAY;//MAINVIEW中按鈕圖片資源陣列
	
	public static Bitmap scaleToFitXYRatio(Bitmap bm,float xRatio,float yRatio)//縮放圖片的方法
	{
	   	float width = bm.getWidth(); 	//圖片寬度
	   	float height = bm.getHeight();	//圖片高度
	   	Matrix m1 = new Matrix(); 
	   	m1.postScale(xRatio, yRatio);   	
	   	Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)height, m1, true);//宣告點陣圖   
	   	return bmResult;
	}
	public static void initPicture(Resources res)
	{//起始化圖片陣列
		BGCOLOR_ARRAY = new Bitmap[BG_PIC_ID.length];
		BGCOLOR_ARRAY_BIG=new Bitmap[BG_PIC_ID.length];
		SETUP_ARRAY = new Bitmap[SETUP_AN_ID.length];
		WELCOME_ARRAY = new Bitmap[WELCOME_PIC_ID.length];
		MAIN_AN_ARRAY = new Bitmap[MAIN_AN_ID.length];
		for(int i=0;i<BG_PIC_ID.length;i++)
		{//處理圖片以適應本機螢幕分別為圖片陣列
			BGCOLOR_ARRAY[i]=BitmapFactory.decodeResource(res, BG_PIC_ID[i]);
			BGCOLOR_ARRAY[i]=Constant.scaleToFitXYRatio(BGCOLOR_ARRAY[i], SCALE,SCALE);
		}
		for(int i=0;i<SETUP_AN_ID.length;i++)
		{//處理圖片以適應本機螢幕分別為歡迎頁圖片和MAINVIEW中按鈕圖片陣列
			SETUP_ARRAY[i]=BitmapFactory.decodeResource(res, SETUP_AN_ID[i]);
			SETUP_ARRAY[i]=Constant.scaleToFitXYRatio(SETUP_ARRAY[i], SCALE,SCALE);
		}
		for(int i=0;i<WELCOME_PIC_ID.length;i++)
		{//處理圖片以適應本機螢幕分別為歡迎頁圖片和MAINVIEW中按鈕圖片陣列
			WELCOME_ARRAY[i]=BitmapFactory.decodeResource(res, WELCOME_PIC_ID[i]);
			WELCOME_ARRAY[i]=Constant.scaleToFitXYRatio(WELCOME_ARRAY[i], SCALE,SCALE);
		}
		for(int i=0;i<MAIN_AN_ID.length;i++)
		{
			MAIN_AN_ARRAY[i]=BitmapFactory.decodeResource(res, MAIN_AN_ID[i]);
			MAIN_AN_ARRAY[i]=Constant.scaleToFitXYRatio(MAIN_AN_ARRAY[i], SCALE,SCALE);
		}		
	}
	public static float an_xOffset=90;//按鈕的x偏移量
	public static float an_yOffset=5; //按鈕的y偏移量
	public static float bg_Offset=10; //背景按鈕的y偏移量
	public static int jg_Num=6;       //按鈕分割空白的數量

	public static float su_xOffset=90;//設定按鈕的x偏移量
	public static float su_yOffset=60; //設定按鈕的y偏移量
	public static int sujg_Num=4;       //設定按鈕分割空白的數量
	public static float choicebg_xOffset=100;//設定背景界面的提示語圖片""的x偏移量
	public static float choicebg_yOffset=5;//設定背景界面的提示語圖片""的y偏移量
	public static float sh_xOffset=35;//設定背景界面的沙畫圖片""的x偏移量
	public static float sh_yOffset=20;//設定背景界面的沙畫圖片""的y偏移量
	public static float shqOffset=5;//進行沙畫區域的xy偏移量		

	public static float [] [] getPicLocationMsg()
	{
		PIC_LOCATION_MSG=new float [][]
		{
				{//填沙按鈕的xy:PIC_LOCATION_MSG[0]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num					
				},
				{//清沙按鈕的xy:PIC_LOCATION_MSG[1]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num+(SCREEN_HEIGHT+MAIN_AN_ARRAY[0].getHeight())/jg_Num*1	
				},
				{//取消按鈕的xy:PIC_LOCATION_MSG[2]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num+(SCREEN_HEIGHT+MAIN_AN_ARRAY[0].getHeight())/jg_Num*2	
				},
				{//背景燈按鈕的xy:PIC_LOCATION_MSG[3]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num+(SCREEN_HEIGHT+MAIN_AN_ARRAY[0].getHeight())/jg_Num*3	
				},
				{//設定按鈕的xy:PIC_LOCATION_MSG[4]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num+(SCREEN_HEIGHT+MAIN_AN_ARRAY[0].getHeight())/jg_Num*4	
				},
				{//矩形的xy四點:PIC_LOCATION_MSG[5]
					//shqOffset*SCALE,shqOffset*SCALE,w-100*SCALE,h-3
					0,0,SCREEN_WIDTH-100*SCALE,SCREEN_HEIGHT-3
				},
				{//歡迎圖片的xy:PIC_LOCATION_MSG[6]
					(SCREEN_WIDTH-WELCOME_ARRAY[0].getWidth())/2,(SCREEN_HEIGHT-WELCOME_ARRAY[0].getHeight())/2
				},
				{//設定圖片"新增畫布"的xy:PIC_LOCATION_MSG[7]
					su_xOffset*SCALE,su_yOffset*SCALE
				},
				{//設定圖片"儲存畫布"的xy:PIC_LOCATION_MSG[8]
					su_xOffset*SCALE,su_yOffset*SCALE+(SCREEN_HEIGHT-SETUP_ARRAY[0].getHeight()*5-2*su_yOffset*SCALE)/4+SETUP_ARRAY[0].getHeight()
				},
				{//設定圖片"參數設定"的xy:PIC_LOCATION_MSG[9]
					su_xOffset*SCALE,su_yOffset*SCALE+((SCREEN_HEIGHT-SETUP_ARRAY[0].getHeight()*5-2*su_yOffset*SCALE)/4+SETUP_ARRAY[0].getHeight())*2
				},
				{//設定圖片"作品集"的xy:PIC_LOCATION_MSG[10]
					su_xOffset*SCALE,su_yOffset*SCALE+((SCREEN_HEIGHT-SETUP_ARRAY[0].getHeight()*5-2*su_yOffset*SCALE)/4+SETUP_ARRAY[0].getHeight())*3
				},
				{//設定圖片"離開"的xy:PIC_LOCATION_MSG[11]
					su_xOffset*SCALE,su_yOffset*SCALE+((SCREEN_HEIGHT-SETUP_ARRAY[0].getHeight()*5-2*su_yOffset*SCALE)/4+SETUP_ARRAY[0].getHeight())*4
				},
				{//設定圖片""的xy:PIC_LOCATION_MSG[12]
					(SCREEN_WIDTH+SETUP_ARRAY[0].getWidth()+su_xOffset*SCALE-SETUP_ARRAY[5].getWidth())/2,(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2
				},
				{//設定背景界面的沙畫圖片""的xy:PIC_LOCATION_MSG[13]
					sh_xOffset*SCALE,(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+sh_yOffset*SCALE
				},
				{//設定背景界面的背景彩色圖片""的xy:PIC_LOCATION_MSG[14]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+an_xOffset*SCALE,
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2
				},
				{//設定背景界面的背景彩色圖片""的xy:PIC_LOCATION_MSG[15]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+2*an_xOffset*SCALE+BGCOLOR_ARRAY[0].getWidth(),
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2
				},
				{//設定背景界面的背景彩色圖片""的xy:PIC_LOCATION_MSG[16]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+an_xOffset*SCALE,
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+BGCOLOR_ARRAY[0].getHeight()
				},
				{//設定背景界面的背景彩色圖片""的xy:PIC_LOCATION_MSG[17]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+2*an_xOffset*SCALE+BGCOLOR_ARRAY[0].getWidth(),
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+BGCOLOR_ARRAY[0].getHeight()
				},
				{//設定背景界面的背景彩色圖片""的xy:PIC_LOCATION_MSG[18]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+an_xOffset*SCALE,
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+2*(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+2*BGCOLOR_ARRAY[0].getHeight()
				},
				{//設定背景界面的背景彩色圖片""的xy:PIC_LOCATION_MSG[19]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+2*an_xOffset*SCALE+BGCOLOR_ARRAY[0].getWidth(),
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+2*(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+2*BGCOLOR_ARRAY[0].getHeight()
				},
				{//設定背景界面的背景彩色圖片""的xy:PIC_LOCATION_MSG[20]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+an_xOffset*SCALE,
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+3*(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+3*BGCOLOR_ARRAY[0].getHeight()
				},
				{//設定背景界面的背景彩色圖片""的xy:PIC_LOCATION_MSG[21]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+2*an_xOffset*SCALE+BGCOLOR_ARRAY[0].getWidth(),
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+3*(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+3*BGCOLOR_ARRAY[0].getHeight()
				},
				{//設定背景界面的文字圖片""的xy:PIC_LOCATION_MSG[22]
					choicebg_xOffset*SCALE,choicebg_yOffset*SCALE
				}
		};	
		
		AREA_WIDTH=(int)(PIC_LOCATION_MSG[5][2]-PIC_LOCATION_MSG[5][0]);	//沙畫區域寬度
		AREA_HEIGHT=(int)(PIC_LOCATION_MSG[5][3]-PIC_LOCATION_MSG[5][1]);//沙畫區域高度
		pic_w=444;		//小圖片寬
		pic_h=pic_w*AREA_HEIGHT/AREA_WIDTH;		//小圖片高		
		for(int i=0;i<BG_PIC_ID.length;i++)
		{//處理圖片以適應本機螢幕分別為圖片陣列
			float xratio=PIC_LOCATION_MSG[5][2]/BGCOLOR_ARRAY[1].getWidth();
			float yratio=PIC_LOCATION_MSG[5][3]/BGCOLOR_ARRAY[1].getHeight();
			BGCOLOR_ARRAY_BIG[i]=Constant.scaleToFitXYRatio(BGCOLOR_ARRAY[i], xratio,yratio);
		}
		
		return PIC_LOCATION_MSG;
	}
}
