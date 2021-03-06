package hvcg.edu.timekeeping.presenters;

import java.util.List;

import hvcg.edu.timekeeping.entities.Staff;

public interface IRequestDataPresenter {
    void onQueryStaffByIdResult(Staff staff);
    void onQueryStaffsByDateResult(List<Staff> staffs);
    void onQueryAllStaffResult(List<Staff> staffs);
    void hasTimeKeepingResult(boolean isKeep);
    void isStaffExistsResult(boolean isExists);
    void timeKeepingResult(Staff staff, boolean isOk, int code);
    void importDemoDataResult(boolean isOk, int code);
}
