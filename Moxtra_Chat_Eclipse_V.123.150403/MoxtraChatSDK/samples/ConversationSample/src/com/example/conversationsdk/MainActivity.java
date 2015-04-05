package com.example.conversationsdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.moxtra.binder.conversation.MXConversationActivity;
import com.moxtra.binder.sdk.InviteToChatCallback;
import com.moxtra.binder.sdk.InviteToMeetCallback;
import com.moxtra.binder.sdk.UserProfileCallback;
import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXChatManager;
import com.moxtra.sdk.MXException.AccountManagerIsNotValid;
import com.moxtra.sdk.MXGroupChatSession;
import com.moxtra.sdk.MXGroupChatSessionCallback;
import com.moxtra.sdk.MXSDKConfig;
import com.moxtra.sdk.MXSDKConfig.MXUserIdentityType;
import com.moxtra.sdk.MXSDKException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

	private static final int DIALOG_USER_PROFILE = 1;
    private static final int DIALOG_INVITE = 2;
    private static final int DIALOG_JOIN_MEET = 3;
    private static final int DIALOG_OPEN_CHAT = 4;

    private static final int REQUEST_CREATE_GROUP_CHAT = 100;

    private static final String TAG = "MainActivity";

    private MXAccountManager mAccountMgr;
    private String mBinderID;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        init();
	}

    private void init(){
        mAccountMgr = ((MoxtraApplication) super.getApplication()).getAccountMgr();
        MXChatManager.getInstance().setRemoteNotificationType(MXChatManager.PushNotificationType.LONG_CONNECTION);
		MXChatManager.getInstance().setMoreButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick(), callback from Moxtra!!");
            }
        });
        MXChatManager.getInstance().setChatInviteCallback(new InviteToChatCallback() {
            @Override
            public void onInviteToChat(String binderID, Bundle extras) {
                Log.d(TAG, "Invite to chat, binderID = " + binderID);
                if (TextUtils.isEmpty(binderID)) {
                    // means that create binder
                } else {
                    Intent i = new Intent(MainActivity.this, InviteToGroupChatActivity.class);
                    i.putExtra("inviteToChat", true);
                    i.putExtra("binderID", binderID);
                    MainActivity.this.startActivity(i);
                }
            }
        });

        MXChatManager.getInstance().setMeetInviteCallback(new InviteToMeetCallback() {
            @Override
            public void onInviteToMeet(String meetId, String url, Bundle extras) {
                Log.d(TAG, "onInviteToMeet(), meetId = " + meetId + ", url = " + url);
                Intent i = new Intent(MainActivity.this, InviteToGroupChatActivity.class);
                i.putExtra("inviteToMeet", true);
                MainActivity.this.startActivity(i);
            }
        });

        MXChatManager.getInstance().setGroupChatSessionCallback(new MXGroupChatSessionCallback() {
            @Override
            public void onGroupChatSessionCreated(MXGroupChatSession session) {
                Log.d(TAG, "onGroupChatSessionCreated(), session = " + session);
            }

            @Override
            public void onGroupChatSessionUpdated(MXGroupChatSession session) {
                Log.d(TAG, "onGroupChatSessionUpdated(), session = " + session);
            }

            @Override
            public void onGroupChatSessionDeleted(MXGroupChatSession session) {
                Log.d(TAG, "onGroupChatSessionDeleted(), session = " + session);
                if (session != null && TextUtils.equals(session.getSessionID(), mBinderID)) {
                    mBinderID = null;
                }
            }
        });
        MXChatManager.getInstance().setUserProfileCallback(new UserProfileCallback() {
            @Override
            public void openUserProfile(String uniqueId, Bundle args) {
                Log.d(TAG, "openUserProfile(), uniqueId = " + uniqueId + ", args = " + args);
            }
        });

        MXChatManager.getInstance().setOnMeetEndListener(new MXChatManager.OnEndMeetListener(){

            @Override
            public void onMeetEnded(String meetId) {
                Toast.makeText(getApplicationContext(),"Meet ended:" + meetId, Toast.LENGTH_SHORT).show();
            }
        });

        // In case you want to customize the behavior of the plus button, uncomment the following section and do what you want.
//        MXChatManager.getInstance().setAddFileBtnClickedListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Open your activity and upload file here~~~", Toast.LENGTH_SHORT).show();
//
//                //this is upload file code
//                ArrayList<String> filelists = new ArrayList<String>();
//                File file = new File(Environment.getExternalStorageDirectory(),"/DCIM/Camera/test.jpg");
//                filelists.add(file.getAbsolutePath());
//                MXChatManager.getInstance().importLocalFiles(filelists);
//            }
//        });
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CREATE_GROUP_CHAT:
                if (resultCode == Activity.RESULT_OK) {
                    mBinderID = data.getExtras().getString("binderID");
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	@Deprecated
    protected Dialog onCreateDialog(int id, Bundle args) {
        switch (id) {
            case DIALOG_USER_PROFILE: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.update_profile);
                LayoutInflater inflater = super.getLayoutInflater();
                final View contentView = inflater.inflate(R.layout.dialog_user_profile, null);
                builder.setView(contentView)
                        .setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText etFirstName = (EditText) contentView.findViewById(R.id.et_firstname);
                                EditText etLastName = (EditText) contentView.findViewById(R.id.et_lastname);
                                MXSDKConfig.MXProfileInfo profile = new MXSDKConfig.MXProfileInfo(etFirstName.getText().toString(), etLastName.getText().toString(), (String) null);
                                mAccountMgr.updateUserProfile(profile);
                            }
                        })
                        .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                return builder.create();
            }

            case DIALOG_INVITE: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.invite_to_chat);
                LayoutInflater inflater = super.getLayoutInflater();
                final View contentView = inflater.inflate(R.layout.dialog_invite_to_chat, null);
                builder.setView(contentView)
                        .setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText etUniqueID = (EditText) contentView.findViewById(R.id.et_unique_id);
//                                // Invite by emails
//                                List<String> emails = new ArrayList<String>();
//                                emails.add("test@aa.com");
//                                MXChatManager.getInstance().inviteByEmails(binderID, emails, callback);
                                // Invite by unique Ids
                                List<String> uniqueIds = new ArrayList<String>();
                                uniqueIds.add(etUniqueID.getText().toString());
                                MXChatManager.getInstance().inviteByUniqueIds(mBinderID, uniqueIds, new MXChatManager.OnInviteListener() {

                                    @Override
                                    public void onInviteSuccess() {

                                    }

                                    @Override
                                    public void onInviteFailed(int errorCode, String message) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                return builder.create();
            }

            case DIALOG_JOIN_MEET: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.join_a_meet);
                LayoutInflater inflater = super.getLayoutInflater();
                final View contentView = inflater.inflate(R.layout.dialog_invite_to_chat, null);
                final EditText et = (EditText)contentView.findViewById(R.id.et_unique_id);
                et.setHint("Input Meet ID");
                builder.setView(contentView)
                        .setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String meetId = et.getText().toString();
                                if (meetId.length() > 0)
                                    try {
                                        MXChatManager.getInstance().joinMeet(meetId, "WhoAmI", new MXChatManager.OnJoinMeetListener() {

                                            @Override
                                            public void onJoinMeetDone(String meetId, String meetUrl) {
                                                Toast.makeText(getApplicationContext(), "Join Meet Done, Meet Id:" + meetId + "/MeetUrl:" + meetUrl,  Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onJoinMeetFailed() {
                                                Toast.makeText(getApplicationContext(), "Join Meet failed ...",  Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } catch (MXSDKException.MeetIsInProgress e) {

                                    }
                                else
                                    Toast.makeText(getApplicationContext(), "Input Meet Id...", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                return builder.create();
            }

            case DIALOG_OPEN_CHAT: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.open_chat);
                LayoutInflater inflater = super.getLayoutInflater();
                final View contentView = inflater.inflate(R.layout.dialog_invite_to_chat, null);
                final EditText et = (EditText) contentView.findViewById(R.id.et_unique_id);
                et.setHint("Input Chat/Binder ID");
                builder.setView(contentView)
                        .setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String binderId = et.getText().toString();
                                if (binderId.length() > 0)
                                    openChatWithBinderId(binderId);
                                else
                                    Toast.makeText(getApplicationContext(), "Input Chat/Binder ID...", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                return builder.create();
            }
        }
        return super.onCreateDialog(id, args);
    }

      public void startMeetButtonPressed(View v){
          try {
              MXChatManager.getInstance().startMeet("No Topic...",null,null, new MXChatManager.OnStartMeetListener(){

                  @Override
                  public void onStartMeetDone(String meetId, String meetUrl) {
                      Toast.makeText(getApplicationContext(), "Start Meet Done, Meet Id:" + meetId + "/MeetUrl:" + meetUrl,  Toast.LENGTH_LONG).show();
                  }
              });
          } catch (MXSDKException.Unauthorized unauthorized) {
              unauthorized.printStackTrace();
          } catch (MXSDKException.MeetIsInProgress meetIsInProgress) {
              meetIsInProgress.printStackTrace();
          }
      }

    public void joinMeetButtonPressed(View v){
        showDialog(DIALOG_JOIN_MEET);
    }

    public void inviteByUniqueButtonPressed(View v) {
        super.showDialog(DIALOG_INVITE);
    }

	public void setupButtonPressed(View v) {
		if (mAccountMgr != null && mAccountMgr.isLinked()) {
            Toast.makeText(getApplicationContext(), getString(R.string.moxtra_is_setup_already_go_unlink_and_try_again), Toast.LENGTH_LONG).show();
            return;
        }
		super.showDialog(DIALOG_USER_PROFILE);
	}

	public void createChatButtonPressed(View v) {
        if (TextUtils.isEmpty(mBinderID)) {
            Intent i = new Intent(this, InviteToGroupChatActivity.class);
            super.startActivityForResult(i, REQUEST_CREATE_GROUP_CHAT);
        } else {
            try {
                MXChatManager.getInstance().openChat(mBinderID, null);
            } catch (AccountManagerIsNotValid e) {
                e.printStackTrace();
            }
        }
	}

    public void deleteChatButtonPressed(View v) {
        MXChatManager.getInstance().deleteChat(mBinderID);
    }

	public void unlinkButtonPressed(View v) {
		if (mAccountMgr != null && mAccountMgr.isLinked()) {
            mAccountMgr.unlinkAccount(new MXAccountManager.MXAccountUnlinkListener() {
                @Override
                public void onUnlinkAccountDone(MXSDKConfig.MXUserInfo user) {
                    mBinderID = null;
                    Toast.makeText(getApplicationContext(), getString(R.string.unlink_success), Toast.LENGTH_LONG).show();
                    MXChatManager.getInstance().unlink();
                    // jump to login activity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                }
            });
        }
	}

    public void setupAccountManagerButtonPressed(View v) {
        init();
    }

    public void openChatButtonClicked(View v){
        showDialog(DIALOG_OPEN_CHAT);
    }

    private void openChatWithBinderId(String binderId){
        try {
            MXChatManager.getInstance().openChat(binderId, new  MXChatManager.OnOpenChatListener(){

                @Override
                public void onOpenChatSuccess() {
                    Toast.makeText(MainActivity.this,"open binder successfully!",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onOpenChatFailed(int resultCode, String resultStr) {
                    switch(resultCode){
                        case MXConversationActivity.OpenChatResultCallback.BINDER_ID_NOT_FOUND:
                            Toast.makeText(MainActivity.this,resultStr,Toast.LENGTH_SHORT).show();
                            break;
                        case MXConversationActivity.OpenChatResultCallback.BINDER_ID_NULL:
                            Toast.makeText(MainActivity.this,resultStr,Toast.LENGTH_SHORT).show();
                            break;
                        case MXConversationActivity.OpenChatResultCallback.USER_NOT_LOGIN:
                            Toast.makeText(MainActivity.this,resultStr,Toast.LENGTH_SHORT).show();
                            break;
                        case MXConversationActivity.OpenChatResultCallback.USER_OFFLINE:
                            Toast.makeText(MainActivity.this,resultStr,Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            if(resultStr!= null)
                            Toast.makeText(MainActivity.this,resultStr,Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        } catch (AccountManagerIsNotValid e) {
            e.printStackTrace();
        }
    }
}
