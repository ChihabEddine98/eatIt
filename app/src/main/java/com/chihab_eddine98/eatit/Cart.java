package com.chihab_eddine98.eatit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.chihab_eddine98.eatit.Database.Database;
import com.chihab_eddine98.eatit.model.Order;
import com.chihab_eddine98.eatit.viewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {


    FirebaseDatabase bdd;
    DatabaseReference table_order;

    RecyclerView recycler_cart;
    RecyclerView.LayoutManager layoutManager;
    CartAdapter adapter;


    TextView txtTotal;
    FButton btnPlaceOrder;

    List<Order> cart=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);



        // Firebase

        bdd=FirebaseDatabase.getInstance();
        table_order=bdd.getReference("Order");

        // Composants graphiques

        recycler_cart=findViewById(R.id.recycler_cart);
        recycler_cart.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(getBaseContext());

        recycler_cart.setLayoutManager(layoutManager);



        txtTotal=findViewById(R.id.txtTotal);
        btnPlaceOrder=findViewById(R.id.btnPlaceOrder);



        loadCart();






    }

    private void loadCart() {

        cart=new Database(getBaseContext()).getCart();
        adapter=new CartAdapter(cart,this);

        recycler_cart.setAdapter(adapter);

        int total=0;


        for (Order order:cart)
        {
            total+=Integer.parseInt(order.getPrix())*Integer.parseInt(order.getQte());

        }

        Locale locale=new Locale("fr","FRA");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);

        txtTotal.setText(fmt.format(total));


    }
}
