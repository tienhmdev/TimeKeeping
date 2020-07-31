package hvcg.edu.timekeeping.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import hvcg.edu.timekeeping.R;
import hvcg.edu.timekeeping.adapters.RecycleViewAdapter;
import hvcg.edu.timekeeping.entities.Staff;
import hvcg.edu.timekeeping.presenters.RequestDataPresenter;
import hvcg.edu.timekeeping.views.IRequestDataView;

public class DayOfWeekFragment extends Fragment implements IRequestDataView, IEventFromActivity, SwipeRefreshLayout.OnRefreshListener, ISearch {
    public static final String ARG_TIME = "time321323256";
    private List<Staff> staffs;
    private List<Staff> staffListFull;
    private RequestDataPresenter requestDataPresenter;
    private RecyclerView list;
    private TextView tvNoData;
    private RecycleViewAdapter adapter;
    private SwipeRefreshLayout sflReload;
    private Date date;
    private FrameLayout flBox;

    //interface
    IEventFromActivity eventFromActivity;
    ISearch search;

    public static DayOfWeekFragment newInstance(int timeNode) {
        DayOfWeekFragment fragment = new DayOfWeekFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_TIME, timeNode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.eventFromActivity = this;
        this.search = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_of_week, container, false);
        initStructure();
        initList();
        initViews(view);
        setupRecyclerView();
        getData();

        Log.d("testData", "value: " + this.staffs.size());

        return view;
    }

    private void initList() {
        this.staffs = new ArrayList<>();
        this.staffListFull = new ArrayList<>();
    }

    private void getData() {
        if (getArguments() != null){
            int timeNode = getArguments().getInt(ARG_TIME);
            Log.d("testTime", String.valueOf(timeNode));
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, timeNode);
            this.date = calendar.getTime();
            Log.d("testTime", calendar.getTime().toString());
            requestDataPresenter.onQueryStaffsByDate(calendar.getTime());
        }else {
            enableNoDataScreen();
        }
    }

    private void enableNoDataScreen() {
        this.tvNoData.setVisibility(View.VISIBLE);
    }

    private void disableNoDataScreen() {
        this.tvNoData.setVisibility(View.GONE);
    }

    private void initStructure() {
        this.requestDataPresenter = new RequestDataPresenter(getContext(), this);
    }

    private void setupRecyclerView() {
        this.adapter = new RecycleViewAdapter(getContext(), this.staffs, R.layout.staff_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        this.list.setLayoutManager(layoutManager);
        this.list.setAdapter(this.adapter);
    }

    private void initViews(View view) {
        this.list = view.findViewById(R.id.list);
        this.sflReload = view.findViewById(R.id.sflReload);
        this.sflReload.setOnRefreshListener(this);
        this.flBox = view.findViewById(R.id.flBox);
        this.tvNoData = view.findViewById(R.id.tvNoData);
    }

    @Override
    public void onQueryStaffByIdResult(Staff staff) {

    }

    @Override
    public void onQueryStaffsByDateResult(final List<Staff> staffs) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateData(staffs);
                if (sflReload.isRefreshing()){
                    sflReload.setRefreshing(false);
                    Snackbar.make(flBox, getResources().getString(R.string.all_done), Snackbar.LENGTH_LONG).show();
                }
            }
        }, 2000);
    }

    private void updateData(List<Staff> staffs){
        if (staffs != null){
            if (!staffs.isEmpty()){
                disableNoDataScreen();
                this.staffs = new ArrayList<>(staffs);
                this.staffListFull = new ArrayList<>(this.staffs);
                this.adapter.addData(this.staffs);
            }else {
                enableNoDataScreen();
            }
        }else {
            enableNoDataScreen();
        }
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

    }

    @Override
    public void newStaffTimeKeepingResult(Staff staff) {
        Log.d("testInterface", staff.getTenNhanVien());
        this.adapter.addData(staff);
    }

    @Override
    public void onRefresh() {
        requestDataPresenter.onQueryStaffsByDate(this.date);
    }

    @Override
    public void search(String keyWord) {
        List<Staff> resultStaffList = new ArrayList<>();
        if (!keyWord.isEmpty()){
            for(int i = 0; i < this.staffs.size(); i++){
                Staff staff = this.staffs.get(i);
                if (staff.getTenNhanVien().toLowerCase().trim().contains(keyWord.toLowerCase())
                        || staff.getMaNhanVien().toLowerCase().trim().contains(keyWord.toLowerCase())){
                    resultStaffList.add(staff);
                }
            }
            adapter.addData(resultStaffList);
        }else {
            adapter.addData(this.staffs);
        }
    }
}
