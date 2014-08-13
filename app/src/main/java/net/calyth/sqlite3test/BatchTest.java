package net.calyth.sqlite3test;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by calyth on 2014-08-10.
 */
public class BatchTest implements View.OnClickListener{
    TextView mResults = null;
    Long mRows;
    @Override
    public void onClick(View view)
    {
        Activity mainScreen = (Activity) view.getContext();
        mResults = (TextView) mainScreen.findViewById(R.id.test_result_box);
        EditText rows_box = (EditText) mainScreen.findViewById(R.id.field_rows);
        mRows = new Long(rows_box.getText().toString());
        new BatchTestTask(mainScreen).execute();
    }
    private class BatchTestTask extends AsyncTask<Void, Long, String> {
        private final Context mContext;

        public BatchTestTask(Context context)
        {
            mContext = context;
        }
        @Override
        protected String doInBackground(Void... voids) {
            CustomerSQLiteHelper db_helper = new CustomerSQLiteHelper(mContext);
            db_helper.initialize();
            long startTime = System.currentTimeMillis();
            StringBuilder sql_builder = new StringBuilder("INSERT INTO " + CustomerSQLiteHelper.TABLE_CUSTOMERS +
                    " (id, customer) VALUES ");
            for (int i = 0; i < 24; i++)
            {
                sql_builder.append("(NULL, ?), ");
            }
            sql_builder.append("(NULL, ?)");
            String sql = sql_builder.toString();
            SQLiteDatabase db = db_helper.getWritableDatabase();
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.clearBindings();
            db.beginTransaction();
            for (int i = 0; i < mRows; i++)
            {

                stmt.bindString((i % 25) + 1, String.format("Customer %d", i));
//                stmt.execute();
                if (i % 25 == 0)
                {
                    stmt.execute();
                    publishProgress((long) i);
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    db.beginTransaction();
                    stmt.clearBindings();
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            long diff = System.currentTimeMillis() - startTime;
            return Double.toString((double) mRows * 1000 / diff);
        }
        @Override
        protected void onProgressUpdate(Long... row)
        {
            mResults.setText("Row " + row[0].toString());
        }
        @Override
        protected void onPostExecute(String result)
        {
            mResults.setText("Inserting " + result + " rows per second");
        }
    }
}
