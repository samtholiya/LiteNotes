package com.trak.samtholiya.litenotes.dummy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.RecyclerView;


import com.trak.samtholiya.litenotes.LiteBookRecyclerViewAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MainContent extends SQLiteOpenHelper{

    /**
     * An array of sample (dummy) items.
     */
    private  List<MainItem> ITEMS = new ArrayList<MainItem>();
    private RecyclerView recyclerView;
    private LiteBookRecyclerViewAdapter liteBookAdapter;

    public  List<MainItem> getITEMS() {
        return ITEMS;
    }

    public  void setITEMS(List<MainItem> ITEMS) {
        this.ITEMS = ITEMS;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public LiteBookRecyclerViewAdapter getLiteBookAdapter() {
        return liteBookAdapter;
    }

    public void setLiteBookAdapter(LiteBookRecyclerViewAdapter liteBookAdapter) {
        this.liteBookAdapter = liteBookAdapter;
    }

    /**
     * A map of sample (dummy) items, by ID.
     */
   // public static final Map<String, MainItem> ITEM_MAP = new HashMap<String, MainItem>();

    private static final String TABLE_NAME = "comments";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_TITLE = "TITLE";
    private static final String COLUMN_CONTENT = "CONTENT";
    private static final String DATABASE_NAME = "NOTES1.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_NAME + "( " + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE
            + " TEXT NOT NULL, "
            + COLUMN_CONTENT
            + " TEXT);";
    
    /*
    private static final int COUNT = 25;*/

    public MainContent(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setITEMS(getDataList());
    }

    /*private static void addItem(MainItem item) {
      //  ITEMS.add(item);
     //   ITEM_MAP.put(item.id, item);
    }*/
/*
    private static MainItem createDummyItem(int position) {
        return new MainItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }*/

  /*  private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private List<MainItem> getDataList(){
        SQLiteDatabase db=getWritableDatabase();
        Cursor result;
        result = db.rawQuery("SELECT * FROM "+ TABLE_NAME,null);

        List<MainItem> item=new ArrayList<>();
        for (int i=0;i<result.getCount();i++){
            result.moveToNext();
            item.add(new MainItem(result.getString(0),result.getString(1),result.getString(2)));
        }
        result.close();
        return item;
    }

    public MainItem getLastItem(){
        SQLiteDatabase db=getWritableDatabase();
        Cursor result;
        MainItem item=null;
        result = db.rawQuery("SELECT * FROM "+ TABLE_NAME+" WHERE   "+COLUMN_ID+" = (SELECT MAX("+COLUMN_ID+")  FROM "+TABLE_NAME+")",null);
        if(result.getCount()!=0) {
            result.moveToNext();
            item=new MainItem(result.getString(0),result.getString(1),result.getString(2));
        }
        return item;
    }
    public boolean insertData(MainItem item)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues content =new ContentValues();
        content.put(COLUMN_TITLE,item.title);
        content.put(COLUMN_CONTENT,item.details);
        long done=db.insert(TABLE_NAME,null,content);
        item=getLastItem();
        ITEMS.add(item);
        return done != -1;
    }

    public boolean updateData(MainItem item) {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues content =new ContentValues();
        content.put(COLUMN_TITLE,item.title);
        content.put(COLUMN_CONTENT,item.details);
        long done=db.update(TABLE_NAME,content,COLUMN_ID+"= ?",new String[]{item.id});
        ITEMS.set(ITEMS.indexOf(item),item);
        return done != 0;
    }

    public boolean deleteData(int position) {
        SQLiteDatabase db=getWritableDatabase();
        int count=db.delete(TABLE_NAME,COLUMN_ID+" = ?",new String[]{ITEMS.get(position).id});
        ITEMS.remove(position);
        return count != 0;
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class MainItem implements Serializable {
        public String id;
        public String title;
        public String details;
        public List<String> contentList;
        public MainItem(String id, String title, String details) {
            this.id = id;
            this.title = title;
            this.details = details;
            this.contentList=new ArrayList<>();
        }
        public MainItem(){

        }

        @Override
        public boolean equals(Object obj) {
            MainItem item=null;
            if(obj instanceof MainItem) {
                item = (MainItem) obj;
                return item.id.equals(this.id);
            }
            return false;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
