package com.example.covidqr2;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private DbAdapter mAdapter;
    private EditText mEditTextName;
    public RadioButton mCheckIn;
    public RadioButton mCheckOut;
    DatabaseReference dbref;
    member Member;
    //private TextView mTextViewAmount;
    private TextView mTimeText;
    //private int mAmount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toast.makeText(MainActivity.this,"firebase connection success", Toast.LENGTH_SHORT).show();


        GroceryDBHelper dbHelper = new GroceryDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DbAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);
        mEditTextName = findViewById(R.id.edittext_name);
        mTimeText = findViewById(R.id.textview_time_item);
        mCheckIn = findViewById(R.id.CheckIn);
        mCheckOut = findViewById(R.id.CheckOut);

        Member = new member();
        dbref = FirebaseDatabase.getInstance().getReference().child("member");


        //mTextViewAmount = findViewById(R.id.textview_amount);
        //Button buttonIncrease = findViewById(R.id.button_increase);
        //Button buttonDecrease = findViewById(R.id.button_decrease);
        Button buttonAdd = findViewById(R.id.button_add);
//        buttonIncrease.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                increase();
//            }
//        });
//        buttonDecrease.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                decrease();
//            }
//        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
    }
//    private void increase() {
//        mAmount++;
//        mTextViewAmount.setText(String.valueOf(mAmount));
//    }
//    private void decrease() {
//        if (mAmount > 0) {
//            mAmount--;
//            mTextViewAmount.setText(String.valueOf(mAmount));
//        }
//    }
    private void addItem() {
        if (mEditTextName.getText().toString().trim().length() == 0 ) {
            return;
        }
        String date = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        String dateDay = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String name = mEditTextName.getText().toString();
        String status1 = "Checked in";
        String status2 = "Checked out";

        ContentValues cv = new ContentValues();
        if(mCheckIn.isChecked() == true){
            cv.put(MyCovidEntry.CovidEntry.COLUMN_CHECKED, "Checked in");

            Member.setBuildingName(name);
            Member.setCheckStatus(status1);
            Member.setDate(dateDay);
            Member.setPersonName("hema");
            dbref.push().setValue(Member);

            Toast.makeText(MainActivity.this, "data successfully added", LENGTH_SHORT).show();


        }else if(mCheckOut.isChecked() == true){
            cv.put(MyCovidEntry.CovidEntry.COLUMN_CHECKED, "Checked out");

            Member.setBuildingName(name);
            Member.setCheckStatus(status2);
            Member.setDate(dateDay);
            Member.setPersonName("hema");
            dbref.push().setValue(Member);

            Toast.makeText(MainActivity.this, "data successfully added", LENGTH_SHORT).show();

        }
        cv.put(MyCovidEntry.CovidEntry.COLUMN_NAME, name);
        //cv.put(MyCovidEntry.CovidEntry.COLUMN_AMOUNT, mAmount);
        cv.put(MyCovidEntry.CovidEntry.COLUMN_MYTIME, date);
        mDatabase.insert(MyCovidEntry.CovidEntry.TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllItems());
        mEditTextName.getText().clear();
    }
    private Cursor getAllItems() {
        return mDatabase.query(
                MyCovidEntry.CovidEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MyCovidEntry.CovidEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }
}