package jp.ac.hal.kadai04_ih13a_22;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE = 1;
    DrawMemoView dmv;
    AlertDialog.Builder saveBuilder;
    AlertDialog.Builder clearBuilder;
    AlertDialog.Builder changeColorBuilder;
    AlertDialog saveDialog;
    AlertDialog clearDialog;
    AlertDialog changeColorDialog;
    SeekBar alphaSb;
    SeekBar redSb;
    SeekBar greenSb;
    SeekBar blueSb;
    SeekBar widthSb;
    TextView alphatext;
    TextView redtext;
    TextView greentext;
    TextView bluetext;
    TextView widthtext;
    TextView colorArea;
    int id;
    int alpha =255;
    int red=0;
    int green=0;
    int blue =255;
    float w =6;
    int ww = 0;
    boolean flg =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dmv  = (DrawMemoView) findViewById(R.id.View);
        dmv.chandeColor(alpha,red,green,blue,w);

        MyDatabaseHelper mh = new MyDatabaseHelper(this);
        SQLiteDatabase db = mh.getWritableDatabase();
        db.close();

        createDialog();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.sava) {

            saveDialog.show();

        } else if (id == R.id.clear) {
            clearDialog.show();

        } else if (id == R.id.all) {
            Intent intent = new Intent(this,ListActivity.class);

            startActivityForResult(intent,REQUEST_CODE);

        } else if (id == R.id.nav_manage) {
            changeColorDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createDialog(){
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View saveLayout = inflater.inflate(R.layout.save,(ViewGroup)findViewById(R.id.layout_root));
        final View clearLayout = inflater.inflate(R.layout.clear,(ViewGroup)findViewById(R.id.layout_root));
        final View changeColorLayout = inflater.inflate(R.layout.color_change_layout,(ViewGroup)findViewById(R.id.layout_root));

        saveBuilder = new AlertDialog.Builder(this);
        saveBuilder.setTitle("保存しますか？");
        saveBuilder.setView(saveLayout);
        saveBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // OK ボタンクリック処理
                EditText name
                        = (EditText)saveLayout.findViewById(R.id.name_area2);
                name.setMaxLines(1);
                String nameStr = name.getText().toString();

                if(nameStr.equals("")){
                    nameStr="無題";
                }

                if(flg) {
                    dmv.update(id, nameStr);
                    flg = false;
                }else{
                    try {
                        dmv.save(nameStr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                name.getText().clear();
            }
        });
        saveBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Cancel ボタンクリック処理
            }
        });
        clearBuilder = new AlertDialog.Builder(this);
        clearBuilder.setTitle("白紙にしますか？");
        clearBuilder.setView(clearLayout);
        clearBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dmv.clear();
                flg = false;
            }
        });
        clearBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Cancel ボタンクリック処理
            }
        });

        changeColorBuilder = new AlertDialog.Builder(this);
        changeColorBuilder.setTitle("色変更");
        changeColorBuilder.setView(changeColorLayout);

        colorArea= (TextView)changeColorLayout.findViewById(R.id.color_area);
        colorArea.setBackgroundColor(Color.argb(alpha,red,green,blue));
        changeColorBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dmv.chandeColor(alpha,red,green,blue,w);
            }
        });
        changeColorBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Cancel ボタンクリック処理
            }
        });
        saveDialog = saveBuilder.create();
        clearDialog= clearBuilder.create();


        alphaSb = (SeekBar)changeColorLayout.findViewById(R.id.alphaBar);
        alphatext=(TextView)changeColorLayout.findViewById(R.id.alphatext);
        alphatext.setText(String.valueOf(alpha));
        alphaSb.setProgress(alpha);
        alphaSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                alpha =  alphaSb.getProgress();
                alphatext.setText(String.valueOf(alpha));
                colorArea.setBackgroundColor(Color.argb(alpha,red,green,blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        redSb = (SeekBar)changeColorLayout.findViewById(R.id.redBar);
        redtext=(TextView)changeColorLayout.findViewById(R.id.redtext);
        redtext.setText(String.valueOf(red));
        redSb.setProgress(red);
        redSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red =  redSb.getProgress();
                redtext.setText(String.valueOf(red));
                colorArea.setBackgroundColor(Color.argb(alpha,red,green,blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        greenSb = (SeekBar)changeColorLayout.findViewById(R.id.greenBar);
        greentext=(TextView)changeColorLayout.findViewById(R.id.greentext);
        greentext.setText(String.valueOf(green));
        greenSb.setProgress(green);
        greenSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                green = greenSb.getProgress();
                greentext.setText(String.valueOf(green));
                colorArea.setBackgroundColor(Color.argb(alpha,red,green,blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        blueSb = (SeekBar)changeColorLayout.findViewById(R.id.blueBar);
        bluetext=(TextView)changeColorLayout.findViewById(R.id.bluetext);
        bluetext.setText(String.valueOf(blue));
        blueSb.setProgress(blue);
        blueSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blue = blueSb.getProgress();
                bluetext.setText(String.valueOf(blue));
                colorArea.setBackgroundColor(Color.argb(alpha,red,green,blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        widthSb = (SeekBar)changeColorLayout.findViewById(R.id.widthBar);
        widthtext=(TextView)changeColorLayout.findViewById(R.id.widthtext);

        ww = (int)w;
        widthtext.setText(String.valueOf(ww));
        widthSb.setProgress(ww);
        widthSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ww =  widthSb.getProgress();
                widthtext.setText(String.valueOf(ww));
                w = (float)ww;
                colorArea.setBackgroundColor(Color.argb(alpha,red,green,blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        changeColorDialog = changeColorBuilder.create();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {

            case (REQUEST_CODE):
                if (resultCode == RESULT_OK) {
                    Hoge hoge = (Hoge)data.getSerializableExtra("hoge");
                    hoge.getId();
                    BitmapFactory.Options options = new  BitmapFactory.Options();
                    options.inMutable = true;
                    byte[] byteArray =hoge.getMemo();
                    //byte[] からビットマップを取得する
                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length,options);
                    dmv.updateDraw(bitmap);
                    flg =true;
                    id=hoge.getId();


                } else if (resultCode == RESULT_CANCELED) {

                } else {

                }
                break;
            default:
                break;
        }

    }
}
