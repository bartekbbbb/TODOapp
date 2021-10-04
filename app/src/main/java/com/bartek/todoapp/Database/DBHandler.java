package com.bartek.todoapp.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bartek.todoapp.Model.TODOModel;

import java.util.ArrayList;
import java.util.List;


public class DBHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String DATA = "data";
    private static final String CAT = "cat";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, " + DATA + " TEXT, " + CAT + " TEXT)";

    private SQLiteDatabase db;

    public DBHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(TODOModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getText());
        cv.put(DATA, task.getData());
        cv.put(CAT, task.getCategory());
        db.insert(TODO_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<TODOModel> getAllTasks(){
        List<TODOModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        TODOModel task = new TODOModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setText(cur.getString(cur.getColumnIndex(TASK)));
                        task.setData(cur.getString(cur.getColumnIndex(DATA)));
                        task.setCategory(cur.getString(cur.getColumnIndex(CAT)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
