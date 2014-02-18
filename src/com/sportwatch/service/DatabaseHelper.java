package com.sportwatch.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.sportwatch.model.scene.OptionsScene;
import com.sportwatch.util.ClockHandColor;
import com.sportwatch.util.ConstantsUtil;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

/**
 * User: Breku
 * Date: 07.10.13
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "myDB_sport_clock";

    /**
     * Every clock hand has its own color
     */
    private static final String HAND_CLOCK_OPTIONS_TABLE = "HAND_CLOCK_OPTIONS";

    /**
     * Main options like number of hand clocks
     */
    private static final String MAIN_OPTIONS_TABLE = "MAIN_OPTIONS";

    private static final String COLUMN_NUMBER_OF_HAND_CLOCKS = "COLUMN_NUMBER_OF_HAND_CLOCKS";

    private static final String COLUMN_ID = "ID";

    /**
     * Clock hands are numbered from zero
     */
    private static final String COLUMN_CLOCK_HAND_NUMBER = "CLOCK_HAND_NUMBER";

    private static final String COLUMN_CLOCK_HAND_COLOR = "COLUMN_CLOCK_HAND_COLOR";
    private static final int DATABASE_VERSION = 10;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when database does not exists
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + HAND_CLOCK_OPTIONS_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CLOCK_HAND_NUMBER + " INTEGER, " +
                COLUMN_CLOCK_HAND_COLOR + " TEXT" +
                ");");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + MAIN_OPTIONS_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NUMBER_OF_HAND_CLOCKS + " INTEGER " +
                ");");

        createDefaultHighScoreValues(sqLiteDatabase);
    }


    /**
     * Is called when DATABASE_VERSION is upgraded
     *
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HAND_CLOCK_OPTIONS_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MAIN_OPTIONS_TABLE);
        onCreate(sqLiteDatabase);
    }


    private boolean isTableExists(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        if (tableName == null || db == null || !db.isOpen()) {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count > 0;
    }

    private void createDefaultHighScoreValues(SQLiteDatabase sqLiteDatabase) {


        createDefaultNumberOfClockHands(sqLiteDatabase, ConstantsUtil.MAX_NUMBER_OF_CLOCK_HANDS);
        createDefaulColorOfClockDial(sqLiteDatabase,ClockHandColor.WHITE);

        // Create default colors for each clock hand
        for (int i = 0; i < ConstantsUtil.MAX_NUMBER_OF_CLOCK_HANDS; i++) {
            createDefaultHighScoreRecord(sqLiteDatabase, i, ClockHandColor.WHITE);
        }
    }

    private void createDefaulColorOfClockDial(SQLiteDatabase sqLiteDatabase, ClockHandColor color) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_CLOCK_HAND_NUMBER, OptionsScene.CLOCK_DIAL_NUMER);
        contentValues.put(COLUMN_CLOCK_HAND_COLOR, color.name());

        sqLiteDatabase.insert(HAND_CLOCK_OPTIONS_TABLE, null, contentValues);
    }

    private void createDefaultNumberOfClockHands(SQLiteDatabase sqLiteDatabase, Integer numberOfHandClocks) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NUMBER_OF_HAND_CLOCKS, numberOfHandClocks);

        sqLiteDatabase.insert(MAIN_OPTIONS_TABLE, null, contentValues);
    }

    private void createDefaultHighScoreRecord(SQLiteDatabase sqLiteDatabase, Integer clockHandNumber, ClockHandColor color) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_CLOCK_HAND_NUMBER, clockHandNumber);
        contentValues.put(COLUMN_CLOCK_HAND_COLOR, color.name());

        sqLiteDatabase.insert(HAND_CLOCK_OPTIONS_TABLE, null, contentValues);


    }

    public Integer getNumberOfHandClocks() {
        Integer result = null;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_NUMBER_OF_HAND_CLOCKS + " FROM " + MAIN_OPTIONS_TABLE, new String[]{});
        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result;
    }

    public void updateNumberOfHandClocks(int numberOfHandClocks) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE " + MAIN_OPTIONS_TABLE + " SET " + COLUMN_NUMBER_OF_HAND_CLOCKS + " = ? ", new Object[]{numberOfHandClocks});
        database.close();
    }

    public void setClockHandColor(int clockNumber, String color) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE " + HAND_CLOCK_OPTIONS_TABLE + " SET " + COLUMN_CLOCK_HAND_COLOR + " = ?  WHERE " + COLUMN_CLOCK_HAND_NUMBER + " = ?", new Object[]{color, clockNumber});
        database.close();
    }

    public Color getColorForClockHand(Integer clockHandNumber) {
        String result = null;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_CLOCK_HAND_COLOR + " FROM " + HAND_CLOCK_OPTIONS_TABLE + " WHERE " + COLUMN_CLOCK_HAND_NUMBER + " = ?", new String[]{clockHandNumber.toString()});
        while (cursor.moveToNext()) {
            result = cursor.getString(0);
        }
        cursor.close();
        database.close();
        for (ClockHandColor color : ClockHandColor.values()) {
            Debug.e(result + " " + color.name());
            if (result.toString().equals(color.name())) {
                return color.getColor();
            }

        }
        throw new UnsupportedOperationException("Color does not exists. Retrieved color from db: " + result.toString());
    }

    public boolean isClockHandColored(Integer clockNumber, ClockHandColor color) {
        String result = null;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_CLOCK_HAND_COLOR + " FROM " + HAND_CLOCK_OPTIONS_TABLE + " WHERE " + COLUMN_CLOCK_HAND_NUMBER + " = ?", new String[]{clockNumber.toString()});
        while (cursor.moveToNext()) {
            result = cursor.getString(0);
        }
        cursor.close();
        database.close();
        return result.equals(color.name());
    }
}
