package com.chihab_eddine98.eatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.chihab_eddine98.eatit.model.Food;
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
    Toolbar toolbar;
    FloatingActionButton btnCart;
    ElegantNumberButton btnQte;

    String foodId="";

    FirebaseDatabase bdd;
    DatabaseReference table_food;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);


        // Database

        bdd=FirebaseDatabase.getInstance();
        table_food=bdd.getReference("Food");

        // Intialiser les components graphiques


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

                Food food=dataSnapshot.getValue(Food.class);
                // Binding information image,nom,prix,description

                // Image
                Picasso.with(getBaseContext()).load(food.getImgUrl())
                        .into(food_img);

                food_nom.setText(food.getNom());
                food_prix.setText(food.getPrix());
                food_description.setText(food.getDescription());
                collapsingToolbarLayout.setTitle("Pizza/"+food.getNom());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
