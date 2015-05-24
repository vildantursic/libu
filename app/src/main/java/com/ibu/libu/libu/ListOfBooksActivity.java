package com.ibu.libu.libu;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;


public class ListOfBooksActivity extends ActionBarActivity {

    String[] allBooks = {"riot","whitewolf"};

    String dt;
    int i = 0;

    ListView listOfBooks;
    ListAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_books);

        final TextView fireData = (TextView)findViewById(R.id.txtData);

        /* Firebase config */
        Firebase.setAndroidContext(this);
        Firebase libu = new Firebase("https://libu.firebaseio.com/");
        /**************/

        listOfBooks = (ListView)findViewById(R.id.listOfBooks);

        bookAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allBooks);

        listOfBooks.setAdapter(bookAdapter);

        libu.child("book").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                allBooks = new String[]{};
                dt = "";

                for (DataSnapshot child : snapshot.getChildren()){
                    dt += "," + child.getKey();
                    allBooks = dt.split(",");
                }

                listOfBooks.invalidateViews();

                fireData.setText(((Integer) allBooks.length).toString());
            }
            @Override public void onCancelled(FirebaseError error) { }
        });



        listOfBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String book = String.valueOf(parent.getItemAtPosition(position));

                Intent intent = new Intent(ListOfBooksActivity.this, BookDetailsActivity.class);

                intent.putExtra("name", book);

                Bundle bookData = new Bundle();
                bookData.putString("data", "Book details");
                intent.putExtras(bookData);

                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_of_books, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_settings:
                Intent intent = new Intent(ListOfBooksActivity.this, UserActivity.class);
                startActivity(intent);

                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
