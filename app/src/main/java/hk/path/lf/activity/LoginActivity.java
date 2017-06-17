package hk.path.lf.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hk.path.lf.R;
import hk.path.lf.entities.API_Login;
import hk.path.lf.entities.API_Login_Ret;
import hk.path.lf.entities.API_Prelogin;
import hk.path.lf.entities.API_Prelogin_Ret;
import hk.path.lf.entities.API_Return;
import hk.path.lf.net.api;

/**
 * 登录模块
 */
public class LoginActivity extends BaseActivity {


    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // UI references.
    private AutoCompleteTextView mIDView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private AppContext appContext;
    private LinearLayout mLinearLayout;
    private View mCaptchaView;
    private ImageView mCaptchaImageView;
    private boolean hasCaptcha;
    private EditText mCaptchaEditView;
    private String LastSID;
    Button mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = (AppContext) getApplication();
        setContentView(R.layout.activity_login);
        // Set up the login form.

        findView();
        init();

        //getWindow().
        //全透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void controlKeyboardLayout(final View root) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                adjustView(root);
            }
        });
    }

    private void adjustView(final View root) {
        Rect rect = new Rect();
        root.getWindowVisibleDisplayFrame(rect);
        ViewGroup.LayoutParams lp;
        lp = mLinearLayout.getLayoutParams();
        lp.height = rect.bottom;
        mLinearLayout.setLayoutParams(lp);
    }

    private void init() {
        populateAutoComplete();
        controlKeyboardLayout(findViewById(R.id.login_root));
        mIDView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !mIDView.getText().toString().equals(LastSID)) {
                    mSignInButton.setText("请稍等...");
                    mSignInButton.setEnabled(false);
                    api.Request(new API_Prelogin(mIDView.getText().toString()), new API_Return<API_Prelogin_Ret>() {
                        @Override
                        public void ret(int Code, API_Prelogin_Ret ret) {
                            mSignInButton.setText("登录");
                            mSignInButton.setEnabled(true);
                            if (Code == 0) {
                                if (ret.getRet() == 0) {
                                    mCaptchaView.setVisibility(View.GONE);
                                    hasCaptcha = false;
                                } else {
                                    Bitmap mBitmap;
                                    byte[] mData = Base64.decode(ret.getCaptcha(), Base64.DEFAULT);
                                    mBitmap = BitmapFactory.decodeByteArray(mData, 0, mData.length);
                                    mCaptchaImageView.setImageBitmap(mBitmap);
                                    mCaptchaView.setVisibility(View.VISIBLE);
                                    hasCaptcha = true;
                                }
                                System.out.println(ret.getRet());
                                System.out.println(ret.getCaptcha());
                            } else {
                                System.out.println("error:" + Code);
                            }
                        }
                    }, LoginActivity.this);
                    LastSID = mIDView.getText().toString();
                }
            }
        });

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void findView() {
        mCaptchaEditView = (EditText) findViewById(R.id.CaptchaText);
        mLinearLayout = (LinearLayout) findViewById(R.id.login_container);
        mCaptchaView = findViewById(R.id.captchaLayout);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mPasswordView = (EditText) findViewById(R.id.password);
        mIDView = (AutoCompleteTextView) findViewById(R.id.ID);
        mCaptchaImageView = (ImageView) findViewById(R.id.captchaImageView);
        mSignInButton = (Button) findViewById(R.id.email_sign_in_button);
    }


    private void populateAutoComplete() {

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mIDView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String mSID = mIDView.getText().toString();
        String password = mPasswordView.getText().toString();
        String captcha = mCaptchaEditView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mSID)) {
            mIDView.setError(getString(R.string.error_field_required));
            focusView = mIDView;
            cancel = true;
        } else if (!isIDValid(mSID)) {
            mIDView.setError(getString(R.string.error_invalid_id));
            focusView = mIDView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            api.Request(new API_Login(mSID, password, hasCaptcha, captcha), new API_Return<API_Login_Ret>() {
                @Override
                public void ret(int Code, API_Login_Ret ret) {
                    LastSID = "";
                    if (Code == 0) {
                        if (ret.getRet() == 0) {
                            appContext.setLogined(true);
                            appContext.setName(ret.getName());
                            appContext.setSID(mSID);
                            finish();
                        } else if (ret.getRet() == 1) {
                            showProgress(false);
                            mCaptchaEditView.setError(getString(R.string.error_incorrect_captcha));
                            mCaptchaEditView.setText("");
                            mCaptchaEditView.requestFocus();
                        } else if (ret.getRet() == 2) {
                            showProgress(false);
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();
                            mPasswordView.setText("");
                            mCaptchaEditView.setText("");
                        } else if (ret.getRet() == 3) {
                            showProgress(false);
                            mIDView.setError(getString(R.string.error_incorrect_sid));
                            mIDView.requestFocus();
                            mCaptchaEditView.setText("");
                        } else {
                            showProgress(false);
                            Toast.makeText(getApplication(), "未知错误", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        showProgress(false);
                        Toast.makeText(getApplication(), "连接网络失败", Toast.LENGTH_LONG).show();
                    }
                }
            }, LoginActivity.this);
//            mAuthTask = new UserLoginTask(email, password, captcha);
//            mAuthTask.execute((Void) null);
        }
    }

    private boolean isIDValid(String ID) {
        //TODO: Replace this with your own logic
        if (ID.length() != 12) {
            return false;
        }
        for (int i = ID.length(); --i >= 0; ) {
            if (!Character.isDigit(ID.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 0;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mIDView.setAdapter(adapter);
    }

}

