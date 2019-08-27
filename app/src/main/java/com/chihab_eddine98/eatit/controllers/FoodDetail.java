package com.chihab_eddine98.eatit.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.chihab_eddine98.eatit.R;
import com.chihab_eddine98.eatit.common.Common;
import com.chihab_eddine98.eatit.database.Database;
import com.chihab_eddine98.eatit.model.Food;
import com.chihab_eddine98.eatit.model.FoodOrder;
import com.chihab_eddine98.eatit.model.Rating;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

public class FoodDetail extends AppCompatActivity implements RatingDialogListener {



    TextView food_nom,food_description,food_prix;
    ImageView food_img;

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart,btnRating;
    ElegantNumberButton btnQte;
    RatingBar ratingBar;

    String foodId="";

    FirebaseDatabase bdd;
    DatabaseReference table_food;
    DatabaseReference table_rating;

    Food currentFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);


        // Database

        bdd=FirebaseDatabase.getInstance();
        table_food=bdd.getReference("Food");
        table_rating=bdd.getReference("Rating");

        // Intialiser les components graphiques

        btnCart=findViewById(R.id.btnCart);
        btnRating=findViewById(R.id.btnRating);
        btnQte=findViewById(R.id.btnQte);


        ratingBar=findViewById(R.id.ratingBar);

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showRatingDialog();
            }
        });

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
            if (Common.isConnectedToNet(getBaseContext()))
            {
                getFoodDetail(foodId);
                getRatingOfFood(foodId);

            }
            else
            {
                Toast.makeText(FoodDetail.this," Check your connection !",Toast.LENGTH_SHORT).show();
                return;
            }
        }




    }

    private void getRatingOfFood(String foodId) {

        Query foodRating=table_rating.orderByChild("foodId").equalTo(foodId);

        foodRating.addValueEventListener(new ValueEventListener() {
            int cpt=0;
            float moyenne=0f,somme=0f;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    Rating item=data.getValue(Rating.class);
                    assert item != null;
                    somme+= Float.parseFloat(item.getRate());
                    cpt++;
                }



                if(cpt!=0)
                {
                    moyenne=somme/cpt;
                }

                ratingBar.setRating(moyenne);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showRatingDialog() {

        new AppRatingDialog.Builder()
                            .setPositiveButtonText("Valider")
                            .setNegativeButtonText("Annuler")
                            .setNoteDescriptions(Arrays.asList("Trop mauvais","Pas bon","Pas Mal","Bon !","Super Bon !"))
                            .setNoteDescriptionTextColor(android.R.color.black)
                            .setDefaultRating(1)
                            .setTitle("Votre avis est important pour nous ")
                            .setDescription(" Un avis de vous vaut hyper cher !")
                            .setTitleTextColor(android.R.color.black)
                            .setDescriptionTextColor(android.R.color.black)
                            .setStarColor(R.color.colorPrimary)
                            .setHint(" Ceçi vous plait ?")
                            .setHintTextColor(R.color.colorPrimary)
                            .setCommentTextColor(android.R.color.white)
                            .setCommentBackgroundColor(R.color.noirClair)
                            .setWindowAnimation(R.style.MyDialogFadeAnimation)
                            .create(FoodDetail.this)
                            .show();
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

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int rate, String comment)
    {
        final Rating rating=new Rating(Common.currentUser.getPhone(),
                                 foodId,
                                 String.valueOf(rate),
                                 comment);

        table_rating.child(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(Common.currentUser.getPhone()).exists())
                {
                    // On supprime l'ancien avis
                    table_rating.child(Common.currentUser.getPhone()).removeValue();
                    // On sauvegarde la nouvelle
                    table_rating.child(Common.currentUser.getPhone()).setValue(rating);

                }
                else
                {
                    table_rating.child(Common.currentUser.getPhone()).setValue(rating);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
