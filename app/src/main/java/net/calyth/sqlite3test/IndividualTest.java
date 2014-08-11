package net.calyth.sqlite3test;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by calyth on 2014-08-10.
 */
public class IndividualTest implements View.OnClickListener{
    @Override
    public void onClick(View view)
    {
        Activity mainScreen = (Activity) view.getContext();
        final TextView results = (TextView) mainScreen.findViewById(R.id.test_result_box);
        results.setText("foo");
    }
}
