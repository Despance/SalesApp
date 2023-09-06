package com.despance.salesapp.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "password")
    private String password;
    @ColumnInfo(name = "firstName")
    private String firstName;



    @ColumnInfo(name = "lastName")
    private String lastName;

    public User( String email, String password, String firstName, String lastName){
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(int id, String email, String password, String firstName, String lastName){
        this(email, password, firstName, lastName);
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }


    public static class DatabaseHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "userManager";
        private static final String TABLE_NAME = "users";
        private static final String KEY_ID = "id";
        private static final String KEY_EMAIL = "email";
        private static final String KEY_PASSWORD = "password";
        private static final String KEY_FIRSTNAME = "firstName";
        private static final String KEY_LASTNAME = "lastName";

        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_EMAIL + " TEXT,"
                    + KEY_PASSWORD + " TEXT,"
                    + KEY_FIRSTNAME + " TEXT,"
                    + KEY_LASTNAME + " TEXT" + ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }

        public void addUser(User user){
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_EMAIL, user.getEmail());
            values.put(KEY_PASSWORD, user.getPassword());
            values.put(KEY_FIRSTNAME, user.getFirstName());
            values.put(KEY_LASTNAME, user.getLastName());

            db.insert(TABLE_NAME, null, values);
            db.close();
        }

        public List<User> getAllUsers(){
            List<User> userList = new ArrayList<User>();

            String selectQuery = "SELECT * FROM " + TABLE_NAME;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()){
                do {
                    User currentUser= new User(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                    currentUser.setId(Integer.parseInt(cursor.getString(0)));
                    userList.add(currentUser);
                }while (cursor.moveToNext());
            }
            return userList;
        }
        public void updateUser(User user){
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_EMAIL, user.getEmail());
            values.put(KEY_PASSWORD, user.getPassword());
            values.put(KEY_FIRSTNAME, user.getFirstName());
            values.put(KEY_LASTNAME, user.getLastName());

            db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[]{String.valueOf(user.getId())});
            db.close();
        }

        public void deleteUser(User user){
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(user.getId())});
            db.close();
        }

        public User findUser(String email, String password){
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_EMAIL + " = " + "\""+email+"\""+" AND "+ KEY_PASSWORD + " = "+ "\""+password+"\"" ;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.moveToFirst()){
                do {
                    User currentUser= new User(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                    currentUser.setId(Integer.parseInt(cursor.getString(0)));
                    return currentUser;
                }while (cursor.moveToNext());
            }
            return null;
        }

        public void deleteAll() {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME, null, null);
            db.close();
        }
    }
}
