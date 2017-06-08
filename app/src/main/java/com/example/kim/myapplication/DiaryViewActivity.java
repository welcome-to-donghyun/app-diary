package com.example.kim.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DiaryViewActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{


    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navi;
    private String[] leftSliderData1={"홈 화면", "로그아웃"};
    private Button editbt, deletebt;

    private ImageView imageView;

    private Dao dao;
    private int dno, uno;
    private TextView textView;
    private TextView titleText, contentText;
    private DtoDiary diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_view);

        nitView();
        if(toolbar!=null){
            toolbar.setTitle("다이어리");
            setSupportActionBar(toolbar);
        }
        initDrawer();

        dao = new Dao(getApplicationContext());
        uno = getIntent().getExtras().getInt("uno");
        dno = getIntent().getExtras().getInt("dno");

        diary = dao.getDiarydno(uno,dno);

        textView = (TextView)findViewById(R.id.idTextView);
        textView.setText(dao.getUserId(uno));

        titleText = (TextView)findViewById(R.id.titleTextView);
        contentText = (TextView)findViewById(R.id.contentTextView);
        titleText.setText(diary.getDtitle());
        contentText.setText(diary.getDcontent());

        imageView = (ImageView)findViewById(R.id.imageView);
        //imageView.setImageResource(R.drawable.test);
        //set imgview 함수 사용



        editbt=(Button)findViewById(R.id.editButton);
        deletebt=(Button)findViewById(R.id.deleteButton);

        editbt.setOnClickListener(this);
        deletebt.setOnClickListener(this);
    }

    private void nitView(){
        leftDrawerList=(ListView)findViewById(R.id.left_drawer);
        toolbar=(Toolbar)findViewById(R.id.toolBar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
            navi=new ArrayAdapter<String>(DiaryViewActivity.this, android.R.layout.simple_list_item_1, leftSliderData1);
        leftDrawerList.setAdapter(navi);
        leftDrawerList.setOnItemClickListener(this);
    }

    private void initDrawer(){
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }


    @Override
    protected void onPostCreate(Bundle saveInstanceState){
        super.onPostCreate(saveInstanceState);
        drawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.editButton :
                intent = new Intent(this, DiaryWriteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("editdno", dno);
                intent.putExtra("uno", uno);
                intent.putExtra("date", diary.getDdate());
                startActivity(intent);
                break;
            case R.id.deleteButton :
                intent = new Intent(this, CalendarActivity.class);
                dao.deletediary(dno);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("uno", uno);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        switch (position) {
            case 0:
                drawerLayout.closeDrawers();
                intent = new Intent(this, CalendarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("uno", uno);
                startActivity(intent);
                finish();
                break;
            case 1:
                drawerLayout.closeDrawers();
                intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
    }
}
