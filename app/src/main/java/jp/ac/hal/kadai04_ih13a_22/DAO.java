package jp.ac.hal.kadai04_ih13a_22;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DAO {

	private SQLiteDatabase db;

	public DAO(SQLiteDatabase db){
		this.db=db;
	}

	public List<Hoge> select(){
		List<Hoge> list = new ArrayList<Hoge>();
		try{
			Cursor c = db.rawQuery("select * from aa",
			//new String[]{String.valueOf(id)}
			null);

	    	boolean flg = c.moveToFirst();
	    	while(flg){
				int id = c.getInt(c.getColumnIndex("_id"));
				String name = c.getString(c.getColumnIndex("name"));
				byte[] memo = c.getBlob(c.getColumnIndex("memo"));
				Hoge hoge = new Hoge();
				hoge.setId(id);
				hoge.setName(name);
				hoge.setMemo(memo);
				flg= c.moveToNext();
				list.add(hoge);
	    	}
	    	c.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return list;
	}
	public List<Hoge> select(String searchStr){
		List<Hoge> list = new ArrayList<Hoge>();
		String[] sStr = searchStr.split(" ",0);
		String str = "select * from aa where name like ? ";
		String where = " or name like ? ";
		for(int i =0;i<sStr.length-1;i++){
			str+=where;
		}
		try{
			Cursor c = db.rawQuery(str,sStr);

	    	boolean flg = c.moveToFirst();
	    	while(flg){
				int id = c.getInt(c.getColumnIndex("_id"));
				String name = c.getString(c.getColumnIndex("name"));

				byte[] memo = c.getBlob(c.getColumnIndex("memo"));
				Hoge hoge = new Hoge();
				hoge.setId(id);
				hoge.setName(name);
				hoge.setMemo(memo);
				flg= c.moveToNext();
				list.add(hoge);
	    	}
	    	c.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return list;
	}
	public Hoge  select(int id){
		Hoge hoge = new Hoge();
		try{
			Cursor c = db.rawQuery("select * from aa where _id = ? ",
					new String[]{String.valueOf(id)});
			boolean flg = c.moveToFirst();
			if(flg){
				id = c.getInt(c.getColumnIndex("_id"));
				String name = c.getString(c.getColumnIndex("name"));
				byte[] memo = c.getBlob(c.getColumnIndex("memo"));
				hoge.setId(id);
				hoge.setName(name);
				hoge.setMemo(memo);
			}
			c.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return hoge;
	}
	public String insert(String name,byte[] blob){
		String msg = "成功";
		try{
			ContentValues cv = new ContentValues();
			cv.put("name", name);
			cv.put("memo", blob);
			db.insert("aa", null, cv);

		}catch(SQLException e){
			e.printStackTrace();
			msg = "失敗";
		}
		return msg;
	}
	public String update(int id, String name , byte[] blob){
		String msg="成功";
		try{
			ContentValues cv = new ContentValues();
			cv.put("_id", id);
			cv.put("name", name);
			cv.put("memo",blob);

			db.update("aa", cv, "_id = ?", new String[]{String.valueOf(id)});

		}catch(SQLiteException e){
			e.printStackTrace();
			msg = "失敗";
		}
		return msg;
	}
	public String  delete(int id){
		String msg="成功";
		try{
			db.execSQL("delete from aa where _id = ?"
					,new String[]{String.valueOf(id)});

		}catch(SQLiteException e){
			e.printStackTrace();
			msg = "失敗";
		}
		return msg;
	}


}
