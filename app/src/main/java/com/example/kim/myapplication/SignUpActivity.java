package com.example.kim.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navi;
    private String[] leftSliderData1={"로그인", "회원가입"};

    private EditText idet, emailet, pwet,pwchet;
    private Button signupbt;
    private LinearLayout ll;
    private Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        nitView();
        if(toolbar!=null){
            toolbar.setTitle("회원가입");
            setSupportActionBar(toolbar);
        }
        initDrawer();

        dao = new Dao(getApplicationContext());


        ll=(LinearLayout)findViewById(R.id.linearLaoyout);
        idet = (EditText) findViewById(R.id.idEditText);
        emailet = (EditText) findViewById(R.id.emailEditText);
        pwet = (EditText) findViewById(R.id.pwEditText);
        pwchet = (EditText) findViewById(R.id.pwchEditText);
        signupbt = (Button) findViewById(R.id.signupButton);
        idet.setPrivateImeOptions("defaultInputmode=english;");
        pwet.setPrivateImeOptions("defaultInputmode=english;");
        pwchet.setPrivateImeOptions("defaultInputmode=english;");


        ll.setOnClickListener(this);
        signupbt.setOnClickListener(this);
    }

    private void nitView(){
        leftDrawerList=(ListView)findViewById(R.id.left_drawer);
        toolbar=(Toolbar)findViewById(R.id.toolBar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        navi=new ArrayAdapter<String>(SignUpActivity.this, android.R.layout.simple_list_item_1, leftSliderData1);
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
            case R.id.signupButton:
                signupButton();
                break;
            case R.id.linearLaoyout :
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(emailet.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(idet.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(pwet.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(pwchet.getWindowToken(), 0);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                drawerLayout.closeDrawers();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            case 1:
                drawerLayout.closeDrawers();
                break;
        }
    }

    private void signupButton(){
        String id,pw,pwch,email;

        if(idet.getText().toString().equals("") || pwet.getText().toString().equals("") || emailet.getText().toString().equals("") || pwchet.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),"빈칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!(pwet.getText().toString().equals(pwchet.getText().toString()))){
            Toast.makeText(getApplicationContext(),"비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        id =idet.getText().toString();
        pw =pwet.getText().toString();
        email=emailet.getText().toString();

        if(!(isEmailValid(email))){
            Toast.makeText(getApplicationContext(),"이메일을 바로 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(dao.sigupCheck(id,pw,email)){
            Toast.makeText(getApplicationContext(),"이미 있는 아이디 입니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Toast.makeText(getApplicationContext(),"회원가입이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
