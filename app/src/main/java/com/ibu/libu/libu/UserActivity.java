package com.ibu.libu.libu;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;


public class UserActivity extends ActionBarActivity {

    String[] allBooks = {"riot","whitewolf"};

    String name;
    String id;

    int resID;

    ListAdapter bookAdapter;
    String dt;

    ListView listOfTakenBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        final TextView txtName = (TextView)findViewById(R.id.txtName);
        final TextView txtId = (TextView)findViewById(R.id.txtId);
        final ImageView imgUser = (ImageView)findViewById(R.id.imgUser);
        final Button btnRefresh = (Button)findViewById(R.id.btnRefresh);

        listOfTakenBooks = (ListView)findViewById(R.id.listOfTakenBooks);

        Firebase.setAndroidContext(this);

        Firebase fireUser = new Firebase("https://libu.firebaseio.com/");

        fireUser.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                resID = getResources().getIdentifier("medinabandic", "drawable", getPackageName());
                imgUser.setImageResource(resID);
                name = snapshot.child("medinabandic/name").getValue().toString();
                id = snapshot.child("medinabandic/id").getValue().toString();

                txtName.setText(name);
                txtId.setText(id);
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });

        fireUser.child("book").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                allBooks = new String[]{};
                dt = "";

                for (DataSnapshot child : snapshot.getChildren()){
                    dt += "," + child.child("borrowing/medinabandic").getValue();
                }

                allBooks = dt.split(",");
               // if (new SimpleDateFormat("MM/yyyy").parse(date).before(new Date())) {}
            }
            @Override public void onCancelled(FirebaseError error) { }
        });

        listing();
        
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listing();
            }
        });
    }

    public void listing(){
        bookAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allBooks);

        listOfTakenBooks.setAdapter(bookAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
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
