package com.example.covidqr2;
import android.provider.BaseColumns;
public class MyCovidEntry {
    private MyCovidEntry() {
    }
    public static final class CovidEntry implements BaseColumns {
        public static final String TABLE_NAME = "groceryList";
        public static final String COLUMN_NAME = "name";
        //public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_MYTIME = "mytime";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_CHECKED = "Checked";
    }
}