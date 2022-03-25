package com.androidproject.travelassistant.Utility;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import android.widget.SearchView;
import android.widget.TextView;

import com.androidproject.travelassistant.R;

public class StringCursorAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private SearchView searchView;

    public StringCursorAdapter(Context context, Cursor cursor, SearchView sv) {
        super(context, cursor, false);
        mContext = context;
        searchView = sv;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.support_simple_spinner_dropdown_item, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        String s = cursor.getString(cursor.getColumnIndexOrThrow("addr"));

        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setText(s);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //take next action based user selected item
                TextView stv = (TextView) view.findViewById(android.R.id.text1);
                searchView.setQuery(stv.getText().toString(), false);
            }
        });

    }
}
