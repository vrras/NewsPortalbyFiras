package com.example.android.beritafiras.helpers;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Firas Luthfi on 2/13/2018.
 */

public class MyValidation {

    private Context context;

    /**
     * constructor
     * @param context
     */
    public MyValidation(Context context) {
        this.context = context;
    }

    /**
     *
     * @param yourField
     * Method ini digunakan untuk validasi field kosong atau tidak
     */
    public boolean isEmptyField(String yourField){
        return !TextUtils.isEmpty(yourField);
    }

    /**
     *
     * @param email
     * Method dibawah ini untuk validasi email kosong atau salah
     */
    public boolean isValidateEmail(String email){
        return !TextUtils.isEmpty(email)&& Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     *
     * @param password
     * @param retypePassword
     * method ini digunakan untuk mencocokan password dengan retype password
     */
    public boolean isMatch(String password, String retypePassword){
        return password.equals(retypePassword);
    }

    /**
     * method untuk menyembunyikan keyboard
     * @param view
     */
    private void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
