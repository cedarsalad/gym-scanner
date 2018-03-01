package com.example.chads.gymscanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gboursiquot on 2/26/2018.
 */

public class DBManager extends SQLiteOpenHelper {
    //Static Variables

    //Database version, creates a new database when changed
    private static final int DB_VERSION = 13;

    private static final String DB_NAME = "gym";
    private static final String TABLE_NAME = "members";
    private static final String KEY_ID = "id";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_LASTNAME = "lastName";
    private static final String KEY_DOB = "dob";
    private static final String KEY_AGE = "age";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CITY = "city";
    private static final String KEY_PROVINCE = "province";
    private static final String KEY_BARCODE = "barcode";
    private static final String KEY_VISITS = "visits";
    private static final String KEY_STATUS = "status";

    public DBManager(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        String CREATE_MEMBER_TABLE = "CREATE TABLE " + TABLE_NAME + " (";
        CREATE_MEMBER_TABLE += KEY_ID + " INTEGER PRIMARY KEY, ";
        CREATE_MEMBER_TABLE += KEY_AVATAR + " TEXT, ";
        CREATE_MEMBER_TABLE += KEY_FIRSTNAME + " TEXT, ";
        CREATE_MEMBER_TABLE += KEY_LASTNAME + " TEXT, ";
        CREATE_MEMBER_TABLE += KEY_DOB + " TEXT, ";
        CREATE_MEMBER_TABLE += KEY_AGE + " TEXT, ";
        CREATE_MEMBER_TABLE += KEY_ADDRESS + " TEXT, ";
        CREATE_MEMBER_TABLE += KEY_CITY + " TEXT, ";
        CREATE_MEMBER_TABLE += KEY_PROVINCE + " TEXT, ";
        CREATE_MEMBER_TABLE += KEY_BARCODE + " TEXT, ";
        CREATE_MEMBER_TABLE += KEY_VISITS + " SMALLINT, ";
        CREATE_MEMBER_TABLE += KEY_STATUS + " BOOLEAN)";

        db.execSQL(CREATE_MEMBER_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addMember (Member member){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, (Byte) null);
        values.put(KEY_AVATAR, member.getAvatar());
        values.put(KEY_FIRSTNAME, member.getFirstName());
        values.put(KEY_LASTNAME, member.getLastName());
        values.put(KEY_DOB, member.getDob());
        values.put(KEY_AGE, member.getAge());
        values.put(KEY_ADDRESS, member.getAddress());
        values.put(KEY_CITY, member.getCity());
        values.put(KEY_PROVINCE, member.getProvince());
        values.put(KEY_BARCODE, member.getBarcode());
        values.put(KEY_VISITS, member.getVisits());
        values.put(KEY_STATUS, member.isStatus());
        Log.d("member created: ", member.toString());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<Member> getAllMembers(){
        ArrayList<Member> members = new ArrayList<Member>();
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Member m = new Member();
                m.setId(Integer.parseInt(cursor.getString(0)));
                m.setAvatar(cursor.getString(1));
                m.setFirstName(cursor.getString(2));
                m.setLastName(cursor.getString(3));
                m.setDob(cursor.getString(4));
                m.setAge(cursor.getInt(5));
                m.setAddress(cursor.getString(6));
                m.setCity(cursor.getString(7));
                m.setProvince(cursor.getString(8));
                m.setBarcode(cursor.getString(9));
                m.setVisits(cursor.getInt(10));
                m.setStatus(cursor.getInt(11)>0);

                Log.d("Member #" + cursor.getString(0), m.toString());
                members.add(m);
            }while(cursor.moveToNext());
        }

        return members;
    }

    public Member getMemberByCode(String code){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{KEY_ID, KEY_AVATAR, KEY_FIRSTNAME, KEY_LASTNAME, KEY_DOB, KEY_AGE, KEY_ADDRESS, KEY_CITY, KEY_PROVINCE, KEY_BARCODE, KEY_VISITS, KEY_STATUS},
                KEY_BARCODE  + "= ?",
                new String[]{code},
                null,
                null,
                null,
                null);

        Member m = new Member();
        Log.d("Test", Integer.toString(cursor.getCount()));

        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            m.setId(Integer.parseInt(cursor.getString(0)));
            m.setAvatar(cursor.getString(1));
            m.setFirstName(cursor.getString(2));
            m.setLastName(cursor.getString(3));
            m.setDob(cursor.getString(4));
            m.setAge(cursor.getInt(5));
            m.setAddress(cursor.getString(6));
            m.setCity(cursor.getString(7));
            m.setProvince(cursor.getString(8));
            m.setBarcode(cursor.getString(9));
            m.setVisits(cursor.getInt(10));
            m.setStatus(cursor.getInt(11) > 0);
        }

        return m;
    }

    public void setStatus(String code, boolean status){
        SQLiteDatabase db = this.getWritableDatabase();

        //array to pass to query (associative array)
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, status);

        //query to update the database
        db.update(
                TABLE_NAME,
                values,
                KEY_BARCODE +" = ? ",
                    new String[]{code});
        db.close();
    }

    //increment visit count!
    public void addVisit(String code){
        SQLiteDatabase db = this.getWritableDatabase();

        //array to pass to query (associative array)
        ContentValues values = new ContentValues();

        Member m = getMemberByCode(code);
        int visits = m.getVisits();
        visits++;
        values.put(KEY_VISITS, visits);

        //query to update the database
        db.update(
                TABLE_NAME,
                values,
                KEY_BARCODE +" = ? ",
                new String[]{code});
        db.close();
    }

    public void deleteMember (Member member){
        SQLiteDatabase db = this.getWritableDatabase();

        //query to delete from database
        db.delete(
                TABLE_NAME,
                KEY_ID +" = ? ",
                new String[]{String.valueOf(member.getId())});
        db.close();
    }

    public int getContactsCount(){
        //Query to select all from database
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        //return the number of records found
        return cursor.getCount();
    }
}
