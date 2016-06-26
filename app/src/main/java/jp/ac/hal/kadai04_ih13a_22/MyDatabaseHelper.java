package jp.ac.hal.kadai04_ih13a_22;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	static final String DB_NAME = "Kadai04";

	public MyDatabaseHelper(Context context) {
		super(context, DB_NAME,null,1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try{
			String sql = "create table aa(_id integer primary key autoincrement,"
					+ "name text NOT NULL,memo blob)";
			db.execSQL(sql);

		}catch(SQLException e){
			e.printStackTrace();

		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		try{
			String sql = "drop table aa";
			db.execSQL(sql);
		}catch(SQLException e){
			e.printStackTrace();

		}
		onCreate(db);

	}

}
