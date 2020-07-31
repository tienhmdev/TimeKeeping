package hvcg.edu.timekeeping.models;
import android.content.Context;
import android.util.Log;

import java.util.Date;
import hvcg.edu.timekeeping.database.DatabaseManager;
import hvcg.edu.timekeeping.entities.Staff;
import hvcg.edu.timekeeping.presenters.IRequestDataPresenter;
import hvcg.edu.timekeeping.presenters.RequestDataPresenter;

public class RequestDataModel {
    private IRequestDataPresenter presenter;
    private DatabaseManager databaseManager;

    public RequestDataModel(Context context, IRequestDataPresenter presenter) {
        this.presenter = presenter;
        this.databaseManager = new DatabaseManager(context, DatabaseManager.DATABASE_NAME, null, DatabaseManager.DATABASE_VERSION);
    }

    public void handleQueryStaffById(String id){
        presenter.onQueryStaffByIdResult(databaseManager.getUserById(id));

    }

    public void handleQueryStaffsByDate(Date date){
        presenter.onQueryStaffsByDateResult(databaseManager.getUsersByDate(date));
    }

    public void handleQueryAllStaff(){
        presenter.onQueryAllStaffResult(databaseManager.getAllUser());
    }

    public void handleTimeKeepingNow(String mnv, Date date){
        if (databaseManager.isStaffExists(mnv)){
            if (!databaseManager.hasTimeKeeping(mnv, date)){
                databaseManager.timeKeepingNow(mnv);
                Staff staff = databaseManager.getUserById(mnv);
                staff.setDate(new Date());
                presenter.timeKeepingResult(staff, true, RequestDataPresenter.TIMEKEEPING_OK);
            }else {
                presenter.timeKeepingResult(databaseManager.getUserById(mnv), false, RequestDataPresenter.TODAY_HAS_BEEN_CHECK);
            }
        }else {
            presenter.timeKeepingResult(databaseManager.getUserById(mnv), false, RequestDataPresenter.STAFF_IS_NOT_EXISTS);
        }
    }

    public void checkStaffExists(String mnv){
        presenter.isStaffExistsResult(databaseManager.isStaffExists(mnv));
    }

    public void checkHasTimeKeeping(String mnv, Date date){
        presenter.hasTimeKeepingResult(databaseManager.hasTimeKeeping(mnv, date));
    }

    public void handleImportDemoData() {
        if (databaseManager.createTestDatabase()){
            presenter.importDemoDataResult(true, 1);
        }else {
            presenter.importDemoDataResult(false, -1);
            Log.d("error", "error");
        }
    }
}
