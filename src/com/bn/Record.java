package com.bn;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//�@�T�F�e�����
public class Record
{
	LinkedHashSet<AtomAction> alAction;
	ArrayList<ActionGroup> allActiongroup;
	HashMap<Long,Bitmap> hmhb;//�Ϥ��֨��x�s��m
	Bitmap bmResult;//Bitmap�֨��Ψ��x�s�w�g�e�F���C�@��
	int bgIndex;    //�I���Ϥ�������
	
	public Record()
	{//���o�ثe���ʵ۪��F�e���U�ذT��
		this.alAction=Constant.alAction;
		this.allActiongroup=Constant.allActiongroup;
		this.hmhb=Constant.hmhb;
		this.bmResult=Constant.bmBuff;
		this.bgIndex=BgColorView.areaFlag;
	}
	
	public Record(LinkedHashSet<AtomAction> alAction,ArrayList<ActionGroup> allActiongroup,HashMap<Long,Bitmap> hmhb,Bitmap bmResult,int bgIndex)
	{//�إߨF�e�O������
		this.alAction=alAction;
		this.allActiongroup=allActiongroup;
		this.hmhb=hmhb;
		this.bmResult=bmResult;
		this.bgIndex=bgIndex;
	}
	
	public byte[] toBytes()
	{//�NRecord�����ରbyte�}�C
		byte[] result=null;		
		  
		try 
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			ObjectOutputStream oout=new ObjectOutputStream(baos);
			oout.writeObject(this.alAction);
			oout.writeObject(this.allActiongroup);
			oout.writeObject(fromBitmapMapToBytesMap(hmhb));
			oout.writeObject(fromBitmapToBytes(bmResult));
			oout.writeObject(Integer.valueOf(bgIndex));
			result=baos.toByteArray();
			oout.close();
			
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static Record fromBytesToRecord(byte[] data)
	{//�Nbyte�}�C�ରRecord����
		Record result=null;
		try 
		{
			ByteArrayInputStream bais=new ByteArrayInputStream(data);
			ObjectInputStream oin;
			oin = new ObjectInputStream(bais);			
			LinkedHashSet<AtomAction> alAction=(LinkedHashSet<AtomAction>)oin.readObject();
			ArrayList<ActionGroup> allActiongroup=(ArrayList<ActionGroup>)oin.readObject();
			HashMap<Long,byte[]>hmbb=(HashMap<Long,byte[]>)oin.readObject();
			HashMap<Long,Bitmap> hmhb=fromBytesMapToBitmapMap(hmbb);
			Bitmap bmResult=fromBytesToBitmap((byte[])oin.readObject());
			int bgIndex=(Integer)oin.readObject();			
			result=new Record(alAction,allActiongroup,hmhb,bmResult,bgIndex);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static HashMap<Long,byte[]> fromBitmapMapToBytesMap(HashMap<Long,Bitmap> hmhb)
	{//�NBitmapMap�ରBytesMap	
		Set<Long> ks=hmhb.keySet();
		HashMap<Long,byte[]> tm=new HashMap<Long,byte[]>();
		
		for(Long l:ks)
		{
			Bitmap bm=hmhb.get(l);
			byte[] data=fromBitmapToBytes(bm);
			tm.put(l, data);
		}
		return tm;
	}
	
	public static HashMap<Long,Bitmap> fromBytesMapToBitmapMap(HashMap<Long,byte[]> hmbb)
	{//�NBytesMap�ରBitmapMap	
		Set<Long> ks=hmbb.keySet();
		HashMap<Long,Bitmap> hmhb=new HashMap<Long,Bitmap>();
		
		for(Long l:ks)
		{
			byte[] data=hmbb.get(l);
			Bitmap bm=fromBytesToBitmap(data);
			hmhb.put(l, bm);
		}
		
		return hmhb;
	}
	
	public static byte[] fromBitmapToBytes(Bitmap bm)
	{//�NBitmap�ରByte�}�C
		ByteArrayOutputStream baos = new ByteArrayOutputStream();     
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);     
		return baos.toByteArray();   
	}
	
	public static Bitmap fromBytesToBitmap(byte[] data)
	{//�NByte�}�C�ରBytesMap		
		if(data.length!=0)
		{   
		  Bitmap bm=BitmapFactory.decodeByteArray(data, 0, data.length);
		  if(bm==null)
		  {
			  throw new RuntimeException(data.length+"zi hua kong");
		  }
		  else
		  {
			  return bm;
		  }		  
		}   
		else 
		{   
			throw new RuntimeException("data.length==0----");
			//return null;   
		}   
	}
}
