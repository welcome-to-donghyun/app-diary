package com.example.kim.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Dao {
    private Context context;
    private SQLiteDatabase database;

    public Dao(Context context) {
        this.context = context;
        String sql;
        //SQLite 초기화
        database = context.openOrCreateDatabase("LocalDATA.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        try {
            //sql = "DROP TABLE IF EXISTS Diary";
            //database.execSQL(sql);
            sql = "CREATE TABLE IF NOT EXISTS User("
                    + "uno integer primary key autoincrement,"
                    + "uid text not null,"
                    + "upw text not null,"
                    + "uemail text not null);";
            database.execSQL(sql);
            sql = "CREATE TABLE IF NOT EXISTS Memo("
                    + "mno integer primary key autoincrement,"
                    + "uno integer not null,"
                    + "mtitle text not null,"
                    + "mcontent text not null,"
                    + "FOREIGN KEY (uno) REFERENCES User(uno));";
            database.execSQL(sql);
            sql = "CREATE TABLE IF NOT EXISTS Diary("
                    + "dno integer primary key autoincrement,"
                    + "uno integer not null,"
                    + "dtitle text not null,"
                    + "dcontent text not null,"
                    + "dimg blob,"
                    + "ddate text not null,"
                    + "FOREIGN KEY (uno) REFERENCES User(uno));";
            database.execSQL(sql);
        } catch (Exception e) {
            Log.e("DB error", "CREATE TABLE FAILED - " + e);
            e.printStackTrace();
        }
    }

    public void insertJsonData(String jsonData) {
        String uid, upw, uemail;
        String mtitle, mcontent;
        String dtitle, dcontent, dimg;
        String sql;

        try {

            JSONObject jObj = new JSONObject(jsonData);

            JSONArray users = jObj.getJSONArray("user");
            // JSONArray memos = jObj.getJSONArray("memo");
            //  JSONArray diaries = jObj.getJSONArray("diary");

            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);

                uid = user.getString("uid");
                upw = user.getString("upw");
                uemail = user.getString("uemail");

                Log.i("TEST", "id : " + uid + "  upw : " + upw);

                sql = "INSERT INTO User(uid, upw, uemail)"
                        + " VALUES('" + uid + "', '" + upw + "', '" + uemail + "');";

                try {
                    database.execSQL(sql);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            Log.e("insert Error", "JSON : " + e);
            e.printStackTrace();
        }

    }



    public String getUserId(int uno) {
        String sql = "SELECT uid FROM User WHERE uno =" + uno + ";";
        Cursor cursor = database.rawQuery(sql, null);
        String id = null;

        while(cursor.moveToNext())
            id = cursor.getString(0);

        cursor.close();
        return id;
    }

    public boolean loginCheck(String id, String pw) {
        String sql = "SELECT uno FROM User WHERE uid ='" + id + "' AND upw = '" + pw + "';";
        Cursor cursor = database.rawQuery(sql, null);
        int uno=0;
        while(cursor.moveToNext())
            uno = cursor.getInt(0);

        cursor.close();
        if(uno==0)
            return true;
        else
            return false;
    }

    public int getuno(String id){
        String sql = "SELECT uno FROM User WHERE uid ='" + id + "';";
        Cursor cursor = database.rawQuery(sql, null);
        int uno=0;
        while(cursor.moveToNext())
            uno = cursor.getInt(0);
        cursor.close();
        return uno;
    }

    public boolean sigupCheck(String id, String pw, String email){
        String sql = "SELECT uno FROM User WHERE uid ='" + id + "';";
        Cursor cursor = database.rawQuery(sql, null);
        int uno=0;
        while(cursor.moveToNext())
            uno = cursor.getInt(0);
        cursor.close();
        if(uno==0) {
            sql = "INSERT INTO User(uid, upw, uemail)"
                    + " VALUES('" + id + "', '" + pw + "', '" + email + "');";
            try {
                database.execSQL(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        else
            return true;
    }


    public ArrayList<DtoMemo> getMemo(int uno) {
        ArrayList<DtoMemo> memo = new ArrayList<DtoMemo>();

        int mno;
        String mtitle;
        String mcontent;

        String sql = "SELECT * FROM Memo WHERE uno = "+uno+";";
        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            mno = cursor.getInt(0);
            mtitle = cursor.getString(2);
            mcontent = cursor.getString(3);
            memo.add(new DtoMemo(mno, uno, mtitle, mcontent));
        }
        cursor.close();
        return memo;
    }

    public DtoMemo getMemomno(int uno, int mno) {
        DtoMemo memo = null;

        String mtitle;
        String mcontent;

        String sql = "SELECT * FROM Memo WHERE uno = "+uno+" AND mno ="+mno+";";
        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            mtitle = cursor.getString(2);
            mcontent = cursor.getString(3);
            memo = new DtoMemo(mno, uno, mtitle, mcontent);
        }
        cursor.close();
        return memo;
    }

    public void insertmemo(int uno, String title, String content){
        String sql = "INSERT INTO Memo(uno, mtitle, mcontent)"
                + " VALUES(" + uno + ", '" + title + "', '" + content + "');";
        try {
            database.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updatememo(int mno, String title, String content){
        String sql = "UPDATE Memo SET mtitle = '"+ title+"', mcontent='"+content+"' WHERE mno="+mno+";";
        try {
            database.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deletememo(int mno){
        String sql = "DELETE FROM Memo WHERE mno =" +mno+";";
        try {
            database.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DtoDiary> getDiary(int uno) {
        ArrayList<DtoDiary> diary = new ArrayList<DtoDiary>();

        int dno;
        String dtitle;
        String dcontent;
        byte [] dimg;
        String ddate;

        String sql = "SELECT * FROM Diary WHERE uno = "+uno+";";
        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            dno = cursor.getInt(0);
            dtitle = cursor.getString(2);
            dcontent = cursor.getString(3);
            dimg=cursor.getBlob(4);
            ddate=cursor.getString(5);
            diary.add(new DtoDiary(dno, uno, dtitle, dcontent,dimg,ddate));
        }
        cursor.close();
        return diary;
    }

    public DtoDiary getDiarydno(int uno, int dno) {
        DtoDiary diary = null;

        String dtitle;
        String dcontent;
        byte [] dimg;
        String ddate;

        String sql = "SELECT * FROM Diary WHERE uno = "+uno+" AND dno ="+dno+";";
        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            dtitle = cursor.getString(2);
            dcontent = cursor.getString(3);
            dimg = cursor.getBlob(4);
            ddate=cursor.getString(5);
            diary = new DtoDiary(dno, uno, dtitle, dcontent, dimg,ddate);
        }
        cursor.close();
        return diary;
    }

    public DtoDiary getDiaryddate(int uno, String ddate) {
        DtoDiary diary = null;

        int dno;
        String dtitle;
        String dcontent;
        byte [] dimg;

        String sql = "SELECT * FROM Diary WHERE uno = "+uno+" AND ddate ='"+ddate+"';";
        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            dno = cursor.getInt(0);
            dtitle = cursor.getString(2);
            dcontent = cursor.getString(3);
            dimg = cursor.getBlob(4);
            diary = new DtoDiary(dno, uno, dtitle, dcontent, dimg,ddate);
        }
        Log.i("테스이빈당ㅇㅇ", ddate);
        cursor.close();
        return diary;
    }


    public void insertdiary(int uno, String title, String content, byte [] img, String date){
        String sql = "INSERT INTO Diary(uno, dtitle, dcontent,ddate)"
                + " VALUES(" + uno + ", '" + title + "', '" + content + "', '"+date+"');";
        try {
            database.execSQL(sql);
            if(img!=null) {
                ContentValues values = new ContentValues();
                values.put("dimg", img);
                database.insert("Diary", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatediary(int dno, String title, String content, byte [] img){
        String sql = "UPDATE Diary SET dtitle = '"+ title+"', dcontent='"+content+"', dimg='"+img
                +" WHERE dno="+dno+";";
        try {
            database.execSQL(sql);
            ContentValues values = new ContentValues();
            values.put("dimg",img);
            database.update("Diary",values," dno=?",new String[]{""+dno});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletediary(int dno){
        String sql = "DELETE FROM Diary WHERE dno =" +dno+";";
        try {
            database.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getJsonTestData(){
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append("     {");
        sb.append("     'user' : [");
        sb.append("          {");
        sb.append("             'uid':'admin',");
        sb.append("             'upw':'admin',");
        sb.append("             'uemail':'skaska1212@naver.com'");
        sb.append("         },");
        sb.append("         {");
        sb.append("             'uid':'root',");
        sb.append("             'upw':'root',");
        sb.append("             'uemail':'skaska1212@naver.com'");
        sb.append("          },");
        sb.append("         {");
        sb.append("             'uid':'asd',");
        sb.append("             'upw':'asd',");
        sb.append("             'uemail':'asd@naver.com'");
        sb.append("          }");
        sb.append("     ]");


        sb.append("}");



        return sb.toString();

    }
}
