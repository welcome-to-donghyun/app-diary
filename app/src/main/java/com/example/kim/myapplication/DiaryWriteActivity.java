package com.example.kim.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DiaryWriteActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private static final int SELECT_PICTURE = 1;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navi;
    private String[] leftSliderData1={"홈 화면", "로그아웃"};
    private LinearLayout ll;
    private EditText titleet, contentet;
    private Button filebt, writebt;
    private String date;
    private byte[] img;
    private Dao dao;
    private int uno, editdno, dno;
    private TextView textView;
    private String selectedImagePath;
    private ImageView imageView;
    private DtoDiary diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        nitView();
        if(toolbar!=null){
            toolbar.setTitle("다이어리 추가");
            setSupportActionBar(toolbar);
        }
        initDrawer();

        img=null;
        dao = new Dao(getApplicationContext());
        uno = getIntent().getExtras().getInt("uno");
        editdno = getIntent().getExtras().getInt("editdno");
        date=getIntent().getExtras().getString("date");

        textView = (TextView)findViewById(R.id.idTextView);
        textView.setText(dao.getUserId(uno));

        imageView =(ImageView)findViewById(R.id.imageView);
        titleet = (EditText)findViewById(R.id.titleEditText);
        contentet = (EditText)findViewById(R.id.contentEditText);
        filebt = (Button)findViewById(R.id.fileaddButton);
        writebt = (Button)findViewById(R.id.writeButton);
        ll=(LinearLayout)findViewById(R.id.linearLaoyout);

        if(editdno != -1){
            dno = editdno;
            diary = dao.getDiarydno(uno,dno);
            titleet.setText(diary.getDtitle());
            contentet.setText(diary.getDcontent());
            img = diary.getDimg();
            //img 파일도 추가
        }

        writebt.setOnClickListener(this);
        filebt.setOnClickListener(this);
        ll.setOnClickListener(this);
    }

    private void nitView(){
        leftDrawerList=(ListView)findViewById(R.id.left_drawer);
        toolbar=(Toolbar)findViewById(R.id.toolBar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
            navi=new ArrayAdapter<String>(DiaryWriteActivity.this, android.R.layout.simple_list_item_1, leftSliderData1);
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
        switch (v.getId()) {
            case R.id.linearLaoyout :
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(titleet.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(contentet.getWindowToken(), 0);
                break;
            case R.id.writeButton:
                writeButton();
                break;
            case R.id.fileaddButton:
                fileaddButton();
                break;
        }
    }

    private void writeButton(){
        String title,content;

        if(titleet.getText().toString().equals("") || contentet.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "제목과 내용을 채워주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        title = titleet.getText().toString();
        content = contentet.getText().toString();

        if(editdno ==-1) {
            dao.insertdiary(uno, title,content,img,date);
            Toast.makeText(getApplicationContext(), "다이어리가 추가 되었습니다", Toast.LENGTH_LONG).show();
        }
        else{
            dao.updatediary(dno, title, content,img);
            Toast.makeText(getApplicationContext(), "다이어리가 수정 되었습니다", Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("uno", uno);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void fileaddButton(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

}

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                Log.i("테스트","에러1");
                try {
                    imageView.setImageURI(data.getData());
                    FileInputStream fis = new FileInputStream(selectedImagePath);

                    img = new byte[fis.available()];

                    fis.read(img);
                    Log.i("테스트","에러3");
                }catch (Exception e){
                    Log.i("테스트",""+selectedImagePath);
                    e.printStackTrace();
                }
            }
        }
    }

    public String getPath(Uri uri) {
        // uri가 null일경우 null반환
        if( uri == null ) {
            return null;
        }
        // 미디어스토어에서 유저가 선택한 사진의 URI를 받아온다.
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // URI경로를 반환한다.
        return uri.getPath();
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
