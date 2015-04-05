package com.example.conversationsdk;

import android.support.multidex.MultiDexApplication;

import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXSDKException;

/**
 * Created by Breeze on 14/10/24.
 */
public class MoxtraApplication extends MultiDexApplication {

    private MXAccountManager mAccountMgr;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mAccountMgr = MXAccountManager.createInstance(getApplicationContext());
        } catch (MXSDKException.InvalidParameter e) {
            e.printStackTrace();
        }
    }

    public MXAccountManager getAccountMgr() {
        return mAccountMgr;
    }
}
