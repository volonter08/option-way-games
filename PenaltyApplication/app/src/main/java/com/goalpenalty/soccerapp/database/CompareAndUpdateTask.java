package com.goalpenalty.soccerapp.database;

import android.content.Context;
import android.widget.Toast;

import com.goalpenalty.soccerapp.R;
import com.goalpenalty.soccerapp.ThisApp;


public class CompareAndUpdateTask extends AdvancedAsyncTask<Record, Void, Void> {

    Record record;
    boolean show;

    public CompareAndUpdateTask(Context ctx){
        super(ctx);
    }

    @Override
    protected Void doInBackground(Record... records){
        database = ThisApp.getInstance().getDatabase();
        recordDao = database.getRecordDao();

        record = records[0];
        Record old = recordDao.getById(record.id);

        if (record.value > old.value){
            show = true;
            recordDao.update(record);
        }

        return null;
    }

    @Override
    public void onTaskComplete(Void v){
        if (show){
            String s = ctx.getResources().getString(R.string.new_record_alert);
            if (record.id.equals(Keys.POINTS_MODE1))
                s += String.format(ctx.getResources().getString(R.string.n_points), record.value);
            if (record.id.equals(Keys.ROUNDS_MODE1))
                s += String.format(ctx.getResources().getString(R.string.n_round), record.value);
            Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPostExecute(Void par){
        super.onPostExecute(par);
    }

}
