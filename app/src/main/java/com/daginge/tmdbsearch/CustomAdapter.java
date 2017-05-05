package com.daginge.tmdbsearch;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by princes on 03-May-17.
 */
public class CustomAdapter extends ArrayAdapter<MovieResult> implements View.OnClickListener{

    private ArrayList<MovieResult> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
       // TextView txtVersion;
        ImageView info;
    }

    public CustomAdapter(ArrayList<MovieResult> data, Context context) {
        super(context, R.layout.album_card, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        MovieResult dataModel=(MovieResult) object;

        switch (v.getId())
        {
            case R.id.item_info:

                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
       MovieResult dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.album_card, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.id);
           // viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.backdroppath);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.thumbnail);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        lastPosition = position;
        String TMDB_API_KEY = "ef68bfed72780ce7ae801b9daba23069";
     String DEBUG_TAG = "TMDBQueryManager";
        viewHolder.txtName.setText(dataModel.getTitle());
        viewHolder.txtType.setText(dataModel.getPosterPath());
        //String p="https://api.themoviedb.org/3/movie/"+dataModel.getId()+"/images?api_key="+TMDB_API_KEY+"&language=en-US";
       // Log.e("dsfs",p);
        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w640/"+dataModel.getBackdropPath()).fit().into(viewHolder.info);
        Log.e("dsf","https://image.tmdb.org/t/p/w640/"+dataModel.getBackdropPath());
        //viewHolder.info.setImageResource(R.drawable.ic_action_search);


        // Return the completed view to render on screen
        return convertView;
    }
}
