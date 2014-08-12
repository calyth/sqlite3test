package net.calyth.sqlite3test;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by calyth on 2014-08-10.
 */
public class IndividualTest implements View.OnClickListener{
    TextView mResults = null;
    Long mRows;
    @Override
    public void onClick(View view)
    {
        Activity mainScreen = (Activity) view.getContext();
        mResults = (TextView) mainScreen.findViewById(R.id.test_result_box);
        EditText rows_box = (EditText) mainScreen.findViewById(R.id.field_rows);
        mRows = new Long(rows_box.getText().toString());
        new IndividualTestTask(mainScreen).execute();
    }

    private class IndividualTestTask extends AsyncTask<Void, Long, String>{
        private Context mContext;
        public IndividualTestTask(Context context)
        {
            mContext = context;
        }
        @Override
        protected String doInBackground(Void... voids) {
            CustomerSQLiteHelper db_helper = new CustomerSQLiteHelper(mContext);
            db_helper.initialize();
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < mRows; i++)
            {
                db_helper.addCustomer("customer " + i);
                if(i % 10 == 0)
                {
                    publishProgress((long) i);
                }
            }
            long diff = System.currentTimeMillis() - startTime;
            return Double.toString((double) mRows * 1000 / diff);
        }

        @Override
        protected void onProgressUpdate(Long... row){
            mResults.setText("Row " + row[0].toString());
        }
        @Override
        protected void onPostExecute(String result)
        {
            mResults.setText("Inserting " + result + " rows per second");
        }
    }
}
