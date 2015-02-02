package com.adapp.gridimagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adapp.gridimagesearch.R;
import com.adapp.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by abhidhar on 1/29/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {


    public ImageResultsAdapter(Context context, List<ImageResult> objects) {
        //super(context, android.R.layout.simple_list_item_1, objects);

        super(context, R.layout.item_image_result, objects);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        ImageResult image = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
        }


        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

        // clear out image from last time
        ivImage.setImageResource(0);

        tvTitle.setText(Html.fromHtml(image.title));
        Picasso.with(getContext()).load(image.thumbUrl).into(ivImage);

        return convertView;

    }
}
