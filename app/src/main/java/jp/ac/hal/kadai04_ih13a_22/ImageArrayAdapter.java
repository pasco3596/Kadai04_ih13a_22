package jp.ac.hal.kadai04_ih13a_22;

import android.app.LauncherActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pasuco on 2016/06/24.
 */
public class ImageArrayAdapter extends ArrayAdapter<Hoge> {
    private int resourceId;
    private List<Hoge> hoge;
    private LayoutInflater inflater;

    public ImageArrayAdapter(Context context, int resourceId, List<Hoge> hoge) {
        super(context, resourceId, hoge);

        this.resourceId = resourceId;
        this.hoge = hoge;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = this.inflater.inflate(this.resourceId, null);
        }

        Hoge hoge = this.hoge.get(position);

        // テキストをセット
        TextView appInfoText = (TextView)view.findViewById(R.id.item_text);
        appInfoText.setText("件名："+hoge.getName());

        BitmapFactory.Options options = new  BitmapFactory.Options();
        options.inMutable = true;
        byte[] byteArray = hoge.getMemo();

        //byte[] からビットマップを取得する
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length,options);

        // アイコンをセット
        ImageView appInfoImage = (ImageView)view.findViewById(R.id.item_image);
        appInfoImage.setImageBitmap(bitmap);

        return view;
    }
}
