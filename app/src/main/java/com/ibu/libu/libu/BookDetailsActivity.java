package com.ibu.libu.libu;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class BookDetailsActivity extends ActionBarActivity {

    String message;

    String name;
    String rented;
    String year;

    int resID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        final TextView txtBookName = (TextView)findViewById(R.id.txtBookName);
        final TextView txtBookRented = (TextView)findViewById(R.id.txtBookRented);
        final TextView txtBookYear = (TextView)findViewById(R.id.txtBookYear);
        ImageView imgBookCover = (ImageView)findViewById(R.id.imgBookCover);

        Firebase.setAndroidContext(this);

        Firebase libu = new Firebase("https://libu.firebaseio.com/");

        Intent intent = getIntent();

        // 2. get message value from intent
        message = intent.getStringExtra("name");

        resID = getResources().getIdentifier(message, "drawable", getPackageName());
        imgBookCover.setImageResource(resID);

        libu.child("book").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                name = snapshot.child(message).child("name").getValue().toString();
                rented = snapshot.child(message).child("rented").getValue().toString();
                year = snapshot.child(message).child("year").getValue().toString();


                txtBookName.setText(name);
                txtBookRented.setText(rented);
                txtBookYear.setText(year);

            }
            @Override public void onCancelled(FirebaseError error) { }
        });

        // 4. get bundle from intent
        Bundle bundle = intent.getExtras();

        // 5. get status value from bundle
        String status = bundle.getString("data");

        // 6. show status on Toast
        Toast toast = Toast.makeText(this, status, Toast.LENGTH_LONG);
        toast.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
