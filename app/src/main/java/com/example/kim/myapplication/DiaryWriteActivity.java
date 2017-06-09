package com.example.kim.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DiaryWriteActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private static final int SELECT_PICTURE = 1;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;
    private Uri mImageCaptureUri;
    private String absoultePath;

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

        imageView.getLayoutParams().width = 0;
        imageView.getLayoutParams().height = 0;

        if(editdno != -1){      //수정하러 온거다.
            dno = editdno;
            diary = dao.getDiarydno(uno,dno);
            titleet.setText(diary.getDtitle());
            contentet.setText(diary.getDcontent());
            img = diary.getDimg();
            if(img!=null) {
                imageView.setImageBitmap(byteArrayToBitmap(diary.getDimg()));
                imageView.getLayoutParams().width = 600;
                imageView.getLayoutParams().height = 600;
            }
            else{
                imageView.getLayoutParams().width = 0;
                imageView.getLayoutParams().height = 0;
            }
        }

        imageView.setMaxHeight(0);
        imageView.setMaxWidth(0);
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
        doTakeAlbumAction();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK)
            return;

        switch(requestCode)
        {
            case PICK_FROM_ALBUM:
            {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.
                mImageCaptureUri = data.getData();
            }

            case PICK_FROM_CAMERA:
            {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                // CROP할 이미지를 200*200 크기로 저장
                intent.putExtra("outputX", 500); // CROP한 이미지의 x축 크기
                intent.putExtra("outputY", 500); // CROP한 이미지의 y축 크기
                intent.putExtra("aspectX", 5); // CROP 박스의 X축 비율
                intent.putExtra("aspectY", 5); // CROP 박스의 Y축 비율
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_iMAGE); // CROP_FROM_CAMERA case문 이동
                break;
            }
            case CROP_FROM_iMAGE:
            {
                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                if(resultCode != RESULT_OK) {
                    return;
                }

                final Bundle extras = data.getExtras();

                // CROP된 이미지를 저장하기 위한 FILE 경로
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+
                        "/SmartWheel/"+System.currentTimeMillis()+".jpg";

                if(extras != null)
                {
                    Bitmap photo = extras.getParcelable("data"); // CROP된 BITMAP
                    img = bitmapToByteArray(photo);

                    imageView.getLayoutParams().width = 600;
                    imageView.getLayoutParams().height = 600;
                    imageView.setImageBitmap(photo); // 레이아웃의 이미지칸에 CROP된 BITMAP을 보여줌

                }
                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists())
                {
                    f.delete();
                }
            }
        }

    }


    /**
     * 앨범에서 이미지 가져오기
     */
    public void doTakeAlbumAction() // 앨범에서 이미지 가져오기
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    public byte[] bitmapToByteArray( Bitmap bitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream) ;
        byte[] byteArray = stream.toByteArray() ;
        return byteArray ;
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

    public Bitmap byteArrayToBitmap( byte[] $byteArray ) {
        Bitmap bitmap = BitmapFactory.decodeByteArray( $byteArray, 0, $byteArray.length ) ;
        return bitmap ;
    }
}
