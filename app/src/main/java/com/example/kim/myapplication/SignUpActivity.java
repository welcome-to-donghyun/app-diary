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

public class SignUpActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navi;
    private String[] leftSliderData1={"Login", "Sign Up"};

    private EditText idet, emailet, pwet,pwchet;
    private Button signupbt;
    private LinearLayout ll;

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
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
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
                break;
            case 1:
                drawerLayout.closeDrawers();
                Intent intent = new Intent(this, SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}
