package net.calyth.sqlite3test;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

/**
 * Created by calyth on 2014-08-10.
 */
public class BatchTest implements View.OnClickListener{
    TextView mResults = null;
    @Override
    public void onClick(View view)
    {
        Activity mainScreen = (Activity) view.getContext();
        mResults = (TextView) mainScreen.findViewById(R.id.test_result_box);
        new BatchTestTask().execute();
    }
    private class BatchTestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return "Batch";
        }
        @Override
        protected void onPostExecute(String result)
        {
            mResults.setText(result);
        }
    }
}
