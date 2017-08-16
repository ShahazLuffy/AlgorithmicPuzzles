package com.example.shahaz.algorithmicpuzzles;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.database.sqlite.SQLiteDiskIOException;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by DNA.h on 8/1/2017.
 */

public class Database extends SQLiteOpenHelper {
    private Context context;
    private static String database_name="puzzles.db";
    private static int database_version = 15;
    private SQLiteDatabase localDB;
    
    //Table for each puzzle
    private static final String COL_ID = "id";
    private static final String COL_HINT = "hint";
    private static final String COL_INTRO = "intro";
    private static final String COL_NOTATION = "notation";
    private static final String COL_HARDNESS = "hardness";
    private static final String COL_TITLE = "title";
    private static final String COL_TRIVIA = "trivia";
    private static final String COL_ICON = "icon";
    private static final String COL_ADDRESS= "imageAddress";
    private static final String COL_CATEGORY="category";
    private static final String COL_DATEADDED="dateAdded";
    //LOCAL NAMES
    private static final String TABLE_PUZZLE = "puzzleInfoTB";

    private static final String DATABASE_PUZZLES_CREATE = "create table " +
            TABLE_PUZZLE + "(" +
            COL_ID + " integer primary key, " +
            COL_HINT + " text not null, " +
            COL_INTRO + " text not null, " +
            COL_NOTATION + " text, " +
            COL_HARDNESS + " integer, " +
            COL_TITLE + " text, " +
            COL_TRIVIA + " text, " +
            COL_ICON + " text, " +
            COL_CATEGORY+ " text, " +
            COL_DATEADDED + " text, "+
            COL_ADDRESS + " text);";

    public Database(Context context) {
        super(context,database_name, null, database_version);
        this.context = context;
    }

    public SQLiteDatabase getDatabase(Context context) {
        // Setup path and database name
        if ((localDB == null) || !localDB.isOpen()) {
            try {
                localDB = new Database(context).getWritableDatabase(); // Now it's writable! I think.
            } catch (SQLiteDatabaseCorruptException sqldce) {
                Log.e("Database", "SQLiteDatabaseCorruptException", sqldce);
                //Util.getInstance().toast(context, context.getResources().getString(R.string.error_while_trying_to_open_db));
            } catch (SQLiteCantOpenDatabaseException sqlcode) {
                Log.e("Database", "SQLiteCantOpenDatabaseException", sqlcode);
                //Util.getInstance().toast(context, context.getResources().getString(R.string.error_while_trying_to_open_db));
            } catch (SQLException sqle) {
                Log.e("Database", "SQLException", sqle);
                //Util.getInstance().toast(context, context.getResources().getString(R.string.error_while_trying_to_open_db));
            } catch (Exception e) {
                Log.e("Database", "Exception", e);
                //Util.getInstance().toast(context, context.getResources().getString(R.string.error_while_trying_to_open_db));
            }
        }
        return localDB;
    }

    private ContentValues setPuzzleCV(Puzzle puzzle) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ID, puzzle.getId());
        cv.put(COL_HINT, puzzle.getHint());
        cv.put(COL_INTRO, puzzle.getIntro());
        cv.put(COL_NOTATION, puzzle.getNotation());
        cv.put(COL_HARDNESS, puzzle.getHardness());
        cv.put(COL_TITLE, puzzle.getTitle());
        cv.put(COL_TRIVIA, puzzle.getTrivia());
        cv.put(COL_ICON, puzzle.getIcon());
        cv.put(COL_ADDRESS, puzzle.getImageAddress());
        cv.put(COL_DATEADDED,puzzle.getDateAdded());
        cv.put(COL_CATEGORY, puzzle.getCategory());

        return cv;
    }

    public int addPuzzle(Puzzle puzzle) {
        int tmp = -1;
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            if (!database.isReadOnly()) {
                tmp=(int) database.insertOrThrow(TABLE_PUZZLE, null, setPuzzleCV(puzzle));
                database.close();
            }else {
                Log.e("Database", "(addManga) " + "error_database_is_read_only");
            }
        } catch (Exception e){
            Log.e("Database", Log.getStackTraceString(e));
            //Util.getInstance().toast(context, context.getResources().getString(R.string.error_while_adding_chapter_or_manga_to_db, manga.getTitle()));
        }
        return tmp;
    }

    public void updatePuzzle( Puzzle puzzle) {
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            if (!database.isReadOnly())
                database.update(TABLE_PUZZLE, setPuzzleCV(puzzle), COL_ID + " = " + puzzle.getId(), null);
            else {
                Log.e("Database", "(updateManga) " + "error_database_is_read_only");
               // Util.getInstance().toast(context, context.getResources().getString(R.string.error_database_is_read_only));
            }
        } catch (SQLiteFullException sqlfe) {
            Log.e("Database", "SQLiteFullException", sqlfe);
            outputPuzzleDebugInformation(puzzle);
            //Util.getInstance().toast(context, context.getString(R.string.error_while_updating_chapter_or_manga_in_db, manga.getTitle()));
        } catch (SQLiteDiskIOException sqldioe) {
            Log.e("Database", "SQLiteDiskIOException", sqldioe);
            outputPuzzleDebugInformation(puzzle);
            //Util.getInstance().toast(context, context.getString(R.string.error_while_updating_chapter_or_manga_in_db, manga.getTitle()));
        } catch (Exception e) {
            Log.e("Database", "Exception", e);
            outputPuzzleDebugInformation(puzzle);
            //Util.getInstance().toast(context, context.getString(R.string.error_while_updating_chapter_or_manga_in_db, manga.getTitle()));
        }
    }

    private void outputPuzzleDebugInformation(Puzzle puzzle) {
        Log.i("Database", "Title: " + puzzle.getTitle());
        Log.i("Database", "ID: " + puzzle.getId());
    }

    public ArrayList<Puzzle> getPuzzles(String sortBy, boolean asc) {
        return getPuzzlesCondition(null, sortBy, asc);
    }

    public ArrayList<Puzzle> getPuzzlesCondition(
            String condition, String sortBy, boolean asc) {
        if (sortBy == null) sortBy = COL_ID;
        Cursor cursor = null;
        if(this.getReadableDatabase() != null) {
            //cursor=this.getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_PUZZLE,null);
            cursor = this.getReadableDatabase().query(
                    TABLE_PUZZLE,
                    new String[]{
                            COL_ID,COL_HINT,COL_INTRO,COL_NOTATION,COL_HARDNESS,
                            COL_TITLE,COL_TRIVIA,COL_ICON,COL_ADDRESS,COL_CATEGORY,COL_DATEADDED
                    },

                    condition, null, null, null, sortBy + (asc ? " ASC" : " DESC"));
        }

        return getPuzzlesFromCursor(cursor);
        //return null;
    }

    private ArrayList<Puzzle> getPuzzlesFromCursor(Cursor cursor) {
        ArrayList<Puzzle> puzzles = new ArrayList<>();
        try{
            if(cursor != null) {
                if (cursor.moveToFirst()) {
                    int colId = cursor.getColumnIndexOrThrow(COL_ID);
                    int colHint = cursor.getColumnIndexOrThrow(COL_HINT);
                    int colIntro = cursor.getColumnIndexOrThrow(COL_INTRO);
                    int colNotation = cursor.getColumnIndexOrThrow(COL_NOTATION);
                    int colHardness = cursor.getColumnIndexOrThrow(COL_HARDNESS);
                    int colTitle = cursor.getColumnIndexOrThrow(COL_TITLE);
                    int colTrivia = cursor.getColumnIndexOrThrow(COL_TRIVIA);
                    int colIcon = cursor.getColumnIndexOrThrow(COL_ICON);
                    int colAddress = cursor.getColumnIndexOrThrow(COL_ADDRESS);
                    int colCategory = cursor.getColumnIndexOrThrow(COL_CATEGORY);
                    int colDateAdded = cursor.getColumnIndexOrThrow(COL_DATEADDED);
                    Puzzle puzzle;
                    do {
                        puzzle = new Puzzle();
                        puzzle.setId(cursor.getInt(colId));
                        puzzle.setHint(cursor.getString(colHint));
                        puzzle.setIntro(cursor.getString(colIntro));
                        puzzle.setNotation(cursor.getString(colNotation));
                        puzzle.setHardness(cursor.getInt(colHardness));
                        puzzle.setTitle(cursor.getString(colTitle));
                        puzzle.setTrivia(cursor.getString(colTrivia));
                        puzzle.setIcon(cursor.getString(colIcon));
                        puzzle.setImageAddress(cursor.getString(colAddress));
                        puzzle.setCategory(cursor.getString(colCategory));
                        puzzle.setDateAdded(cursor.getString(colDateAdded));
                        puzzles.add(puzzle);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }else{
                puzzles =new ArrayList<>();
                Puzzle p=new Puzzle();
                p.setTitle("33");
                puzzles.add(p);
                return puzzles;
            }
        }catch (Exception e){
            puzzles =new ArrayList<>();
            Puzzle p=new Puzzle();
            p.setTitle(e.getMessage());
            puzzles.add(p);
            return puzzles;
        }
        return puzzles;
    }

    public Puzzle getFullPuzzle(int puzzleID) {
        return getFullPuzzle(puzzleID, false);
    }

    private Puzzle getFullPuzzle(int puzzleID, boolean asc) {
        Puzzle puzzle = new Puzzle();
        try {
            puzzle = getPuzzlesCondition(COL_ID + " = " + puzzleID, null, false).get(0);
        } catch (Exception ignore) {
            Log.d("Database", "Exception", ignore);
        }
        return puzzle;
    }

    public int deletePuzzle(int mid) {
        SQLiteDatabase database = this.getWritableDatabase();
        if (!database.isReadOnly()) {
            database.delete(TABLE_PUZZLE, COL_ID + " = " + mid, null);
        } else {
            Log.e("Database", "(deletePuzzle) " + "error_database_is_read_only");
        }
        return 0;
    }

    public int deleteAll(){
        SQLiteDatabase database = this.getWritableDatabase();
        if (!database.isReadOnly()) {
            return database.delete(TABLE_PUZZLE,"1",null);
        } else {
            Log.e("Database", "(deletePuzzle) " + "error_database_is_read_only");
        }
        return 0;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("Drop TABLR "+TABLE_PUZZLE);
        //if (context.getDatabasePath("puzzles.db").exists() && !(doesTableExist(db, TABLE_PUZZLE))) {
            //move to new path
            //copyDbToSd(context);
            //db.close();
            //TODO CHECK HERE
            //restartApp(context);
        //} else {
        //db.execSQL("DROP TABLE IF EXISTS "+TABLE_PUZZLE);
        //db.close();
        db.execSQL(DATABASE_PUZZLES_CREATE);
            //if(!(doesTableExist(db, TABLE_PUZZLE)))
        //}
    }

    private void copyDbToSd(Context c) {
        File dbFile = c.getDatabasePath("puzzles.db");
        String ruta = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AlgorithmicPuzzles/";
        ruta += "dbs/";
        File exportDir = new File(ruta, "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, dbFile.getName());
        try {
            file.createNewFile();
            InputStream is = new FileInputStream(dbFile);
            FileCache.writeFile(is, file);
            is.close();
        } catch (IOException e) {
            Toast.makeText(c, "Error: ", Toast.LENGTH_LONG).show();
        }
    }

    private boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery(
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '" +
                        tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
