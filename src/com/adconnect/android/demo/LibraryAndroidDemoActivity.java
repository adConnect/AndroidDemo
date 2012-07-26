package com.adconnect.android.demo;

import com.android.adcnx.adlib.Ad;
import com.android.adcnx.adlib.AdBlock;
import com.android.adcnx.adlib.AdListener;
import com.android.adcnx.adlib.AdRequest;
import com.android.adcnx.adlib.AdRequest.ErrorCode;
import com.android.adcnx.adlib.AdSize;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;


/**
 * The Android Demo to showcase how to use the adConnect library.
 * 
 * This demo has the activity implement the AdListener interface in order to 
 * show how to get updates on the status of the library.
 * 
 * This is done so that the library can remain passive and degrade gracefully without
 * interfering with the main application.
 * @author KoulMomo
 *
 */
public class LibraryAndroidDemoActivity extends Activity implements AdListener
{
	private static final String LOG_TAG = "AdConnectLibraryAndroidDemo";
	
	//Adconnect library object.
	private AdBlock lib;
	private AdRequest req;
	private boolean isTest = true;
	
	/*
	 * At the beginning, if we are expecting our first ad,
	 * this is true.
	 */
	private boolean isFirstAd = true;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        //Set the current content view to the main.xml file in the layout folder
        //of the res folder.
        setContentView(R.layout.main);
        
        //Create the library for this activity
        
        //The constructor for the AdBlock object is:
        //AdBlock(context, AdSize.TYPE, publisher_id)
        lib = new AdBlock(this, AdSize.BANNER, "12345");
        
        /*Now we are setting the listener for the library.
        //In this case, the current activity object is also the listener.
        //This needs to be an object that is an instance of a class
        //that implements the AdListener interface.
        */
        lib.setAdListener(this);
        
        /*An ad request object is used to define the ad that is to be requested
        //If targeting information is available for the current user
        //ad.setBirthDate(string bday)
        //ad.setGender(Gender MALE or FEMALE)
        //ad.setLanguage(String language ex:"EN", or "CN")
        */
        req = new AdRequest();
        
        //During testing set the test mode to TRUE
        //in production this should be set to FALSE (which is the default)
        req.setTestMode(isTest);
        
        //Set the gravity of the FrameLayout that will contain the ad to the bottom.
        ((FrameLayout.LayoutParams) lib.getLayoutParams()).gravity = Gravity.BOTTOM;
        
        /*
         * We will initially set the visibility of the library to INVISIBLE,
         * until we have our first ad to show.
         */
        lib.setVisibility(View.INVISIBLE);
        
        //Add the content view to the curent view.
        addContentView(lib, lib.getLayoutParams());
    }
    
    /**
     * Called when the activity is resumed.
     * 
     * Restart serving ads by creating a new AdRequest
     * and loading ads again.
     */
    @Override
    public void onResume()
    {
        super.onResume();
        
        if(req == null)
    	{
    		req = new AdRequest();
    		req.setTestMode(isTest);
    	}
   
        if(lib != null)
        {
        	lib.loadAd(req);
        }
    }
    
    /**
     * Called when the activity is paused.
     * 
     * When the activity is paused, we need to stop
     * loading ads. This is important as the library 
     * spawns new threads to not interfere with the main
     * UI view of the main application.
     */
    @Override
    public void onPause()
    {
    	super.onPause();
    	
    	if(lib != null)
    	{	
    		lib.stopLoading();
    	}
    }

    /*
     * AD LISTENER INTERFACE IMPLEMENTATION 
     */
    
    /**
     * This method is invoked when a library that this object
     * is listening to fails to receive an ad.
     * 
     * In order to degrade gracefully without interfering with the current application,
     * it is up to the developer to decide what to do on failure.
     * 
     * One can choose to remove the AdBlock view, make the view invisible,
     * or attempt to load another adRequest.
     * 
     * @param err - The error code that has caused the ad to fail to load.
     */
	public void OnFailedToReceiveAd(Ad ad, ErrorCode err)
	{
		/*
		 * In this case, we choose to try re-loading the ad again.
		 */
		
		if(req == null)
		{
			req = new AdRequest();
			req.setTestMode(isTest);
		}
		
		if(lib != null)
		{
			lib.loadAd(req);
		}
	}
	
	/**
	 * Method that is invoked whenever an ad is received.
	 * In this case, we detect if this is the first ad to be shown.
	 * If it is, we set the visibility of the library to View.VISIBLE.
	 */
	public void onReceiveAd(Ad ad)
	{
		if(isFirstAd)
		{
			isFirstAd = false;
			
			if(lib != null)
			{
				lib.setVisibility(View.VISIBLE);
			}
		}
	}

	public void onDismissScreen(Ad ad)
	{
		// TODO Auto-generated method stub
	}

	public void onLeaveApplication(Ad ad)
	{
		//TODO Auto-generated method stub
	}

	public void onPresentScreen(Ad ad)
	{
		// TODO Auto-generated method stub
	}

	
}