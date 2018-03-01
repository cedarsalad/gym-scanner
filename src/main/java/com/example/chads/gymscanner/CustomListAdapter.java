package com.example.chads.gymscanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by chads on 2018-03-01.
 */

public class CustomListAdapter extends ArrayAdapter<Member> {

    public CustomListAdapter(AllMembersActivity context, ArrayList<Member> members){
        super(context,0,members);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Member member = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
        }
        // Lookup view for data population
        ImageView avatar = (ImageView) convertView.findViewById(R.id.imgAvatar);
        TextView txtFirstName = (TextView) convertView.findViewById(R.id.etxtFirstName);
        TextView txtLastName = (TextView) convertView.findViewById(R.id.txtLastName);
        TextView txtAddress = (TextView) convertView.findViewById(R.id.txtAdress);
        TextView txtCity = (TextView) convertView.findViewById(R.id.txtCity);
        TextView txtProvince = (TextView) convertView.findViewById(R.id.txtProvince);
        TextView txtCode = (TextView) convertView.findViewById(R.id.txtCode);
        TextView txtVisits= (TextView) convertView.findViewById(R.id.txtVisits);
        // Populate the data into the template view using the data object
        File imgFile = new File(member.getAvatar());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            avatar.setImageBitmap(myBitmap);
        }
        txtFirstName.setText(member.getFirstName());
        txtLastName.setText(member.getLastName());
        txtAddress.setText(member.getAddress());
        txtCity.setText(member.getCity());
        txtVisits.setText("Visits: "+String.valueOf(member.getVisits()));
        txtProvince.setText(member.getProvince());
        txtCode.setText("#"+member.getBarcode());

        // Return the completed view to render on screen
        return convertView;
    }


}
