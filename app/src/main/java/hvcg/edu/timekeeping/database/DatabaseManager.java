package hvcg.edu.timekeeping.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hvcg.edu.timekeeping.entities.Staff;

public class DatabaseManager extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "staffDatabase.sqlite";
    public static final String TABLE_STAFF_NAME = "staffs";
    public static final String TABLE_TIMEKEEPING_NAME = "timekeeping";

    public DatabaseManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Staff getUserById(String mnv){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_STAFF_NAME + " WHERE manhanvien = '" + mnv + "'", null);
        cursor.moveToNext();
        return convertCursorToStaff(cursor);
    }

    public List<Staff> getUsersByDate(Date date){
        SQLiteDatabase database = getReadableDatabase();
        SimpleDateFormat fm24 = new SimpleDateFormat("dd");
        String fm24hStr = fm24.format(date);
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_STAFF_NAME + " LEFT JOIN " + TABLE_TIMEKEEPING_NAME + " ON " + TABLE_STAFF_NAME + ".manhanvien = " + TABLE_TIMEKEEPING_NAME + ".manhanvien WHERE strftime('%d', " + TABLE_TIMEKEEPING_NAME + ".timekeeping) = '" + fm24hStr + "' ORDER BY " + TABLE_TIMEKEEPING_NAME + ".timekeeping DESC", null);
        List<Staff> staffs = new ArrayList<>();
        while (cursor.moveToNext()){
            Staff staff = convertCursorToStaff(cursor);
            String dateStr = cursor.getString(6);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateD = null;
            try {
                 dateD = dateFormat.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            staff.setDate(dateD);
            staffs.add(staff);
        }
        return staffs;
    }

    public boolean hasTimeKeeping(String mnv, Date date){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_STAFF_NAME + " LEFT JOIN " + TABLE_TIMEKEEPING_NAME + " ON " + TABLE_STAFF_NAME + ".manhanvien = " + TABLE_TIMEKEEPING_NAME + ".manhanvien WHERE " + TABLE_STAFF_NAME + ".manhanvien = '" + mnv + "' AND strftime('%d', " + TABLE_TIMEKEEPING_NAME + ".timekeeping) = '" + date.getDate() + "'", null);
        if (!cursor.moveToNext()){
            return false;
        }
        return true;
    }

    public void timeKeepingNow(String mnv){
        SQLiteDatabase database = getWritableDatabase();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time24hFmStr = df.format(new Date());
        database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, '" + mnv + "', '" + time24hFmStr + "')");
    }

    public boolean isStaffExists(String mnv){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_STAFF_NAME + " WHERE manhanvien = '" + mnv + "'", null);
        if (cursor.moveToNext()){
            return true;
        }
        return false;
    }

    public List<Staff> getAllUser() {
        List<Staff> staffs = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_STAFF_NAME, null);
        while (cursor.moveToNext()){
            staffs.add(convertCursorToStaff(cursor));
        }
        return staffs;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private Staff convertCursorToStaff(Cursor cursor){
        Staff staff = new Staff();
        staff.setMaNhanVien(cursor.getString(0));
        staff.setTenNhanVien(cursor.getString(1));
        staff.setNgaySinh(cursor.getString(2));
        staff.setViTri(cursor.getString(3));
        return staff;
    }

    //Create test data!
    public boolean createTestDatabase(){
        try {
            SQLiteDatabase database = getWritableDatabase();
            //Create tables
            database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_STAFF_NAME + "(manhanvien NVARCHAR(50) PRIMARY KEY, tennhanvien NVARCHAR(100) NOT NULL, ngaysinh NVARCHAR(20) NOT NULL, vitri NVARCHAR(100) NOT NULL)");
            database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TIMEKEEPING_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, manhanvien NVARCHAR(50) NOT NULL, timekeeping DATETIME NOT NULL)");

            //Create demo staffs data
            database.execSQL("INSERT INTO staffs VALUES( 'HVCG001', 'Hoàng Mạnh Tiến', '22/02/1998', 'Bảo vệ')");
            database.execSQL("INSERT INTO staffs VALUES( 'HVCG002', 'Bùi Quang Hiếu', '10/10/1998', 'Trưởng phòng')");
            database.execSQL("INSERT INTO staffs VALUES( 'HVCG003', 'Trần Phương Thảo', '22/06/1998', 'Sản xuất')");
            database.execSQL("INSERT INTO staffs VALUES( 'HVCG004', 'Nguyễn Thị Thùy Linh', '29/09/1998', 'Thực tập')");
            database.execSQL("INSERT INTO staffs VALUES( 'HVCG005', 'Nguyễn Trường Giang', '22/05/1998', 'Xây dựng')");
            database.execSQL("INSERT INTO staffs VALUES( 'HVCG006', 'Đỗ Thanh Bình', '07/02/1998', 'Công nhân')");
            database.execSQL("INSERT INTO staffs VALUES( 'HVCG007', 'Trần Thị Tuyết', '15/09/1998', 'Công nhân')");
            database.execSQL("INSERT INTO staffs VALUES( 'HVCG008', 'Hoàng Thu Sương', '07/05/1998', 'Công nhân')");
            database.execSQL("INSERT INTO staffs VALUES( 'HVCG009', 'Nguyễn Kim Thoa', '11/02/1998', 'Công nhân')");
            database.execSQL("INSERT INTO staffs VALUES( 'HVCG010', 'Nguyễn Mỹ Hạnh', '15/02/1998', 'Công nhân')");

            //mã chưa quét
//            database.execSQL("INSERT INTO staffs VALUES( 'HVCG011', 'Phạm Nhật Trường', '09/09/1998', 'Công nhân')");
//            database.execSQL("INSERT INTO staffs VALUES( 'HVCG012', 'Phạm Thị Nguyệt', '01/01/1998', 'Công nhân')");
//            database.execSQL("INSERT INTO staffs VALUES( 'HVCG013', 'Hoàng Ngọc Ánh', '14/02/1998', 'Công nhân')");
//            database.execSQL("INSERT INTO staffs VALUES( 'HVCG014', 'Trần Thị Tuyết', '15/06/1998', 'Công nhân')");
//            database.execSQL("INSERT INTO staffs VALUES( 'HVCG015', 'Trần Quốc Toản', '13/03/1998', 'Công nhân')");

            //Create demo timeKeeping data
            //30/07/2020
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG001', '2020-08-01 10:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG002', '2020-08-01 09:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG003', '2020-08-01 10:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG004', '2020-08-01 05:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG005', '2020-08-01 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG006', '2020-08-01 09:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG007', '2020-08-01 11:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG008', '2020-08-01 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG009', '2020-08-01 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG010', '2020-08-01 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG011', '2020-08-01 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG012', '2020-08-01 08:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG013', '2020-08-01 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG014', '2020-08-01 09:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG015', '2020-08-01 11:00:00')");
            //29/07/2020
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG001', '2020-08-02 10:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG002', '2020-08-02 09:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG003', '2020-08-02 10:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG004', '2020-08-02 05:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG005', '2020-08-02 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG006', '2020-08-02 09:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG007', '2020-08-02 11:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG008', '2020-08-02 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG009', '2020-08-02 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG010', '2020-08-02 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG011', '2020-08-02 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG012', '2020-08-02 08:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG013', '2020-08-02 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG014', '2020-08-02 09:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG015', '2020-08-02 11:00:00')");
            //28/07/2020
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG001', '2020-08-03 10:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG002', '2020-08-03 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG003', '2020-08-03 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG004', '2020-08-03 05:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG005', '2020-08-03 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG006', '2020-08-03 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG007', '2020-08-03 11:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG008', '2020-08-03 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG009', '2020-08-03 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG010', '2020-08-03 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG011', '2020-08-03 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG012', '2020-08-03 08:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG013', '2020-08-03 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG014', '2020-08-03 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG015', '2020-08-03 06:00:00')");

            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG001', '2020-08-04 10:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG002', '2020-08-04 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG003', '2020-08-04 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG004', '2020-08-04 05:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG005', '2020-08-04 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG006', '2020-08-04 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG007', '2020-08-04 11:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG008', '2020-08-04 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG009', '2020-08-04 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG010', '2020-08-04 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG011', '2020-08-04 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG012', '2020-08-04 08:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG013', '2020-08-04 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG014', '2020-08-04 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG015', '2020-08-04 06:00:00')");

            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG001', '2020-08-05 10:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG002', '2020-08-05 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG003', '2020-08-05 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG004', '2020-08-05 05:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG005', '2020-08-05 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG006', '2020-08-05 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG007', '2020-08-05 11:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG008', '2020-08-05 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG009', '2020-08-05 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG010', '2020-08-05 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG011', '2020-08-05 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG012', '2020-08-05 08:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG013', '2020-08-05 07:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG014', '2020-08-05 06:00:00')");
            database.execSQL("INSERT INTO " + TABLE_TIMEKEEPING_NAME + " VALUES(null, 'HVCG015', '2020-08-05 06:00:00')");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
