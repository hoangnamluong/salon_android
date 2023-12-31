package ou.lhn.salon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ou.lhn.salon.R;
import ou.lhn.salon.data.Constant;
import ou.lhn.salon.data.GlobalState;
import ou.lhn.salon.db.service.Auth_db.AuthServiceImpl;

public class LoginActivity extends AppCompatActivity implements View .OnClickListener{
    private EditText loginEditTxtUsername, loginEditTxtPassword;
    private TextView loginTxtForgotPassword, loginTxtRegister, loginTxtError;
    private AppCompatButton loginBtnLogin;
    private RelativeLayout loginLoadingBar;
    private AuthServiceImpl authService = AuthServiceImpl.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loginEditTxtUsername.setText("");
        loginEditTxtPassword.setText("");
    }

    private void initView() {
        this.loginEditTxtUsername = findViewById(R.id.loginEditTxtUsername);
        this.loginEditTxtPassword = findViewById(R.id.loginEditTxtPassword);
        this.loginTxtForgotPassword = findViewById(R.id.loginTxtForgotPassword);
        this.loginTxtRegister = findViewById(R.id.loginTxtRegister);
        this.loginBtnLogin = findViewById(R.id.loginBtnLogin);
        this.loginTxtError = findViewById(R.id.loginTxtError);
        this.loginLoadingBar = findViewById(R.id.loginLoadingBar);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.loginBtnLogin) {
            if(validateData()){
                loginFunc();
            }
        } else if (id == R.id.loginTxtForgotPassword) {
            forgotPasswordFunc();
        } else if (id == R.id.loginTxtRegister) {
            registerActivitySwitch();
        }
    }

    private void initListener() {
        loginBtnLogin.setOnClickListener(this);
        loginTxtForgotPassword.setOnClickListener(this);
        loginTxtRegister.setOnClickListener(this);
    }

    private boolean validateData() {
        if(loginEditTxtUsername.getText().toString().equals("") || loginEditTxtPassword.getText().toString().equals("")){
            loginTxtError.setText(R.string.username_or_password_can_t_be_empty);
            loginTxtError.setVisibility(View.VISIBLE);
            return false;
        }

        return true;
    }

    private void loginFunc() {
        loginTxtError.setVisibility(View.GONE);
        new Thread(() -> {
            runOnUiThread(() -> {
                loginBtnLogin.setEnabled(false);
                loginLoadingBar.setVisibility(View.VISIBLE);
                loginBtnLogin.setVisibility(View.GONE);
                if(authService.loginUser(loginEditTxtUsername.getText().toString(), loginEditTxtPassword.getText().toString())) {
                    Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
                    if (GlobalState.getLoggedIn().getRole()== Constant.ROLE.USER.value)
                        userActivitySwitch();
                    if (GlobalState.getLoggedIn().getRole()== Constant.ROLE.STAFF.value)
                        staffActivitySwitch();
                    if (GlobalState.getLoggedIn().getRole()== Constant.ROLE.STAFF.value)
                        adminActivitySwitch();
                }
                else
                    Toast.makeText(this, "Wrong username or password", Toast.LENGTH_LONG).show();

            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            runOnUiThread(() -> {
                loginBtnLogin.setEnabled(true);
                loginLoadingBar.setVisibility(View.GONE);
                loginBtnLogin.setVisibility(View.VISIBLE);
            });
        }).start();
    }

    private void forgotPasswordFunc() {
        Toast.makeText(LoginActivity.this, "Forgot Password", Toast.LENGTH_SHORT).show();
    }

    private void registerActivitySwitch() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void userActivitySwitch() {
        Intent intent = new Intent(this, UserMainActivity.class);
        startActivity(intent);
    }

    private void staffActivitySwitch() {
        Intent intent = new Intent(this, StaffMainActivity.class);
        startActivity(intent);
    }

    private void adminActivitySwitch() {
        Toast.makeText(this, "Admin", Toast.LENGTH_LONG).show();
//        thay cái mainactivity thành admin của m nha
//        Intent intent = new Intent(this, UserMainActivity.class);
//        startActivity(intent);
    }

}