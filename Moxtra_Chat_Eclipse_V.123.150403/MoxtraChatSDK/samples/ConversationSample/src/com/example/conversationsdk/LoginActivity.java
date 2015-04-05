package com.example.conversationsdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXChatManager;
import com.moxtra.sdk.MXGroupChatSession;
import com.moxtra.sdk.MXSDKConfig;

import java.util.List;

/**
 * Created by Breeze on 14/12/18.
 */
public class LoginActivity extends FragmentActivity {

    private static final String TAG = "LoginActivity";
    private MXAccountManager mAccountMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountMgr = ((MoxtraApplication) super.getApplication()).getAccountMgr();
        if (mAccountMgr.isLinked()) {
            Intent intent = new Intent(this, MainActivity.class);
            super.startActivity(intent);
            super.finish();
            return;
        }
        super.setContentView(R.layout.activity_login);
    }

    public void loginButtonPressed(View view) {
        EditText etUniqueId = (EditText) super.findViewById(R.id.et_unique_id);
        final MXSDKConfig.MXUserInfo user = new MXSDKConfig.MXUserInfo(etUniqueId.getText().toString(), MXSDKConfig.MXUserIdentityType.IdentityUniqueId);
        mAccountMgr.setupUser(user, null, null, null, new MXAccountManager.MXAccountLinkListener() {
            @Override
            public void onLinkAccountDone(boolean bSuccess) {
                Log.d(TAG, "onLinkAccountDone(), logged-in user: " + mAccountMgr.getUserInfo());
                if (bSuccess) {
                    Toast.makeText(getApplicationContext(), getString(R.string.login_success), Toast.LENGTH_LONG).show();

                    List<MXGroupChatSession> sessions = MXChatManager.getInstance().getGroupChatSessions();
                    if (sessions != null) {
                        for (MXGroupChatSession session : sessions) {
                            Log.d(TAG, session.toString());
                        }
                    }
                    Log.d(TAG, "total number of unread messages: " + MXChatManager.getInstance().getTotalNumberOfUnreadMessages());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void ssoLoginButtonPressed(View v) {
        final MXSDKConfig.MXUserInfo user = new MXSDKConfig.MXUserInfo("your_access_token", MXSDKConfig.MXUserIdentityType.IdentityTypeSSOAccessToken);
        mAccountMgr.setupUser(user, null, null, new MXAccountManager.MXAccountLinkListener() {
            @Override
            public void onLinkAccountDone(boolean bSuccess) {
                if (bSuccess) {
                    Toast.makeText(getApplicationContext(), getString(R.string.SSO_Login_Success), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
