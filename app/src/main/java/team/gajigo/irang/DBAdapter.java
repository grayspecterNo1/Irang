package team.gajigo.irang;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//DB를 컨트롤하기 위한 클래스. (MainActivity에서 DB 컨트롤 위한 인스턴스 생성)
//클래스 내부에 DataBaseHelper 객체가 있어, 데이터베이스를 관리.
//DataBaseHelper에는 3가지 메소드가 존재 - 생성자, onCreate, onUpdate
public class DBAdapter {

    public static final String KEY_SNO = "sno";
    public static final String KEY_SNAME = "sname";
    public static final String KEY_SADDRESS = "saddress";
    public static final String KEY_STEL = "stel";
    public static final String KEY_SCATG = "scatg";
    public static final String KEY_SLAT = "slat";
    public static final String KEY_SLONG = "slong";
    public static final String KEY_TNO = "tno";
    public static final String KEY_TNAME = "tname";
    public static final String KEY_ROWID = "_id";
    private static final String TAG = "DBAdapter";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     *
     * Database creation sql statement
     */

    private static final String DATABASE_CREATE = "create table notes (_id integer primary key autoincrement, "
            + " tno text not null, tname text not null, sno text not null, sname text not null," +
            "saddress text not null, stel text not null, scatg text not null," +
            "slat text not null, slong text not null);";
    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "notes";
    private static final int DATABASE_VERSION = 2;
    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //table 생성.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
            Log.w(TAG, "CREATING database=====================================");
        }

        //upgrade 시 다시 생성.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    public DBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public DBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
//        mDbHelper.onUpgrade(mDb, 2, 3);
        return this;
    }

    public DBAdapter reset() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        mDbHelper.onUpgrade(mDb, 2, 3);
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    //쿼리문 없이 데이터 조정
    //ContentValues 라는 타입을 이용하여 기존에 있는 테이블의 속성명과 조작하려는 인스턴스를 넣어 한꺼번에 데이터베이스로 요청
    public long createNote(String tno, String tname, String sno, String sname, String saddress, String stel,
                           String scatg, String slat, String slong ) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TNO, tno);
        initialValues.put(KEY_TNAME, tname);
        initialValues.put(KEY_SNO, sno);
        initialValues.put(KEY_SNAME, sname);
        initialValues.put(KEY_SADDRESS, saddress);
        initialValues.put(KEY_STEL, stel);
        initialValues.put(KEY_SCATG, scatg);
        initialValues.put(KEY_SLAT, slat);
        initialValues.put(KEY_SLONG, slong);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteAll(){
        Log.i("Delete called", "value__");
        return mDb.delete(DATABASE_TABLE, null, null) > 0;
    }


    // 데이터베이스의 특성상 하나의 테이블의 레코드를 읽어 오기 위해서는 커서라는 것이 필요
    // 조건에 맞는 레코드를 한꺼번에 모두 들고 올 수가 없기 때문에 커서를 이용해 조작
    // 커서: 현재 레코드를 가리키고 있는 곳. 커서 객체를 이용하여 get.

    //select문이라 생각해. selection에 where 조건문 붙임. null로 바꿔주면 조건 없이 가져옴(현재 코드는 tno가 2인 값만 가져오는 것
    public Cursor fetchAllNotes(String tcatg) {
        return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TNO, KEY_TNAME, KEY_SNO, KEY_SNAME, KEY_SADDRESS,
                        KEY_STEL, KEY_SCATG, KEY_SLAT, KEY_SLONG},
                "tno = ?", new String[] {tcatg}, null, null, null);
    }

    public Cursor fetchRowId() {
        return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TNO, KEY_TNAME}, null, null,
                null, null, null);
    }

    public boolean deleteRowId(){
        return mDb.delete(DATABASE_TABLE, KEY_ROWID,null) > 0;
    }

}
