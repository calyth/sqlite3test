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
            String sql = "INSERT INTO " + CustomerSQLiteHelper.TABLE_CUSTOMERS +
                    " VALUES (NULL,?);";
            SQLiteDatabase db = db_helper.getWritableDatabase();
            SQLiteStatement stmt = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < mRows; i++)
            {
                stmt.clearBindings();
                stmt.bindString(1, String.format("Customer %d", i));
                stmt.execute();
                if (i % 10 == 0)
                {
                    publishProgress((long) i);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
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
