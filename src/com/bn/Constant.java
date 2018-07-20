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
	public static final int NAME_INPUT_DIALOG_ID=0;//�F�e�W�ٿ�J��͵���id
	public static final int DELETE_DIALOG_ID=1;
	public static final float hs=480; 	//����ù��Э�e��
	public static final float ws=800; 	//����ù��Э㰪��
	public static float SCREEN_WIDTH;	//�ù��e��
	public static float SCREEN_HEIGHT;	//�ù�����
	public static float SCALE;	        //�ϥΪ��Y����
	public static float[][] PIC_LOCATION_MSG;
	public static int [] SANDCOLOR={196, 165, 43};//�F�l�m��}�C�bAtomAction�����o
	public static int state=0;			//�ثeø��A   0-��F  1-�M�F
	public static float hbSize=10;		//�e���b�|
	public static float tcl=40;			//��R�v  
	public static final float STANDARD_LENGTH=30; //�e���C �����Э������
	public final static float TCLMAX=100;//��R�v���̤j��
	public final static float TCLMIN=10;//��R�v���̤p��
	public final static float HBMAX=20;	//�e���b�|���̤j��
	public final static float HBMIN=5;	//�e���b�|���̤p��
	public static float AREA_WIDTH;	//�F�e�ϰ�e��
	public static float AREA_HEIGHT;//�F�e�ϰ찪��
	public static float pic_w;		//�p�Ϥ��e
	public static float pic_h;		//�p�Ϥ���
	public static float bitmap_between_width=5;//�p�Ϥ����j
	public static Object actionLock=new Object();//�ꪫ��
	
	public static int galleryPageNo=0;//�ثe�F�e����
	public static int galleryPageSpan=6;//�C����ܼ�
	public static int galleryPageCount=0;//�F�e���ƶq
	
	//�ʧ@�M��
	static LinkedHashSet<AtomAction> alAction=new LinkedHashSet<AtomAction>();//�̤p�ʧ@
	static ArrayList<ActionGroup> allActiongroup=new ArrayList<ActionGroup>();//�ʧ@�s�զX
	static HashMap<Long,Bitmap> hmhb=new HashMap<Long,Bitmap>();//�Ϥ��֨��x�s��m
	//�ثe�ϧνw�R
	static Bitmap bmBuff;//Bitmap�֨��Ψ��x�s�w�g�e�F���C�@��
	static Canvas canvasBuff; 
	
	//���v�O���M��
	static LinkedHashMap<String,byte[]> history=new LinkedHashMap<String,byte[]>();
	public static ArrayList<Record> alr;//�Ψ��x�s�F�e�O��
	static ArrayList<String> aln;//�Ψ��x�s�F�e�W
	public static int[] SETUP_AN_ID = new int[]
  	{//SETUP�����s�Ϥ��귽ID
  		R.drawable.setup0,R.drawable.setup1,
  		R.drawable.setup2,R.drawable.setup3,
  		R.drawable.setup4,R.drawable.s5
  	};
	public static int[] BG_PIC_ID = new int[]
	{//�I���Ϥ��귽ID
		R.drawable.bj0,R.drawable.bj1,
		R.drawable.bj2,R.drawable.bj3,
		R.drawable.bj4,R.drawable.bj5,
		R.drawable.bj6,R.drawable.bj7
	};	
	public static int[] MAIN_AN_ID = new int[]
	{//MAINVIEW�����s�Ϥ��귽ID
		R.drawable.a1,R.drawable.a2,
		R.drawable.a3,R.drawable.a4,
		R.drawable.a5
	};
	public static int[] WELCOME_PIC_ID = new int[]{//�w�ﭶ���Ϥ��귽ID
		R.drawable.z6,R.drawable.choicebg,
		R.drawable.zpjkong,R.drawable.zpj,
		R.drawable.prepage,R.drawable.nextpage
	};

	public static void getRatio(){		//�D�X�b��ڨϥήɪ��Y���
		float wratio=SCREEN_WIDTH/ws;				//�����y�Ф��	
		float hratio=SCREEN_HEIGHT/hs;				//�����y�Ф��	
		if(wratio<hratio){
			SCALE=wratio;
		}else{
			SCALE=hratio; 
		}
		hbSize=SCALE*hbSize;		//�e���b�|��ڭ�
	}
	public static Bitmap []BGCOLOR_ARRAY;//�]�w�����Ϥ��귽�}�C
	public static Bitmap []BGCOLOR_ARRAY_BIG;//�]�w�����Ϥ��귽�}�C
	public static Bitmap [] SETUP_ARRAY;//�]�w�����Ϥ��귽�}�C
	public static Bitmap [] WELCOME_ARRAY;//�w�ﭶ���Ϥ��귽�}�C
	public static Bitmap [] MAIN_AN_ARRAY;//MAINVIEW�����s�Ϥ��귽�}�C
	
	public static Bitmap scaleToFitXYRatio(Bitmap bm,float xRatio,float yRatio)//�Y��Ϥ�����k
	{
	   	float width = bm.getWidth(); 	//�Ϥ��e��
	   	float height = bm.getHeight();	//�Ϥ�����
	   	Matrix m1 = new Matrix(); 
	   	m1.postScale(xRatio, yRatio);   	
	   	Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)height, m1, true);//�ŧi�I�}��   
	   	return bmResult;
	}
	public static void initPicture(Resources res)
	{//�_�l�ƹϤ��}�C
		BGCOLOR_ARRAY = new Bitmap[BG_PIC_ID.length];
		BGCOLOR_ARRAY_BIG=new Bitmap[BG_PIC_ID.length];
		SETUP_ARRAY = new Bitmap[SETUP_AN_ID.length];
		WELCOME_ARRAY = new Bitmap[WELCOME_PIC_ID.length];
		MAIN_AN_ARRAY = new Bitmap[MAIN_AN_ID.length];
		for(int i=0;i<BG_PIC_ID.length;i++)
		{//�B�z�Ϥ��H�A�������ù����O���Ϥ��}�C
			BGCOLOR_ARRAY[i]=BitmapFactory.decodeResource(res, BG_PIC_ID[i]);
			BGCOLOR_ARRAY[i]=Constant.scaleToFitXYRatio(BGCOLOR_ARRAY[i], SCALE,SCALE);
		}
		for(int i=0;i<SETUP_AN_ID.length;i++)
		{//�B�z�Ϥ��H�A�������ù����O���w�ﭶ�Ϥ��MMAINVIEW�����s�Ϥ��}�C
			SETUP_ARRAY[i]=BitmapFactory.decodeResource(res, SETUP_AN_ID[i]);
			SETUP_ARRAY[i]=Constant.scaleToFitXYRatio(SETUP_ARRAY[i], SCALE,SCALE);
		}
		for(int i=0;i<WELCOME_PIC_ID.length;i++)
		{//�B�z�Ϥ��H�A�������ù����O���w�ﭶ�Ϥ��MMAINVIEW�����s�Ϥ��}�C
			WELCOME_ARRAY[i]=BitmapFactory.decodeResource(res, WELCOME_PIC_ID[i]);
			WELCOME_ARRAY[i]=Constant.scaleToFitXYRatio(WELCOME_ARRAY[i], SCALE,SCALE);
		}
		for(int i=0;i<MAIN_AN_ID.length;i++)
		{
			MAIN_AN_ARRAY[i]=BitmapFactory.decodeResource(res, MAIN_AN_ID[i]);
			MAIN_AN_ARRAY[i]=Constant.scaleToFitXYRatio(MAIN_AN_ARRAY[i], SCALE,SCALE);
		}		
	}
	public static float an_xOffset=90;//���s��x�����q
	public static float an_yOffset=5; //���s��y�����q
	public static float bg_Offset=10; //�I�����s��y�����q
	public static int jg_Num=6;       //���s���Ϊťժ��ƶq

	public static float su_xOffset=90;//�]�w���s��x�����q
	public static float su_yOffset=60; //�]�w���s��y�����q
	public static int sujg_Num=4;       //�]�w���s���Ϊťժ��ƶq
	public static float choicebg_xOffset=100;//�]�w�I���ɭ������ܻy�Ϥ�""��x�����q
	public static float choicebg_yOffset=5;//�]�w�I���ɭ������ܻy�Ϥ�""��y�����q
	public static float sh_xOffset=35;//�]�w�I���ɭ����F�e�Ϥ�""��x�����q
	public static float sh_yOffset=20;//�]�w�I���ɭ����F�e�Ϥ�""��y�����q
	public static float shqOffset=5;//�i��F�e�ϰ쪺xy�����q		

	public static float [] [] getPicLocationMsg()
	{
		PIC_LOCATION_MSG=new float [][]
		{
				{//��F���s��xy:PIC_LOCATION_MSG[0]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num					
				},
				{//�M�F���s��xy:PIC_LOCATION_MSG[1]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num+(SCREEN_HEIGHT+MAIN_AN_ARRAY[0].getHeight())/jg_Num*1	
				},
				{//�������s��xy:PIC_LOCATION_MSG[2]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num+(SCREEN_HEIGHT+MAIN_AN_ARRAY[0].getHeight())/jg_Num*2	
				},
				{//�I���O���s��xy:PIC_LOCATION_MSG[3]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num+(SCREEN_HEIGHT+MAIN_AN_ARRAY[0].getHeight())/jg_Num*3	
				},
				{//�]�w���s��xy:PIC_LOCATION_MSG[4]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num+(SCREEN_HEIGHT+MAIN_AN_ARRAY[0].getHeight())/jg_Num*4	
				},
				{//�x�Ϊ�xy�|�I:PIC_LOCATION_MSG[5]
					//shqOffset*SCALE,shqOffset*SCALE,w-100*SCALE,h-3
					0,0,SCREEN_WIDTH-100*SCALE,SCREEN_HEIGHT-3
				},
				{//�w��Ϥ���xy:PIC_LOCATION_MSG[6]
					(SCREEN_WIDTH-WELCOME_ARRAY[0].getWidth())/2,(SCREEN_HEIGHT-WELCOME_ARRAY[0].getHeight())/2
				},
				{//�]�w�Ϥ�"�s�W�e��"��xy:PIC_LOCATION_MSG[7]
					su_xOffset*SCALE,su_yOffset*SCALE
				},
				{//�]�w�Ϥ�"�x�s�e��"��xy:PIC_LOCATION_MSG[8]
					su_xOffset*SCALE,su_yOffset*SCALE+(SCREEN_HEIGHT-SETUP_ARRAY[0].getHeight()*5-2*su_yOffset*SCALE)/4+SETUP_ARRAY[0].getHeight()
				},
				{//�]�w�Ϥ�"�ѼƳ]�w"��xy:PIC_LOCATION_MSG[9]
					su_xOffset*SCALE,su_yOffset*SCALE+((SCREEN_HEIGHT-SETUP_ARRAY[0].getHeight()*5-2*su_yOffset*SCALE)/4+SETUP_ARRAY[0].getHeight())*2
				},
				{//�]�w�Ϥ�"�@�~��"��xy:PIC_LOCATION_MSG[10]
					su_xOffset*SCALE,su_yOffset*SCALE+((SCREEN_HEIGHT-SETUP_ARRAY[0].getHeight()*5-2*su_yOffset*SCALE)/4+SETUP_ARRAY[0].getHeight())*3
				},
				{//�]�w�Ϥ�"���}"��xy:PIC_LOCATION_MSG[11]
					su_xOffset*SCALE,su_yOffset*SCALE+((SCREEN_HEIGHT-SETUP_ARRAY[0].getHeight()*5-2*su_yOffset*SCALE)/4+SETUP_ARRAY[0].getHeight())*4
				},
				{//�]�w�Ϥ�""��xy:PIC_LOCATION_MSG[12]
					(SCREEN_WIDTH+SETUP_ARRAY[0].getWidth()+su_xOffset*SCALE-SETUP_ARRAY[5].getWidth())/2,(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2
				},
				{//�]�w�I���ɭ����F�e�Ϥ�""��xy:PIC_LOCATION_MSG[13]
					sh_xOffset*SCALE,(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+sh_yOffset*SCALE
				},
				{//�]�w�I���ɭ����I���m��Ϥ�""��xy:PIC_LOCATION_MSG[14]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+an_xOffset*SCALE,
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2
				},
				{//�]�w�I���ɭ����I���m��Ϥ�""��xy:PIC_LOCATION_MSG[15]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+2*an_xOffset*SCALE+BGCOLOR_ARRAY[0].getWidth(),
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2
				},
				{//�]�w�I���ɭ����I���m��Ϥ�""��xy:PIC_LOCATION_MSG[16]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+an_xOffset*SCALE,
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+BGCOLOR_ARRAY[0].getHeight()
				},
				{//�]�w�I���ɭ����I���m��Ϥ�""��xy:PIC_LOCATION_MSG[17]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+2*an_xOffset*SCALE+BGCOLOR_ARRAY[0].getWidth(),
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+BGCOLOR_ARRAY[0].getHeight()
				},
				{//�]�w�I���ɭ����I���m��Ϥ�""��xy:PIC_LOCATION_MSG[18]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+an_xOffset*SCALE,
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+2*(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+2*BGCOLOR_ARRAY[0].getHeight()
				},
				{//�]�w�I���ɭ����I���m��Ϥ�""��xy:PIC_LOCATION_MSG[19]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+2*an_xOffset*SCALE+BGCOLOR_ARRAY[0].getWidth(),
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+2*(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+2*BGCOLOR_ARRAY[0].getHeight()
				},
				{//�]�w�I���ɭ����I���m��Ϥ�""��xy:PIC_LOCATION_MSG[20]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+an_xOffset*SCALE,
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+3*(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+3*BGCOLOR_ARRAY[0].getHeight()
				},
				{//�]�w�I���ɭ����I���m��Ϥ�""��xy:PIC_LOCATION_MSG[21]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+2*an_xOffset*SCALE+BGCOLOR_ARRAY[0].getWidth(),
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+3*(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+3*BGCOLOR_ARRAY[0].getHeight()
				},
				{//�]�w�I���ɭ�����r�Ϥ�""��xy:PIC_LOCATION_MSG[22]
					choicebg_xOffset*SCALE,choicebg_yOffset*SCALE
				}
		};	
		
		AREA_WIDTH=(int)(PIC_LOCATION_MSG[5][2]-PIC_LOCATION_MSG[5][0]);	//�F�e�ϰ�e��
		AREA_HEIGHT=(int)(PIC_LOCATION_MSG[5][3]-PIC_LOCATION_MSG[5][1]);//�F�e�ϰ찪��
		pic_w=444;		//�p�Ϥ��e
		pic_h=pic_w*AREA_HEIGHT/AREA_WIDTH;		//�p�Ϥ���		
		for(int i=0;i<BG_PIC_ID.length;i++)
		{//�B�z�Ϥ��H�A�������ù����O���Ϥ��}�C
			float xratio=PIC_LOCATION_MSG[5][2]/BGCOLOR_ARRAY[1].getWidth();
			float yratio=PIC_LOCATION_MSG[5][3]/BGCOLOR_ARRAY[1].getHeight();
			BGCOLOR_ARRAY_BIG[i]=Constant.scaleToFitXYRatio(BGCOLOR_ARRAY[i], xratio,yratio);
		}
		
		return PIC_LOCATION_MSG;
	}
}
