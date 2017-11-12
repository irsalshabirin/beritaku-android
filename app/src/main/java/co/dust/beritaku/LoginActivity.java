package co.dust.beritaku;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Set;

import co.dust.beritaku.model.User;
//import rx.Observer;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;

/**
 * Created by irsal on 8/2/16.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    // my model
    private User user;

    // google
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    // facebook
    private CallbackManager fbCallbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // facebook
        FacebookSdk.sdkInitialize(getApplicationContext());

        user = new User();

        // google
        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        SignInButton googleLoginButton = (SignInButton) findViewById(R.id.login_google_button);
        googleLoginButton.setSize(SignInButton.SIZE_STANDARD);
        googleLoginButton.setScopes(gso.getScopeArray());

        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });
        // [END customize_button]

        fbCallbackManager = CallbackManager.Factory.create();

//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
//        LoginManager.getInstance().registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//            }
//
//            @Override
//            public void onCancel() {
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//            }
//        });

        LoginButton fbLoginButton = (LoginButton) findViewById(R.id.login_fb_button);
        fbLoginButton.setReadPermissions(Arrays.asList(
                "public_profile",
//                "user_friends",
//                "user_birthday",
                "email"

        ));

        fbLoginButton.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {

                String accessToken = loginResult.getAccessToken().getToken();
                if (BuildConfig.DEBUG) {
                    Log.e("accessToken", accessToken);
                }

                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {

                            // profile2 is the new profile
                            if (BuildConfig.DEBUG) {
                                Log.e("fb - firstname", profile2.getFirstName());
                                Log.e("fb - last name", profile2.getLastName());
                                Log.e("fb - name", profile2.getName());
                                Log.e("fb - id", profile2.getId());
                            }
                            user.id = profile2.getId();
                            user.name = profile2.getName();

                            mProfileTracker.stopTracking();

                        }
                    };

                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.

                } else {
                    Profile profile = Profile.getCurrentProfile();
                    if (BuildConfig.DEBUG) {
                        Log.e("fb - firstname", profile.getFirstName());
                        Log.e("fb - last name", profile.getLastName());
                        Log.e("fb - name", profile.getName());
                        Log.e("fb - id", profile.getId());
//                        Log.e("fb - link uri", profile.getLinkUri().getPath());
                    }
                    user.id = profile.getId();
                    user.name = profile.getName();
                }

                // App code
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                // Application code
                                if (BuildConfig.DEBUG) {
                                    Log.e("LoginActivity", response.toString());
                                }

                                try {
                                    String gender = object.getString("gender");
                                    String ageRange = object.getString("age_range");
//                                    String locale = object.getString("locale");
//                                    String birthday = object.getString("birthday"); // 01/31/1980 format

                                    user.gender = gender;
                                    user.age = ageRange;

                                    if (BuildConfig.DEBUG) {
                                        Log.e("gender - user - login", gender);
                                        Log.e("age range", ageRange);
//                                        Log.e("locale login", locale);
//                                        Log.e("birthday - login", birthday);
                                    }
                                } catch (JSONException ignored) {

                                }

                                try {
                                    String email = object.getString("email");
                                    user.email = email;

                                    if (object.has("picture")) {
                                        String profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                        // set profile image to imageview using Picasso or Native methods
                                        user.photoUrl = profilePicUrl;

                                        if (BuildConfig.DEBUG) {
                                            Log.e("url picture", profilePicUrl);
                                        }
                                    }

                                    if (BuildConfig.DEBUG) {
                                        Log.e("email user - login", email);
                                    }
                                } catch (JSONException ignored) {

                                }

                                insertFacebookProfile(user);

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, gender, age_range, locale, picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();

                if (loginResult.getAccessToken() != null) {
                    Set<String> deniedPermissions = loginResult.getRecentlyDeniedPermissions();

//                    if (deniedPermissions.contains("user_friends")) {
//                        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("user_friends"));
//                    }
                    if (deniedPermissions.contains("email")) {
                        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email"));
                    }

                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
            finish();
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
//            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleSignInResult(googleSignInResult);

//                    finish();
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // facebook
        fbCallbackManager.onActivityResult(requestCode, resultCode, data);

        // google
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            getGoogleProfileInformation(acct);
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
//            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
        }
    }

    private void getGoogleProfileInformation(GoogleSignInAccount acct) {
        try {
            String personName = acct.getDisplayName();
            String email = acct.getEmail();


            user.name = personName;
            user.email = email;

//            String token = acct.getIdToken();

            try {
                String personPhotoUrl = acct.getPhotoUrl().toString();
                user.photoUrl = personPhotoUrl;
            } catch (Exception ignored) {

            }
//            imageView.setImageResource(R.mipmap.ic_launcher);

//            personPhotoUrl = personPhotoUrl.substring(0, personPhotoUrl.length() - 2) + PROFILE_PIC_SIZE;

//            if (personPhotoUrl != null && !personPhotoUrl.equals("")) {
//                Picasso.with(imageView.getContext())
//                        .load(personPhotoUrl)
//                        .resize(200, 200)
//                        .error(R.mipmap.ic_launcher)
//                        .placeholder(R.mipmap.ic_launcher)
//                        .centerCrop()
//                        .transform(new CircleTransform())
//                        .into(imageView);
//            }

            insertGoogleProfile(user);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertGoogleProfile(User user) {
//        insertProfile(user, User.LOGIN_FROM_GOOGLE);
    }

    // [START googleSignIn]
    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END googleSignIn]

    // [START googleSignOut]
    private void googleSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
//                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END googleSignOut]

    // [START revokeAccessGoogle]
    private void revokeAccessGoogle() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
//                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccessGoogle]


    // insert profile - Facebook
    private void insertFacebookProfile(User user) {
//        insertProfile(user, User.LOGIN_FROM_FACEBOOK);
    }

//    private void insertProfile(User user, int loginFrom) {
//
//        ApiService orderService = ServiceFactory.createRetrofitService(ApiService.class);
//        orderService.registerOrLoginUser(user.id, user.name, user.photoUrl, user.email, loginFrom)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ApiService.Message>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("error ngirim data", e.toString());
////                        Toast.makeText(LoginActivity.this, "" + e, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onNext(ApiService.Message message) {
//
//                    }
//
//                });
//    }

}
