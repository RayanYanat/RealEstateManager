package com.example.realestatemanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;


import com.example.realestatemanager.utils.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowNetworkInfo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

@RunWith(AndroidJUnit4.class)
public class InternetTest {

    private ConnectivityManager connectivityManager;
    private ShadowNetworkInfo shadowOfActiveNetworkInfo;
    private Context context;


    @Test
    public void checkIsInternetAvailableFalse_NoInternet() throws Exception{
        this.setUpContextInternet();
        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.DISCONNECTED);
        assertFalse(utils.isInternetAvailable(context));
    }

    @Test
    public void checkIsInternetAvailableTrue_InternetOn() throws Exception{
        this.setUpContextInternet();
        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.CONNECTED);
        assertTrue(utils.isInternetAvailable(ApplicationProvider.getApplicationContext()));
    }

    private void setUpContextInternet(){
        context =  ApplicationProvider.getApplicationContext();
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        shadowOfActiveNetworkInfo = shadowOf(connectivityManager.getActiveNetworkInfo());
    }
}
