package hvcg.edu.timekeeping.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hvcg.edu.timekeeping.R;
import hvcg.edu.timekeeping.entities.Staff;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private Context context;
    private List<Staff> staffs;
    private int layout;

    public RecycleViewAdapter(Context context, List<Staff> staffs, int layout) {
        this.context = context;
        this.staffs = staffs;
        this.layout = layout;
    }

    public void addData(Staff staff){
        this.staffs.add(0, staff);
        notifyItemChanged(0);
    }

    public void addData(List<Staff> staffs){
        this.staffs.clear();
        this.staffs.addAll(staffs);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Staff staff = this.staffs.get(position);
        holder.tvName.setText(staff.getTenNhanVien());
        holder.tvStaffId.setText(staff.getMaNhanVien());
        holder.tvViTri.setText(staff.getViTri());
        setStatus(holder, staff);
        Glide.with(context).load(R.drawable.icon_user_100).into(holder.imvAvatar);
    }

    private void setStatus(ViewHolder holder, Staff staff) {
        Date date = staff.getDate();
        SimpleDateFormat simpleDateFormatArrivals = new SimpleDateFormat("HH", Locale.UK);
        String hour = simpleDateFormatArrivals.format(date);
        Log.d("testDate", "value: " + date.toString());
        Log.d("testDate", "value: " + hour);

        if (Integer.parseInt(hour) <= 8){
            Glide.with(context).load(R.drawable.icon_ok_30).into(holder.imvStatus);
            holder.tvStatus.setText(R.string.time_keeping_status_ok);
        }else {
            Glide.with(context).load(R.drawable.icon_cancel_30).into(holder.imvStatus);
            holder.tvStatus.setText(R.string.time_keeping_status_late);
        }
    }

    @Override
    public int getItemCount() {
        if (this.staffs != null){
            return this.staffs.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvStaffId;
        private TextView tvViTri;
        private TextView tvStatus;
        private ImageView imvStatus;
        private ImageView imvAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvStaffId = itemView.findViewById(R.id.tvStaffId);
            tvViTri = itemView.findViewById(R.id.tvViTri);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            imvStatus = itemView.findViewById(R.id.imvStatus);
            imvAvatar = itemView.findViewById(R.id.imvAvatar);
        }
    }
}
