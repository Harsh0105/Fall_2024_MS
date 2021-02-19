package com.example.covidqr2;
import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DbAdapter extends RecyclerView.Adapter<DbAdapter.DbViewHolder> {
    private Context mContext;
    public Cursor mCursor;
    public Cursor lCursor;

    public DbAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;

    }
    public class DbViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        //public TextView countText;
        public TextView timeText;
        public TextView checkText;
        public DbViewHolder(View itemView){
            super(itemView);
            nameText = itemView.findViewById(R.id.textview_name_item);
            //countText = itemView.findViewById(R.id.textview_amount_item);
            timeText = itemView.findViewById(R.id.textview_time_item);
            checkText = itemView.findViewById(R.id.textview_check_item);

        }
    }
    @Override
    public DbViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.log_item, parent, false);
        return new DbViewHolder(view);
    }
    @Override
    public void onBindViewHolder(DbViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String name = mCursor.getString(mCursor.getColumnIndex(MyCovidEntry.CovidEntry.COLUMN_NAME));
        //int amount = mCursor.getInt(mCursor.getColumnIndex(MyCovidEntry.CovidEntry.COLUMN_AMOUNT));
        String time = mCursor.getString(mCursor.getColumnIndex(MyCovidEntry.CovidEntry.COLUMN_MYTIME));
        String check = mCursor.getString(mCursor.getColumnIndex(MyCovidEntry.CovidEntry.COLUMN_CHECKED));
        if(check.equals("Checked out")){
            holder.checkText.setBackgroundColor(Color.RED);
        }
        else if (check.equals("Checked in")){
            holder.checkText.setBackgroundColor(Color.GREEN);
        }

        holder.nameText.setText(name);
        //holder.countText.setText(String.valueOf(amount));
        holder.timeText.setText(time);
        holder.checkText.setText(check);


    }
    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();

        }
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
