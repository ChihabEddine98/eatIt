package com.chihab_eddine98.eatit.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.chihab_eddine98.eatit.model.User;

public class Common {

    // Constantes Globales
    public static final String DELETE="Supprimer";
    public static final String USR_KEY="userPhone";
    public static final String USR_Pass="mdp";


    public static User currentUser;



    // Cette méthode check si l'utilisateur est bien connecté à internet

    public static boolean isConnectedToNet(Context context)
    {
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager!=null)
        {
            NetworkInfo[] infos=connectivityManager.getAllNetworkInfo();

            if (infos!=null)
            {
                for (int i=0;i<infos.length;i++)
                {
                    if (infos[i].getState()==NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }

        }



        return false;
    }

}
