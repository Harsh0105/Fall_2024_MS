package com.example.covidqr2;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
    public String newlocation;

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
//        mEditTextName = findViewById(R.id.edittext_name);
        mTimeText = findViewById(R.id.textview_time_item);
//        mCheckIn = findViewById(R.id.CheckIn);
//        mCheckOut = findViewById(R.id.CheckOut);

        Member = new member();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        dbref = FirebaseDatabase.getInstance().getReference().child("member");
        dbref.keepSynced(true);

        //mTextViewAmount = findViewById(R.id.textview_amount);
        //Button buttonIncrease = findViewById(R.id.button_increase);
        //Button buttonDecrease = findViewById(R.id.button_decrease);
        Button buttonAdd = findViewById(R.id.button_add);
        final Activity activity = this;

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
            public void onClick(View v) { IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        Toast.makeText(this,result.getContents(),Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        newlocation = String.valueOf(result.getContents());
        addItem();

    }


    private void addItem() {
        String status1 = "Checked in";
        String status2 = "Checked out";
        String oldLocation;
        String oldStatus;
        String newLocation = newlocation;
        if(mAdapter.mCursor.getCount() == 0){
            oldLocation = "empty";
            oldStatus = "empty";
        }
        else{
        mAdapter.mCursor.moveToFirst();
        oldLocation = mAdapter.mCursor.getString(mAdapter.mCursor.getColumnIndex(MyCovidEntry.CovidEntry.COLUMN_NAME));

       // System.out.println(oldLocation);
        oldStatus = mAdapter.mCursor.getString(mAdapter.mCursor.getColumnIndex(MyCovidEntry.CovidEntry.COLUMN_CHECKED));
        //System.out.println(oldStatus);
          }
        if (oldStatus.equals(status2) || oldStatus.equals("empty")){
            localUpdate(status1, newLocation);
            serverUpdate(status1,newLocation);

        }
        else if(oldStatus.equals(status1)){
            if(newLocation.equals(oldLocation)){
                localUpdate(status2, oldLocation);
                serverUpdate(status2,oldLocation);

            }
            else if (newLocation.equals(oldLocation) == false){
                localUpdate(status1, newLocation);
                serverUpdate(status1,newLocation);
                localUpdate(status2, oldLocation);
                serverUpdate(status2,oldLocation);


            }
        }


    }
    protected void serverUpdate(String status, String location){
        String name = location;
        String dateDay = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        Member.setBuildingName(name);
        Member.setCheckStatus(status);
        Member.setDate(dateDay);
        Member.setPersonName("hema");
        dbref.push().setValue(Member);
        Toast.makeText(MainActivity.this, "data successfully added", LENGTH_SHORT).show();

    }
    protected void localUpdate(String status, String location){
        String name = location;
        String date = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        ContentValues cv = new ContentValues();
        cv.put(MyCovidEntry.CovidEntry.COLUMN_CHECKED, status);
        cv.put(MyCovidEntry.CovidEntry.COLUMN_NAME, name);
        cv.put(MyCovidEntry.CovidEntry.COLUMN_MYTIME, date);
        mDatabase.insert(MyCovidEntry.CovidEntry.TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllItems());

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