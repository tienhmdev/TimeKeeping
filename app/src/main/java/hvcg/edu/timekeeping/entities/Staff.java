package hvcg.edu.timekeeping.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class Staff implements Parcelable {
    private String maNhanVien;
    private String tenNhanVien;
    private String ngaySinh;
    private String viTri;
    private Date date;

    public Staff(String maNhanVien, String tenNhanVien, String ngaySinh, String viTri) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.ngaySinh = ngaySinh;
        this.viTri = viTri;
    }

    public Staff() {
    }

    protected Staff(Parcel in) {
        maNhanVien = in.readString();
        tenNhanVien = in.readString();
        ngaySinh = in.readString();
        viTri = in.readString();
    }

    public static final Creator<Staff> CREATOR = new Creator<Staff>() {
        @Override
        public Staff createFromParcel(Parcel in) {
            return new Staff(in);
        }

        @Override
        public Staff[] newArray(int size) {
            return new Staff[size];
        }
    };

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

    @NonNull
    @Override
    public String toString() {
        return maNhanVien + " / " + tenNhanVien;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(maNhanVien);
        dest.writeString(tenNhanVien);
        dest.writeString(ngaySinh);
        dest.writeString(viTri);
    }
}
