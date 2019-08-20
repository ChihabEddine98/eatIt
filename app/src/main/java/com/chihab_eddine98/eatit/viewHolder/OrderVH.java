package com.chihab_eddine98.eatit.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chihab_eddine98.eatit.R;
import com.chihab_eddine98.eatit.interfaces.ItemClickListener;

public class OrderVH extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView order_item_id,order_item_status,order_item_nomComplet,order_item_phone,order_item_adresse;
    public LinearLayout status_layout;
    public ImageView order_item_status_img;
    private ItemClickListener itemClickListener;





    public OrderVH(@NonNull View itemView) {
        super(itemView);


        order_item_id=itemView.findViewById(R.id.order_item_id);
        status_layout=itemView.findViewById(R.id.status_layout);
        order_item_status=itemView.findViewById(R.id.order_item_status);
        order_item_status_img=itemView.findViewById(R.id.order_item_status_img);
        order_item_nomComplet=itemView.findViewById(R.id.order_item_nomComplet);
        order_item_phone=itemView.findViewById(R.id.order_item_phone);
        order_item_adresse=itemView.findViewById(R.id.order_item_adresse);

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
