package com.chihab_eddine98.eatit.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.chihab_eddine98.eatit.R;
import com.chihab_eddine98.eatit.database.Database;
import com.chihab_eddine98.eatit.model.Food;
import com.chihab_eddine98.eatit.model.FoodOrder;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {



    TextView food_nom,food_description,food_prix;
    ImageView food_img;

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton btnQte;

    String foodId="";

    FirebaseDatabase bdd;
    DatabaseReference table_food;

    Food currentFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);


        // Database

        bdd=FirebaseDatabase.getInstance();
        table_food=bdd.getReference("Food");

        // Intialiser les components graphiques

        btnCart=findViewById(R.id.btnCart);
        btnQte=findViewById(R.id.btnQte);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodOrder order=new FoodOrder(foodId,
                        currentFood.getNom(),
                        btnQte.getNumber(),
                        currentFood.getPrix(),
                        currentFood.getReduction());

                new Database(getBaseContext()).addToCart(order);

                Toast.makeText(getBaseContext()," Ajouté au panier !",Toast.LENGTH_SHORT).show();
            }
        });


        food_nom=findViewById(R.id.food_nom);
        food_description=findViewById(R.id.food_description);
        food_prix=findViewById(R.id.food_prix);
        food_img=findViewById(R.id.food_img);

        collapsingToolbarLayout=findViewById(R.id.collapsing);

        if (getIntent()!=null)
        {
            foodId=getIntent().getStringExtra("foodId");
        }
        if(!foodId.isEmpty())
        {
            getFoodDetail(foodId);
        }




    }

    private void getFoodDetail(String foodId) {

        table_food.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentFood=dataSnapshot.getValue(Food.class);
                // Binding information image,nom,prix,description

                // Image
                Picasso.with(getBaseContext()).load(currentFood.getImgUrl())
                        .into(food_img);

                food_nom.setText(currentFood.getNom());
                food_prix.setText(currentFood.getPrix()+" €");
                food_description.setText(currentFood.getDescription());
                collapsingToolbarLayout.setTitle("Pizza/"+currentFood.getNom());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
