package com.chihab_eddine98.eatit.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chihab_eddine98.eatit.R;
import com.chihab_eddine98.eatit.interfaces.ItemClickListener;

public class CategoryVH extends RecyclerView.ViewHolder implements View.OnClickListener {


    public  TextView category_nom;
    public  ImageView category_img;
    private ItemClickListener itemClickListener;


    public CategoryVH(@NonNull View itemView) {
        super(itemView);

        category_nom=(TextView)  itemView.findViewById(R.id.category_nom);
        category_img=(ImageView) itemView.findViewById(R.id.category_img);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}
