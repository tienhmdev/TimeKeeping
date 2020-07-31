package hvcg.edu.timekeeping.presenters;

import android.content.Context;

import java.util.Date;
import java.util.List;

import hvcg.edu.timekeeping.entities.Staff;
import hvcg.edu.timekeeping.models.RequestDataModel;
import hvcg.edu.timekeeping.views.IRequestDataView;

public class RequestDataPresenter implements IRequestDataPresenter {
    public static final int STAFF_IS_NOT_EXISTS = -1;
    public static final int TODAY_HAS_BEEN_CHECK = -2;
    public static final int TIMEKEEPING_OK = 1;


    private IRequestDataView view;
    private RequestDataModel model;

    public RequestDataPresenter(Context context, IRequestDataView view) {
        this.view = view;
        this.model = new RequestDataModel(context, this);
    }

    public void onQueryStaffById(String id){
        model.handleQueryStaffById(id);
    }

    public void onQueryStaffsByDate(Date date){
        model.handleQueryStaffsByDate(date);
    }

    public void onQueryAllStaff(){
        model.handleQueryAllStaff();
    }

    public void onImportDemoData(){
        model.handleImportDemoData();
    }

    public void onCheckStaffExists(String mnv){
        model.checkStaffExists(mnv);
    }

    public void onCheckHasTimeKeeping(String mnv, Date date){
        model.checkHasTimeKeeping(mnv, date);
    }

    public void onTimeKeepingRequest(String mnv, Date date){
        model.handleTimeKeepingNow(mnv, date);
    }

    @Override
    public void onQueryStaffByIdResult(Staff staff) {
        view.onQueryStaffByIdResult(staff);
    }

    @Override
    public void onQueryStaffsByDateResult(List<Staff> staffs) {
        view.onQueryStaffsByDateResult(staffs);
    }

    @Override
    public void onQueryAllStaffResult(List<Staff> staffs) {
        view.onQueryAllStaffResult(staffs);
    }

    @Override
    public void hasTimeKeepingResult(boolean isKeep) {
        view.hasTimeKeepingResult(isKeep);
    }

    @Override
    public void isStaffExistsResult(boolean isExists) {
        view.isStaffExistsResult(isExists);
    }

    @Override
    public void timeKeepingResult(Staff staff, boolean isOk, int code) {
        view.timeKeepingResult(staff, isOk, code);
    }

    @Override
    public void importDemoDataResult(boolean isOk, int code) {
        view.importDemoDataResult(isOk, code);
    }
}
