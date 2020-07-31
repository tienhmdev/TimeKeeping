package hvcg.edu.timekeeping.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hvcg.edu.timekeeping.R;
import hvcg.edu.timekeeping.entities.Staff;
import hvcg.edu.timekeeping.presenters.RequestDataPresenter;

public class WelcomeActivity extends AppCompatActivity implements IRequestDataView {
    public static final String SHARED_DATABASE_NAME = "Save114324";
    public static final String IS_IMPORT_KEY = "import";
    RequestDataPresenter requestDataPresenter;

    private TextView tvLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        this.tvLoad = findViewById(R.id.load);
        requestDataPresenter = new RequestDataPresenter(this, this);
        final SharedPreferences shared = this.getSharedPreferences(SHARED_DATABASE_NAME, MODE_PRIVATE);
        final boolean isImport = shared.getBoolean(IS_IMPORT_KEY, false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isImport){
                    tvLoad.setVisibility(View.VISIBLE);
                    tvLoad.setText(getResources().getString(R.string.welcome_alert));
                    requestDataPresenter.onImportDemoData();
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putBoolean(IS_IMPORT_KEY, true);
                    editor.apply();
                }else {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);



    }

    @Override
    public void onQueryStaffByIdResult(Staff staff) {

    }

    @Override
    public void onQueryStaffsByDateResult(List<Staff> staffs) {

    }

    @Override
    public void onQueryAllStaffResult(List<Staff> staffs) {

    }

    @Override
    public void hasTimeKeepingResult(boolean isKeep) {

    }

    @Override
    public void isStaffExistsResult(boolean isExists) {

    }

    @Override
    public void timeKeepingResult(Staff staff, boolean isOk, int code) {

    }

    @Override
    public void importDemoDataResult(boolean isOk, int code) {
        if (isOk){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
