package com.chihab_eddine98.eatit.controllers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chihab_eddine98.eatit.R;
import com.chihab_eddine98.eatit.database.Database;
import com.chihab_eddine98.eatit.common.Common;
import com.chihab_eddine98.eatit.model.FoodOrder;
import com.chihab_eddine98.eatit.model.Order;
import com.chihab_eddine98.eatit.viewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

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

    List<FoodOrder> cart=new ArrayList<>();
    double total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);



        // Firebase

        bdd=FirebaseDatabase.getInstance();
        table_order=bdd.getReference("FoodOrder");

        // Composants graphiques

        recycler_cart=findViewById(R.id.recycler_cart);
        recycler_cart.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(getBaseContext());

        recycler_cart.setLayoutManager(layoutManager);



        txtTotal=findViewById(R.id.txtTotal);
        btnPlaceOrder=findViewById(R.id.btnPlaceOrder);

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                afficheAdresseAlert(getBaseContext(),"Plus qu'une étape !","Entrez Votre Adresse",R.drawable.ic_shopping_cart_black_24dp);
            }
        });



        loadCart();






    }


    private void afficheAdresseAlert(final Context context, String titre, String msg, int icon) {

        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(Cart.this);

        alertDialog.setTitle(titre);
        alertDialog.setMessage(msg);
        alertDialog.setIcon(icon);


        final MaterialEditText editText=new MaterialEditText(Cart.this);
        editText.setPaddings(10,10,10,10);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // On crée une nouvelle commande ( avec le client l'adresse et les produits )
                String adresse=editText.getText().toString();

                Order order=new Order(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getNomComplet(),
                        adresse,
                        String.format("%.2f", total),
                        cart
                );


                // Save à Firebase
                table_order.child(String.valueOf(System.currentTimeMillis())).setValue(order);
                // Delete le panier
                new Database(context).cleanCart();

                Toast.makeText(Cart.this," Merci l'enregistrement de votre commande est bien déroulé !",Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        alertDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private void loadCart() {

        cart=new Database(getBaseContext()).getCart();
        adapter=new CartAdapter(cart,this);

        recycler_cart.setAdapter(adapter);

       total=0.0;


        for (FoodOrder order:cart)
        {
            total+=Double.parseDouble(order.getPrix())*Double.parseDouble(order.getQte());
        }

        Locale locale=new Locale("fr","FR");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);

        txtTotal.setText(fmt.format(total));


    }
}
