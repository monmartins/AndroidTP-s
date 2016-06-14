package br.ufc.dc.dspm.mynotes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class NoteDAO extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyNotes1.db";
    public static final int DATABASE_VERSION = 1;
    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);

    public NoteDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public NoteDAO(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table notes (");
        sql.append("id integer primary key autoincrement,");
        sql.append("title text,");
        sql.append("content text,");
        sql.append("date DATE)");
        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists notes");
        onCreate(db);
    }

    public void create(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", note.getTitle());
        contentValues.put("content", note.getContent());
        contentValues.put("date", formatter.format(note.getDate()));
        long id = db.insert("notes", null, contentValues);
        Log.v("SQLite", "create id = " + id);
    }

    public Note retrieve(Integer id) {
        String[] fieldValues = new String[1];
        fieldValues[0] = Integer.toString(id);
        Log.v("SQLite", "create id = " + Arrays.toString(fieldValues));
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from notes where id = ?", fieldValues);
        Note note = list().get(id);
//        if (result != null && result.getCount() > 0) {
//            note = new Note();
//            note.setId(result.getInt(0));
//            note.setTitle(result.getString(1));
//            note.setContent(result.getString(2));
//            try {
//                note.setDate(formatter.parse(result.getString(3)));
//            } catch (ParseException e) {
//                note.setDate(null);
//            }
//        }
        result.close();
        return note;
    }

    public void update(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", note.getTitle());
        contentValues.put("content", note.getContent());
        contentValues.put("date", formatter.format(note.getDate()));
        String[] fieldValues = new String[1];
        fieldValues[0] = Integer.toString(note.getId());
        db.update("notes", contentValues, " id = ? ", fieldValues);
    }

    public void delete(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("notes", " id = ? ", new String[] { Integer.toString(id) });
    }

    public List<Note> list() {
        List<Note> notes = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from notes order by id", null);
        if (result != null && result.getCount() > 0) {
            notes = new ArrayList<Note>();
            result.moveToFirst();
            while (result.isAfterLast() == false) {
                Note note = new Note();
                note.setId(result.getInt(0));
                note.setTitle(result.getString(1));
                note.setContent(result.getString(2));
                try {
                    note.setDate(formatter.parse(result.getString(3)));
                } catch (ParseException e) {
                    note.setDate(null);
                }
                notes.add(note);
                result.moveToNext();
            }
        }
        result.close();
        return notes;
    }
    public List<Note> listByIterval(Date initialDate, Date finalDate){
        int cont=0;
        List<Note> notes = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Log.v("Pesquisa", "tentando4 = " + initialDate + " " + finalDate);
        Cursor result = db.rawQuery("select * from notes order by id", null);
        if (result != null && result.getCount() > 0) {
            Log.v("Pesquisa", "tentando5 = " + initialDate + " " + finalDate);
            notes = new ArrayList<Note>();
            result.moveToFirst();
            while (result.isAfterLast() == false) {
                Note note = new Note();
                try {
//                    if(formatter.parse(result.getString(3)).getTime()==initialDate.getTime())

                    if(formatter.parse(result.getString(3)).getTime()>initialDate.getTime()&&formatter.parse(result.getString(3)).getTime()<finalDate.getTime()) {
                        note.setId(result.getInt(0));
                        note.setTitle(result.getString(1));
                        note.setContent(result.getString(2));
                        note.setDate(formatter.parse(result.getString(3)));
                        notes.add(note);
                        cont++;
                    }
                    result.moveToNext();
                } catch (ParseException e) {
                    note.setDate(null);
                }
            }
        }
        result.close();
        if(cont==0){
            notes = null;
        }
        return notes;
    }
}