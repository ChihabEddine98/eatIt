package com.chihab_eddine98.eatit.viewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.chihab_eddine98.eatit.R;
import com.chihab_eddine98.eatit.interfaces.ItemClickListener;
import com.chihab_eddine98.eatit.model.Order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

 class CartVH extends RecyclerView.ViewHolder implements View.OnClickListener {



    public TextView cart_item_nom,cart_item_prix;
    public ImageView cart_item_qte;
    private ItemClickListener itemClickListener;


    public CartVH(@NonNull View itemView) {
        super(itemView);

        cart_item_nom=itemView.findViewById(R.id.cart_item_nom);
        cart_item_prix=itemView.findViewById(R.id.cart_item_prix);
        cart_item_qte=itemView.findViewById(R.id.cart_item_qte);

    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view,getLayoutPosition(),false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}


public class CartAdapter extends RecyclerView.Adapter<CartVH>
{

    private List<Order> orderList=new ArrayList<>();
    private Context context;


    public CartAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.cart_item,parent,false);
        return new CartVH(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull CartVH holder, int position) {

        TextDrawable drawable=TextDrawable.builder().buildRound(""+orderList.get(position).getQte(), Color.RED);

        holder.cart_item_qte.setImageDrawable(drawable);

        Locale locale=new Locale("fr","FRA");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);

        int prix=(Integer.parseInt(orderList.get(position).getPrix()))*(Integer.parseInt(orderList.get(position).getQte()));
        holder.cart_item_prix.setText(fmt.format(prix));

        holder.cart_item_nom.setText(orderList.get(position).getFoodName());

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
