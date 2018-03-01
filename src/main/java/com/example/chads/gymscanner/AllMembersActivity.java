package com.example.chads.gymscanner;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AllMembersActivity extends AppCompatActivity {
    CustomListAdapter adapter;
    DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_members);

        //Construct the data source
        db = new DBManager(this);
        final ArrayList<Member> members = db.getAllMembers();

        //Create Adapter to convert the array to views
        adapter = new CustomListAdapter(AllMembersActivity.this, members);

//        adapter.add(new Member(0,"R.drawable.avatar", "Nathaniel", "Reid", "Dec 18, 1994", 23, "4045 rue Barbeau", "St-hubert", "QC", "1234567890", 0, true));
//        adapter.add(new Member(0,"R.drawable.avatar", "Nathaniel", "Reid", "Dec 18, 1994", 23, "4045 rue Barbeau", "St-hubert", "QC", "1234567890", 0, true));
//        adapter.add(new Member(0,"R.drawable.avatar", "Nathaniel", "Reid", "Dec 18, 1994", 23, "4045 rue Barbeau", "St-hubert", "QC", "1234567890", 0, true));
//        adapter.add(new Member(0,"R.drawable.avatar2", "Nathaniel", "Reid", "Dec 18, 1994", 23, "4045 rue Barbeau", "St-hubert", "QC", "1234567890", 0, true));

        //Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listViewAllMembers);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Member member = adapter.getItem(i);
                Toast.makeText(getApplicationContext(), i + " : " + member.getId() + " - " + member.getFirstName() + " - " + member.getLastName() + " - " + member.getDob() + " - " + member.getAge() + " - " + member.getAddress() + " - " + member.getCity() + " - " + member.getProvince() + " - " + member.getBarcode(),Toast.LENGTH_LONG).show();

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                AlertDialog.Builder deleteConfirmation = new AlertDialog.Builder(AllMembersActivity.this);
                deleteConfirmation.setTitle("Comfirm Delete");
                deleteConfirmation.setMessage("Are you sure you would like to delete this member?");


                // Set YES button delete
                deleteConfirmation.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int x) {
                        db.deleteMember(members.get(i));
                        adapter.remove(adapter.getItem(i));
                        adapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                // Set Cancel Button
                deleteConfirmation.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                deleteConfirmation.show();

                return false;
            }
        });


    }
}
