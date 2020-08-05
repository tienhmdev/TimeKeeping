package hvcg.edu.timekeeping.views;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import hvcg.edu.timekeeping.R;
import hvcg.edu.timekeeping.adapters.MainViewPagerAdapter;
import hvcg.edu.timekeeping.fragments.DayOfWeekFragment;
import hvcg.edu.timekeeping.entities.Staff;
import hvcg.edu.timekeeping.permission.IPermissionAccess;
import hvcg.edu.timekeeping.permission.PermissionUtil;
import hvcg.edu.timekeeping.presenters.RequestDataPresenter;
import hvcg.edu.timekeeping.scanner.CaptureActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IRequestDataView {
    private List<Fragment> fragments;
    private List<Staff> staffs;
    private MainViewPagerAdapter viewPagerAdapter;
    private RequestDataPresenter requestDataPresenter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ImageView qrScan;
    private EditText edtSearch;
    private RelativeLayout rlBox;
    private MaterialCardView cvCancelSearch;
    private Button btnCancelSearch;
    DayOfWeekFragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initMVPStructure();
        initLists();
        initToolbar();
        initFragments();
        initViewPager();
        initClickEvents();
        initSearch();
        btnCancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                Toast.makeText(MainActivity.this, "clcik", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSearch() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()){
                    cvCancelSearch.setVisibility(View.INVISIBLE);
                }else {
                    cvCancelSearch.setVisibility(View.VISIBLE);
                }
                checkCurrentPager();
                currentFragment.search(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkCurrentPager() {
        switch (viewPager.getCurrentItem()){
            case 0:
                this.currentFragment = (DayOfWeekFragment) this.fragments.get(0);
                break;
            case 1:
                this.currentFragment = (DayOfWeekFragment) this.fragments.get(1);
                break;
            case 2:
                this.currentFragment = (DayOfWeekFragment) this.fragments.get(2);
                break;
            case 3:
                this.currentFragment = (DayOfWeekFragment) this.fragments.get(3);
                break;
            case 4:
                this.currentFragment = (DayOfWeekFragment) this.fragments.get(4);
                break;
            case 5:
                this.currentFragment = (DayOfWeekFragment) this.fragments.get(5);
                break;
        }
    }

    private void initMVPStructure() {
        this.requestDataPresenter = new RequestDataPresenter(this, this);
    }

    private void initClickEvents() {
        this.qrScan.setOnClickListener(this);
    }


    private void initViewPager() {
        this.viewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), this.fragments);
        this.viewPager.setAdapter(this.viewPagerAdapter);
        this.viewPager.setOffscreenPageLimit(this.fragments.size());
        tabLayout.setupWithViewPager(viewPager, true);

        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i <= 5; i++){
            String dayOfWeek = getResources().getString(R.string.day_of_week_prefix) + calendar.get(Calendar.DAY_OF_WEEK);
            if (i == 0){
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                    dayOfWeek = getResources().getString(R.string.sunday);
                }
                tabLayout.getTabAt(0).setText(getResources().getString(R.string.today) + "(" + dayOfWeek + ")");
            }else{
                calendar.add(Calendar.DAY_OF_WEEK, -1);
                dayOfWeek = getResources().getString(R.string.day_of_week_prefix) + calendar.get(Calendar.DAY_OF_WEEK);
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                    dayOfWeek = getResources().getString(R.string.sunday);
                }
                tabLayout.getTabAt(i).setText(dayOfWeek);
            }
        }
    }

    private void initFragments() {
        for (int i = 0; i <= 5; i++){
            this.fragments.add(i, DayOfWeekFragment.newInstance(i-(i*2)));
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    private void initLists() {
        this.fragments = new ArrayList<>();
        this.staffs = new ArrayList<>();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        qrScan = findViewById(R.id.qrScan);
        rlBox = findViewById(R.id.rlBox);
        edtSearch = findViewById(R.id.edtSearch);
        cvCancelSearch = findViewById(R.id.cvCancelSearch);
        btnCancelSearch = findViewById(R.id.btnCancelSearch);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qrScan:
                String[] permissions = {Manifest.permission.CAMERA};
                PermissionUtil.requestPermission(MainActivity.this, permissions, new IPermissionAccess() {
                    @Override
                    public void permissionResult(boolean check) {
                        if (check){
                            IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                            integrator.setPrompt(getResources().getString(R.string.scanning_alert));
                            integrator.setCaptureActivity(CaptureActivity.class);
                            integrator.setOrientationLocked(true);
                            integrator.setCameraId(0);  // Use a specific camera of the device
                            integrator.setBeepEnabled(true);
                            integrator.initiateScan();
                        }else {
                            Snackbar.make(rlBox, getResources().getString(R.string.access_permissin_retry), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String contents = result.getContents();
            try {
                requestDataPresenter.onQueryStaffById(contents);
            }catch (Exception e){
                Snackbar.make(rlBox, getResources().getString(R.string.staff_not_exists), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onQueryStaffByIdResult(Staff staff) {
        requestDataPresenter.onTimeKeepingRequest(staff.getMaNhanVien(), new Date());
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
        if (isOk){
            Bundle bundle = new Bundle();
            bundle.putParcelable("staff", staff);
            Intent intent = new Intent(this, StaffInfoActivity.class);
            DayOfWeekFragment fragment = (DayOfWeekFragment) this.fragments.get(0);
            fragment.newStaffTimeKeepingResult(staff);
            intent.putExtra(StaffInfoActivity.ARG_STAFF_BUNDLE, bundle);
            startActivity(intent);
        }else {
            switch (code){
                case RequestDataPresenter.STAFF_IS_NOT_EXISTS:
                    Snackbar.make(rlBox, getResources().getString(R.string.staff_not_exists), Snackbar.LENGTH_LONG).show();
                    break;
                 case RequestDataPresenter.TODAY_HAS_BEEN_CHECK:
                     Snackbar.make(rlBox, getResources().getString(R.string.has_timekeeping), Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    public void importDemoDataResult(boolean isOk, int code) {

    }
}
