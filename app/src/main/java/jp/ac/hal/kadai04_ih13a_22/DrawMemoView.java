package jp.ac.hal.kadai04_ih13a_22;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class DrawMemoView extends View{

	 private float oldx = 0f;
	 private float oldy = 0f;
	 private Bitmap bmp = null;
	 private Paint paint;
	 private Canvas canvas;
	public DrawMemoView(Context context,AttributeSet attrs) {
		super(context,attrs);
		 paint = new Paint();

		 paint.setAntiAlias(true);
		 paint.setStyle(Paint.Style.STROKE);
		 paint.setStrokeCap(Paint.Cap.ROUND);
		 paint.setStrokeJoin(Paint.Join.ROUND);


	}
    public void chandeColor(int a,int r, int g, int b,float w){
        paint.setARGB(a,r,g,b);

        paint.setStrokeWidth(w);

    }
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		 super.onSizeChanged(w,h,oldw,oldh);
		 bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		 canvas = new Canvas(bmp);

		 canvas.drawColor(Color.WHITE);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(bmp,0,0,null);
	}
	@Override
	public boolean onTouchEvent(MotionEvent e){
		 switch(e.getAction()){
		 case MotionEvent.ACTION_DOWN:
		 oldx = e.getX();
		 oldy = e.getY();
		 break;
		 case MotionEvent.ACTION_MOVE:
		 canvas.drawLine(oldx, oldy, e.getX(), e.getY(), paint);
		 oldx = e.getX();
		 oldy = e.getY();
		 invalidate();
		 break;
		 default:
		 break;
		 }
		 return true;
	}

	public void save(String name) throws IOException{

		  ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
          boolean success = bmp.compress(CompressFormat.PNG, 100, byteStream);
          if(success){
              ByteArrayOutputStream byteos = new ByteArrayOutputStream();
              bmp.compress(CompressFormat.JPEG, 100, byteos);
              byte[] blob = byteos.toByteArray();
              MyDatabaseHelper mh = new MyDatabaseHelper(getContext());
              SQLiteDatabase db = mh.getWritableDatabase();
              DAO dao = new DAO(db);
              dao.insert(name,blob);
              Toast.makeText(getContext(),"保存しました",Toast.LENGTH_SHORT).show();
              clear();
          }
    }
	public void clear(){
		 canvas.drawColor(Color.WHITE);
		 invalidate();
	}
    public void updateDraw(Bitmap bitmap){
        canvas.drawBitmap(bitmap,0,0,null);
    }
    public void update(int id,String name){

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        boolean success = bmp.compress(CompressFormat.PNG, 100, byteStream);
        if(success){
            ByteArrayOutputStream byteos = new ByteArrayOutputStream();
            bmp.compress(CompressFormat.JPEG, 100, byteos);
            byte[] blob = byteos.toByteArray();
            MyDatabaseHelper mh = new MyDatabaseHelper(getContext());
            SQLiteDatabase db = mh.getWritableDatabase();
            DAO dao = new DAO(db);
            dao.update(id,name,blob);
            Toast.makeText(getContext(),"上書き保存しました",Toast.LENGTH_SHORT).show();
            clear();
        }
    }

}