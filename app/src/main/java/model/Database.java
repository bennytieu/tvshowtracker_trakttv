package model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


/**
 * A local database to store the tv-shows in lists
 */
public class Database extends SQLiteOpenHelper {
    private static final String DB_NAME = "ShowListsDB";
    private static final String TABLE1 = "watching";
    private static final String TABLE2 = "plan_to_watch";
    private static final String TABLE3 = "completed";
    private static final String TABLE4 = "on_hold";
    private static final String TABLE5 = "dropped";

    public Database(Context context)
    {
        super(context, DB_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE1 + " (id INTERGER PRIMARY KEY, title VARCHAR, year VARCHAR, overview VARCHAR, thumb VARCHAR);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE2 + " (id INTERGER PRIMARY KEY, title VARCHAR, year VARCHAR, overview VARCHAR, thumb VARCHAR);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE3 + " (id INTERGER PRIMARY KEY, title VARCHAR, year VARCHAR, overview VARCHAR, thumb VARCHAR);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE4 + " (id INTERGER PRIMARY KEY, title VARCHAR, year VARCHAR, overview VARCHAR, thumb VARCHAR);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE5 + " (id INTERGER PRIMARY KEY, title VARCHAR, year VARCHAR, overview VARCHAR, thumb VARCHAR);");

    }

    public boolean addShowToList(int listIndex, TVShow tvshow) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues tvShowMap = new ContentValues();
        tvShowMap.put("id",tvshow.getID());
        tvShowMap.put("title",tvshow.getTitle());
        tvShowMap.put("year",tvshow.getYear());
        tvShowMap.put("overview",tvshow.getOverview());
        tvShowMap.put("thumb",tvshow.getImgThumb());

        switch(listIndex) {
            case 0:  db.insert(TABLE1,null,tvShowMap);
                break;
            case 1:  db.insert(TABLE2,null,tvShowMap);
                break;
            case 2:  db.insert(TABLE3,null,tvShowMap);
                break;
            case 3:  db.insert(TABLE4,null,tvShowMap);
                break;
            case 4:  db.insert(TABLE5,null,tvShowMap);
                break;
            default:
        }

        return true;
    }

    public ArrayList<TVShow> getTVShowsFromList(String listName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<TVShow> tvShows = new ArrayList<>();
        Cursor resultSet = db.rawQuery("SELECT * FROM "+listName, null);

        TVShow tvshow;
        while (resultSet.moveToNext()) {
            tvshow = new TVShow();
            tvshow.setID(resultSet.getInt(0));
            tvshow.setTitle(resultSet.getString(1));
            tvshow.setYear(resultSet.getString(2));

            tvshow.setOverview(resultSet.getString(3));
            tvshow.setImgThumb(resultSet.getString(4));

            tvShows.add(tvshow);
        }

        return tvShows;
    }

    public Integer removeShowFromList (String listName, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(listName, "id = ? ", new String[] { Integer.toString(id) });
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    private String getTable(int listIndex) {
        switch(listIndex) {
            case 0:  return TABLE1;
            case 1:  return TABLE2;
            case 2:  return TABLE3;
            case 3:  return TABLE4;
            case 4:  return TABLE5;
        }
        return "ERROR";
    }
}
