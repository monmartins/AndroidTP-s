package br.ufc.dc.dspm.mynotes;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Lincoln Rocha on 29/05/16.
 */
public class NotesProvider extends ContentProvider {

    static final String PROVIDER_NAME = "br.ufc.dc.dspm.mynotes.provider.Note";
    static final String URL = "content://" + PROVIDER_NAME + "/notes";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String ID = "id";
    static final String TITLE = "title";
    static final String CONTENT = "content";
    static final String DATE = "date";
    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);

    private static HashMap<String, String> NOTES_PROJECTION_MAP;


    static final int NOTES = 1;
    static final int NOTE_ID = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "notes", NOTES);
        uriMatcher.addURI(PROVIDER_NAME, "notes/#", NOTE_ID);
    }

    private SQLiteDatabase database;
    static final String DATABASE_NAME = "MyNotesV2.db";
    static final int DATABASE_VERSION = 1;
    static final String NOTES_TABLE_NAME = "notes";
    static final String CREATE_DB_TABLE = "create table " + NOTES_TABLE_NAME +
            " (id integer primary key autoincrement," +
            " title text, content text, date DATE)";

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();

        return (database == null) ? false : true;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            /**
             * Get all note records
             */
            case NOTES:
                return "vnd.android.cursor.dir/vnd.br.ufc.dc.sdpm.mynotes.notes";
            /**
             * Get a particular note
             */
            case NOTE_ID:
                return "vnd.android.cursor.item/vnd.br.ufc.dc.sdpm.mynotes.notes";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(NOTES_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case NOTES:
                queryBuilder.setProjectionMap(NOTES_PROJECTION_MAP);
                break;
            case NOTE_ID:
                queryBuilder.appendWhere(ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = ID;
        }

        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = database.insert(NOTES_TABLE_NAME, "", values);

        if (rowID > 0) {
            Uri uriAux = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(uriAux, null);
            return uriAux;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case NOTES:
                count = database.delete(NOTES_TABLE_NAME,  " id = ? ", selectionArgs);
                break;
            case NOTE_ID:
                String id = uri.getPathSegments().get(1);
                count = database.delete(NOTES_TABLE_NAME, ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String [] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case NOTES:
                count = database.update(NOTES_TABLE_NAME, values,
                        " id = ? ", selectionArgs);
                break;
            case NOTE_ID:
                count = database.update(NOTES_TABLE_NAME, values, ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            database.execSQL("drop table if exists " + NOTES_TABLE_NAME);
            onCreate(database);
        }
    }


}
