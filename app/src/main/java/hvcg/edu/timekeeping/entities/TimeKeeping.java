package hvcg.edu.timekeeping.entities;

import java.util.Date;

public class TimeKeeping {
    private int id;
    private String maNhanVien;
    private Date date;

    public TimeKeeping(int id, String maNhanVien, Date date) {
        this.id = id;
        this.maNhanVien = maNhanVien;
        this.date = date;
    }

    public TimeKeeping() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
