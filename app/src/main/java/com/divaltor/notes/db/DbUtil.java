package com.divaltor.notes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

import com.divaltor.notes.NotesApp;

import java.util.ArrayList;
import java.util.List;

public class DbUtil {
    private final static int DB_VERSION = 1;
    private final static String
            DB = "notes.db",
            TABLE = "notes",
            COLUMN_UID = "uid",
            COLUMN_TITLE = "title",
            COLUMN_TEXT = "text",
            COLUMN_INDEX = "_index";

    private static Helper helper;

    /**
     * Inserts or updates note in the DB
     * @param note Note to insert or update
     */
    public static void insertOrUpdate(Note note) {
        SQLiteDatabase readableDatabase = getHelper().getReadableDatabase();
        boolean has;
        if (note.id == -1) {
            has = false;
        } else {
            Cursor cursor = readableDatabase.rawQuery(String.format("SELECT 1 FROM %s WHERE %s = %s", TABLE, COLUMN_UID, note.id), null);
            has = cursor.moveToNext();
            cursor.close();
        }

        if (!has) {
            Cursor cursor = readableDatabase.rawQuery(String.format("SELECT MAX(%s), MAX(%s) FROM %s", COLUMN_UID, COLUMN_INDEX, TABLE), null);
            if (cursor.moveToNext()) {
                note.id = cursor.getInt(0) + 1;
                note.index = cursor.getInt(1) + 1;
            } else {
                note.id = 0;
                note.index = 0;
            }
            cursor.close();
        }

        SQLiteDatabase writableDatabase = getHelper().getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_UID, note.id);
        contentValues.put(COLUMN_TITLE, note.title);
        contentValues.put(COLUMN_TEXT, note.text);
        contentValues.put(COLUMN_INDEX, note.index);
        if (has) {
            writableDatabase.update(TABLE, contentValues, String.format("%s=%s", COLUMN_UID, note.id), null);
        } else {
            writableDatabase.insert(TABLE, null, contentValues);
        }
    }

    /**
     * Removes note in the DB
     * @param note Note to remove
     */
    public static void remove(Note note) {
        SQLiteDatabase writableDatabase = getHelper().getWritableDatabase();
        writableDatabase.delete(TABLE, String.format("%s=%s", COLUMN_UID, note.id), null);
    }

    /**
     * @return ALl notes in the DB
     */
    public static List<Note> getNotes() {
        SQLiteDatabase readableDatabase = getHelper().getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery(String.format("SELECT * FROM %s", TABLE), null);
        ContentValues contentValues = new ContentValues();
        List<Note> noteList = new ArrayList<>();
        while (cursor.moveToNext()) {
            DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
            Note note = new Note();
            note.id = contentValues.getAsInteger(COLUMN_UID);
            note.index = contentValues.getAsInteger(COLUMN_INDEX);
            note.title = contentValues.getAsString(COLUMN_TITLE);
            note.text = contentValues.getAsString(COLUMN_TEXT);
            noteList.add(note);
        }
        cursor.close();
        return noteList;
    }

    private static Helper getHelper() {
        if (helper == null)
            helper = new Helper(NotesApp.APP_INSTANCE);
        return helper;
    }

    private static class Helper extends SQLiteOpenHelper {

        public Helper(@Nullable Context context) {
            super(context, DB, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT)",
                    TABLE,
                    BaseColumns._ID,
                    COLUMN_UID,
                    COLUMN_INDEX,
                    COLUMN_TITLE,
                    COLUMN_TEXT));
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    }
}
