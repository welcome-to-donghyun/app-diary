package com.example.kim.myapplication;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MemoWriteActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{


    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navi;
    private String[] leftSliderData1={"홈 화면", "로그아웃"};
    private LinearLayout ll;
    private EditText titleet, contentet;
    private Button writebt;
    private Dao dao;
    private int uno, editmno, mno;
    private TextView textView;
    private DtoMemo memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_write);

        nitView();
        if(toolbar!=null){
            toolbar.setTitle("메모 추가");
            setSupportActionBar(toolbar);
        }
        initDrawer();
        dao = new Dao(getApplicationContext());
        uno = getIntent().getExtras().getInt("uno");
        editmno = getIntent().getExtras().getInt("editmno");

        textView = (TextView)findViewById(R.id.idTextView);
        textView.setText(dao.getUserId(uno));

        titleet = (EditText)findViewById(R.id.titleEditText);
        contentet = (EditText)findViewById(R.id.contentEditText);
        writebt = (Button)findViewById(R.id.writeButton);
        ll=(LinearLayout)findViewById(R.id.linearLaoyout);

        if(editmno != -1) {
            mno = editmno;
            memo = dao.getMemomno(uno,mno);
            titleet.setText(memo.getMtitle());
            contentet.setText(memo.getMcontent());
        }
        writebt.setOnClickListener(this);
        ll.setOnClickListener(this);
    }

    private void nitView(){
        leftDrawerList=(ListView)findViewById(R.id.left_drawer);
        toolbar=(Toolbar)findViewById(R.id.toolBar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        navi=new ArrayAdapter<String>(MemoWriteActivity.this, android.R.layout.simple_list_item_1, leftSliderData1);
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

        if(editmno ==-1) {
            dao.insertmemo(uno, title, content);
            Toast.makeText(getApplicationContext(), "메모가 추가 되었습니다", Toast.LENGTH_LONG).show();
        }
        else{
            dao.updatememo(mno, title, content);
            Toast.makeText(getApplicationContext(), "메모가 수정 되었습니다", Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("uno", uno);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        switch (position) {
            case 0:
                drawerLayout.closeDrawers();
                intent = new Intent(this, CalendarActivity.class);
                intent.putExtra("uno", uno);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
