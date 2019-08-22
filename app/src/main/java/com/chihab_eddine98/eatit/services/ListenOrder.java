package com.chihab_eddine98.eatit.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.chihab_eddine98.eatit.R;
import com.chihab_eddine98.eatit.controllers.MesCommandes;
import com.chihab_eddine98.eatit.model.Order;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListenOrder extends Service implements ChildEventListener {


    FirebaseDatabase bdd;
    DatabaseReference table_order;




    public ListenOrder() {
    }


    @Override
    public void onCreate() {

        super.onCreate();

        bdd=FirebaseDatabase.getInstance();
        table_order=bdd.getReference("FoodOrder");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        table_order.addChildEventListener(this);

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        // Update in order
        Order order=dataSnapshot.getValue(Order.class);
        showNotification(dataSnapshot.getKey(),order);

    }

    private void showNotification(String key, Order order) {

        Intent intent=new Intent(getBaseContext(), MesCommandes.class);
        intent.putExtra("userPhone",order.getPhone());
        PendingIntent contentIntent=PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel=
                    new NotificationChannel("orderStatus","orderStatus",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

        NotificationCompat.Builder builder= new NotificationCompat.Builder(getBaseContext(),"orderStatus");

        builder.setAutoCancel(true)
               .setDefaults(Notification.DEFAULT_ALL)
               .setWhen(System.currentTimeMillis())
               .setTicker("EatIt")
               .setContentInfo(" Des nouvelles informations disponibles sur votre commande !")
               .setContentText("Commande #"+key+" a était modifiée rentrez pour en savoir plus")
               .setContentIntent(contentIntent)
               .setSmallIcon(R.mipmap.ic_launcher_round);


        NotificationManager notificationManager=(NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());

    }


    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
