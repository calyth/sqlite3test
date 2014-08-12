package net.calyth.sqlite3test;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by calyth on 2014-08-11.
 */
public class CustomerSQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "customer.db";
    public static final String TABLE_CUSTOMERS = "customers";
    private static final String KEY_ID = "id";
    private static final String KEY_CUSTOMER = "customer";

    public CustomerSQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CUSTOMER_TABLE = "CREATE TABLE customers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "customer TEXT )";
        sqLiteDatabase.execSQL(CREATE_CUSTOMER_TABLE);
    }

    @Override
    public void onConfigure(SQLiteDatabase db)
    {
        db.enableWriteAheadLogging();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS customers");
        this.onCreate(sqLiteDatabase);
    }

    public void initialize(){
        SQLiteDatabase db = this.getWritableDatabase();
        this.onUpgrade(db, 0, 0);
    }

    public void addCustomer(String customer)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        CustomerSQLiteHelper.addCustomer(db, customer);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    static public void addCustomer(SQLiteDatabase db, String customer)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER, customer);
        db.insert(TABLE_CUSTOMERS, null, values);
    }
}
