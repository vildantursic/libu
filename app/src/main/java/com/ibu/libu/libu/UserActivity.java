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

    String[] allBooks = {"..."};

    String name;
    String id;
    String auth;

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
        final TextView txtTesting = (TextView)findViewById(R.id.txtTesting);
        final ImageView imgUser = (ImageView)findViewById(R.id.imgUser);

        listOfTakenBooks = (ListView)findViewById(R.id.listOfTakenBooks);

        Firebase.setAndroidContext(this);

        final Firebase fireUser = new Firebase("https://libu.firebaseio.com/");

        auth = fireUser.getAuth().getProviderData().get("email").toString();
        auth = auth.substring(0,auth.indexOf("@"));

        fireUser.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                resID = getResources().getIdentifier(auth, "drawable", getPackageName());
                imgUser.setImageResource(resID);
                name = snapshot.child(auth+"/name").getValue().toString();
                id = snapshot.child(auth+"/id").getValue().toString();

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

                String test = "";

                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.child("borrowing/" + auth).getValue() != null) {
                        dt += "," + child.child("borrowing/" + auth).getValue();
                        //test = child.child("borowing/" + auth).getParent().toString();
                    }
                }

                txtTesting.setText(test);

                allBooks = dt.split(",");

                listing();
            }

            @Override
            public void onCancelled(FirebaseError error) { }
        });

        listing();

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
