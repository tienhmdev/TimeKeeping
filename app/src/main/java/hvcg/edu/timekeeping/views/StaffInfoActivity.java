package hvcg.edu.timekeeping.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import hvcg.edu.timekeeping.R;
import hvcg.edu.timekeeping.entities.Staff;

public class StaffInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvStaffId;
    private TextView tvName;
    private TextView tvNgaySinh;
    private TextView tvViTri;
    private Button btnFinish;

    private Staff staff;

    public static final String ARG_STAFF_BUNDLE = "staffBundle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_info);
        getData();
        initViews();
        setupViews();
    }

    private void setupViews() {
        btnFinish.setOnClickListener(this);
        tvName.setText(this.staff.getTenNhanVien());
        tvStaffId.setText(this.staff.getMaNhanVien());
        tvNgaySinh.setText(this.staff.getNgaySinh());
        tvViTri.setText(this.staff.getViTri());
    }

    private void getData() {
        Bundle bundle = getIntent().getBundleExtra(ARG_STAFF_BUNDLE);
        if (bundle !=null){
            this.staff = bundle.getParcelable("staff");
        }else {
            //return to home
        }
    }

    private void initViews() {
        tvStaffId = findViewById(R.id.tvStaffId);
        tvName = findViewById(R.id.tvName);
        tvNgaySinh = findViewById(R.id.tvNgaySinh);
        tvViTri = findViewById(R.id.tvViTri);
        btnFinish = findViewById(R.id.btnFinish);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
