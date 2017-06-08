package com.example.kim.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kim.myapplication.decorator.EventDecorator;
import com.example.kim.myapplication.decorator.MySelectorDecorator;
import com.example.kim.myapplication.decorator.OneDayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import static com.example.kim.myapplication.R.id.calendarView;

public class CalendarActivity extends AppCompatActivity implements OnDateSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener{
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    private String[] leftSliderData1={"홈 화면", "로그아웃"};

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList, memoList;
    private ArrayAdapter<String> navi,memo;
    private ArrayList<DtoDiary> diarydata;
    private int diarylength;
    private String [] diaryListData;
    private DtoDiary diary;

    private ArrayList<DtoMemo> memodata;
    private Button writebutton;
    private MaterialCalendarView calendar;
    private int uno;
    private TextView textView;
    private Dao dao;
    private String ddate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        nitView();
        if(toolbar!=null){
            toolbar.setTitle("앱 다이어리");
            setSupportActionBar(toolbar);
        }
        initDrawer();

        dao = new Dao(getApplicationContext());

        uno = getIntent().getExtras().getInt("uno");
        textView = (TextView)findViewById(R.id.idTextView);
        textView.setText(dao.getUserId(uno));

        //하단에 메모 리스트를 만들기 위한 코드
        memodata = dao.getMemo(uno);
        int length = memodata.size();
        String [] memoListData = new String[length];
        for(int i=0;i<memodata.size();i++)
            memoListData[i] = memodata.get(i).getMtitle();
        //달력 표시를 위한 사전 준비 코드
        diarydata = dao.getDiary(uno);
        diarylength = diarydata.size();
        diaryListData = new String[diarylength];
        for(int i=0;i<diarydata.size();i++) {
            diaryListData[i] = diarydata.get(i).getDdate();
            Log.i("달력표시를 위한 사전 준비 코드 : " , diaryListData[i]);
        }
        diary=null;
        // 달력 생성
        calendarFunction();

        writebutton = (Button)findViewById(R.id.memobutton);
        writebutton.setOnClickListener(this);
        memoList=(ListView)findViewById(R.id.memoList);
        memo=new ArrayAdapter<String>(CalendarActivity.this, android.R.layout.simple_list_item_1, memoListData);
        memoList.setAdapter(memo);
        setListViewHeightBasedOnChildren(memoList);
        memoList.setOnItemClickListener(this);
    }

    private void nitView(){
        leftDrawerList=(ListView)findViewById(R.id.left_drawer);
        toolbar=(Toolbar)findViewById(R.id.toolBar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        navi=new ArrayAdapter<String>(CalendarActivity.this, android.R.layout.simple_list_item_1, leftSliderData1);
        leftDrawerList.setAdapter(navi);
        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        drawerLayout.closeDrawers();
                        break;
                    case 1:
                        drawerLayout.closeDrawers();
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                }
            }
        });
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
            case R.id.memobutton:
                Intent intent = new Intent(this, MemoWriteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("editmno", -1);
                intent.putExtra("uno", uno);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        intent = new Intent(this, MemoViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("uno", uno);
        intent.putExtra("mno", memodata.get(position).getMno());
        startActivity(intent);
    }

    public  void calendarFunction(){

        calendar = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendar.setOnDateChangedListener(this);
        calendar.setLeftArrowMask(getResources().getDrawable(R.drawable.ic_navigation_arrow_back));
        calendar.setRightArrowMask(getResources().getDrawable(R.drawable.ic_navigation_arrow_forward));

        Calendar instance = Calendar.getInstance();
        calendar.setSelectedDate(instance.getTime());
        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);
        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        calendar.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .commit();

        calendar.addDecorators(
                new MySelectorDecorator(this),
                oneDayDecorator
        );
        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    //메모 리스트 뷰의 높이 지정
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();

        if(date.getMonth()>9) {
            if(date.getDay()>9)
                ddate = "" + date.getYear() + "" + (date.getMonth()+1) + "" + date.getDay();
            else
                ddate = "" + date.getYear() + "" + (date.getMonth()+1) + "0" + date.getDay();
        }
        else {
            if(date.getDay()>9)
                ddate = "" + date.getYear() + "0" + (date.getMonth()+1) + "" + date.getDay();
            else
                ddate = "" + date.getYear() + "0" + (date.getMonth()+1) + "0" + date.getDay();
        }
        Toast.makeText(getApplicationContext(),  ddate, Toast.LENGTH_LONG).show();

        if(checkDiary()){       //다이어리가 있을 때
            Intent intent = new Intent(getApplicationContext(), DiaryViewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("uno", uno);
            intent.putExtra("dno", diary.getDno());
            startActivity(intent);
        }
        else{   //다이어리가 없을 때
            Intent intent = new Intent(getApplicationContext(), DiaryWriteActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("uno", uno);
            intent.putExtra("editdno", -1);
            intent.putExtra("date", ""+ddate);
            startActivity(intent);
        }
    }

    private boolean checkDiary(){
        diary = dao.getDiaryddate(uno,ddate);
        if(diary!=null)
            return true;
        else
            return false;
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {
        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int year,month,date;

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            for (int i = 0; i < diaryListData.length; i++) {
                calendar.set(Integer.parseInt(diaryListData[i].substring(0, 4)), Integer.parseInt(diaryListData[i].substring(4, 6)) - 1, Integer.parseInt(diaryListData[i].substring(6, 8)));
            }
            for (int i = 0; i < diaryListData.length; i++) {
                year = Integer.parseInt(diaryListData[i].substring(0, 4));
                month = Integer.parseInt(diaryListData[i].substring(4, 6)) - 1;
                date = Integer.parseInt(diaryListData[i].substring(6, 8)) ;
                calendar.set(year,month,date);
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            calendar.addDecorator(new EventDecorator(Color.RED, calendarDays));
        }
    }
}
