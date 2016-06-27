package jp.ac.hal.kadai04_ih13a_22;

import android.os.Handler;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    ListView lv;
    ImageArrayAdapter adapter;
    List<Hoge> list;
    Hoge hoge;
    Button searchBt;
    EditText searchArea;
    MyDatabaseHelper mh;
    private AlertDialog imageListDialog;
    AlertDialog.Builder imageListBuilder;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
         mh = new MyDatabaseHelper(this);
        SQLiteDatabase db = mh.getWritableDatabase();

        searchArea = (EditText)findViewById(R.id.search_area);
        searchArea.setMaxLines(1);
        searchBt=(Button)findViewById(R.id.searchBt);

        lv = (ListView)findViewById(R.id.listView);
        listview();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View view,int position, long id){
                ListView listview = (ListView)parent;
                hoge = (Hoge)listview.getItemAtPosition(position);
                imageListDialog.show();

            }
        });
        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview();
            }
        });
        createdialog();

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

        } else if (id == R.id.clear) {

        } else if (id == R.id.all) {

        } else if (id == R.id.nav_manage) {

  //      }else if (id == R.id.nav_share) {

       // }else if (id == R.id.nav_send) {

        }else if(id == R.id.top){
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void listview(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        String searchStr = searchArea.getText().toString();
                        SQLiteDatabase db = mh.getWritableDatabase();
                        DAO dao = new DAO(db);

                        if(searchStr.equals("")){
                            list = dao.select();
                        }else {

                            list = dao.select(searchStr);

                        }
                        adapter = new ImageArrayAdapter(ListActivity.this, R.layout.imagelist, list);
                        lv.setAdapter(adapter);
                        db.close();

                    }
                });

            }
        }).start();


    }
    public void createdialog(){
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(
                LAYOUT_INFLATER_SERVICE);
        final View imageListLayout = inflater.inflate(R.layout.clear,(ViewGroup)findViewById(R.id.layout_root));

        imageListBuilder = new AlertDialog.Builder(this);
        imageListBuilder.setTitle("メニュー");
        imageListBuilder.setView(imageListLayout);
        String[] items = {"編集","削除"};
        imageListBuilder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if(0==which){
                    //編集
                    Intent intent = new Intent(ListActivity.this,MainActivity.class);
                    intent.putExtra("hoge",hoge.getId());
                    setResult(RESULT_OK,intent);
                    finish();

                }else if(1==which){
                    //削除
                    SQLiteDatabase db = mh.getWritableDatabase();
                    DAO dao = new DAO(db);
                    String msg = dao.delete(hoge.getId());

                    Toast.makeText(ListActivity.this,msg,Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    finish();
                }


            }
        });

        imageListDialog = imageListBuilder.create();
    }

}
