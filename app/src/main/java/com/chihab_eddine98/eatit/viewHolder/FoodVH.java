package com.chihab_eddine98.eatit.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chihab_eddine98.eatit.R;
import com.chihab_eddine98.eatit.interfaces.ItemClickListener;

public class FoodVH extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView food_nom;
    public ImageView food_img,fav_img;
    private ItemClickListener itemClickListener;

    public FoodVH(@NonNull View itemView) {
        super(itemView);

        food_nom=itemView.findViewById(R.id.food_nom);
        food_img=itemView.findViewById(R.id.food_img);
        fav_img=itemView.findViewById(R.id.fav_img);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
