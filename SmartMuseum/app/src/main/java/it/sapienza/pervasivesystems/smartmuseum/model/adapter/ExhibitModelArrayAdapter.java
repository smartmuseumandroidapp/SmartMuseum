package it.sapienza.pervasivesystems.smartmuseum.model.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.view.DetailOfExhibitActivity;
import it.sapienza.pervasivesystems.smartmuseum.view.ListOfExhibitsActivity;

/**
 * Created by Guamaral on 5/1/2016.
 */
public class ExhibitModelArrayAdapter extends ArrayAdapter<ExhibitModel> {

    Context context;
    int layoutResourceId;
    ArrayList<ExhibitModel> exhibitModels = new ArrayList<ExhibitModel>();

    public ExhibitModelArrayAdapter(Context context, int layoutResourceId, ArrayList<ExhibitModel> exhibitModelsNew) {

        super(context, layoutResourceId, exhibitModelsNew);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.exhibitModels = exhibitModelsNew;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View item = convertView;
        ExhibitWrapper exhibitWrapper = null;

        if (item == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            item = inflater.inflate(layoutResourceId, parent, false);

            exhibitWrapper = new ExhibitWrapper();
            exhibitWrapper.image = (ImageView) item.findViewById(R.id.exh_image);
            exhibitWrapper.title = (TextView) item.findViewById(R.id.exh_title);
            exhibitWrapper.shortDesc = (TextView) item.findViewById(R.id.exh_short_desc);
            exhibitWrapper.exhDate = (TextView) item.findViewById(R.id.exh_date);
            exhibitWrapper.detailBtn = (Button) item.findViewById(R.id.detailBtn);
            exhibitWrapper.objectBtn = (Button) item.findViewById(R.id.objectBtn);

            item.setTag(exhibitWrapper);

        } else {
            exhibitWrapper = (ExhibitWrapper) item.getTag();
        }

        final ExhibitModel exhibitModel = exhibitModels.get(position);

        Picasso.with(context).load(exhibitModel.getImage()).into(exhibitWrapper.image);
        exhibitWrapper.title.setText(exhibitModel.getTitle());
        exhibitWrapper.shortDesc.setText(exhibitModel.getShortDescription());
        exhibitWrapper.exhDate.setText(exhibitModel.getPeriod());

        exhibitWrapper.objectBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, exhibitModel.getTitle(), Toast.LENGTH_LONG).show();
            }

        });
        exhibitWrapper.detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, DetailOfExhibitActivity.class);
//                startActivity(intent);
            }

        });

        return item;

    }

    /**
     * Wrapper class that has to fill the exhibition items
     */
    static class ExhibitWrapper {

        ImageView image;
        TextView title;
        TextView shortDesc;
        TextView exhDate;

        Button detailBtn;
        Button objectBtn;
    }

}