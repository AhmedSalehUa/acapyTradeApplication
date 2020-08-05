package com.acpay.acapytrade.oreder;

import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acpay.acapytrade.R;
import com.acpay.acapytrade.floadingCell.FoldingCell;

import java.util.HashSet;
import java.util.List;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class OrderAdapter extends ArrayAdapter<Order> {
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    private View.OnClickListener defaultPendingBtnClickListener;
    private View.OnClickListener defaultEnableBtnClickListener;
    private View.OnClickListener defaultAddNotesBtnClickListener;
    private View.OnClickListener defaultDeleteBtnClickListener;

    private Visibility requestBtn;
    private Visibility addNotes;
    private Visibility EnableBtn;
    private Visibility DeleteBtn;

    int view;
    public OrderAdapter(Context context, List<Order> objects,int view) {
        super(context, 0, objects);
        this.view = view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Order item = getItem(position);

        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.orders_activity_list, parent, false);

            viewHolder.orderNum = cell.findViewById(R.id.title_orderNum);
            viewHolder.userName = cell.findViewById(R.id.title_username);
            viewHolder.time = cell.findViewById(R.id.title_time_label);
            viewHolder.date = cell.findViewById(R.id.title_date_label);
            viewHolder.place = cell.findViewById(R.id.title_place);
            viewHolder.location = cell.findViewById(R.id.title_location);
            viewHolder.dliverCost = cell.findViewById(R.id.title_dliverCost);
            viewHolder.classMatter = cell.findViewById(R.id.title_matter);
            viewHolder.fixType = cell.findViewById(R.id.title_fixType);

            viewHolder.ContenetorderNum = cell.findViewById(R.id.content_orderNum);
            viewHolder.Contenettime = cell.findViewById(R.id.content_time_label);
            viewHolder.Contenetdate = cell.findViewById(R.id.content_date_label);
            viewHolder.Contenetplace = cell.findViewById(R.id.content_place);
            viewHolder.Contenetlocation = cell.findViewById(R.id.content_location);
            viewHolder.ContenetdliverCost = cell.findViewById(R.id.content_dliverCost);
            viewHolder.ContenetclassMatter = cell.findViewById(R.id.title_matter);
            viewHolder.ContenetfixType = cell.findViewById(R.id.content_fixType);

            viewHolder.notes = cell.findViewById(R.id.content_notes);
            viewHolder.file = cell.findViewById(R.id.content_files);

            viewHolder.Enable = cell.findViewById(R.id.content_enable_btn);
            viewHolder.AddNotes = cell.findViewById(R.id.content_note_btn);
            viewHolder.Delete = cell.findViewById(R.id.content_delete_btn);
            viewHolder.Pending = cell.findViewById(R.id.content_pending_btn);
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item) {
            return cell;
        }
        viewHolder.orderNum.setText(item.getOrderNum());
        viewHolder.userName.setText(item.getUserName());
        viewHolder.ContenetorderNum.setText(item.getOrderNum());

        viewHolder.time.setText(item.getTime());
        viewHolder.Contenettime.setText(item.getTime());

        viewHolder.date.setText(item.getDate());
        viewHolder.Contenetdate.setText(item.getDate());

        viewHolder.place.setText(item.getPlace());
        viewHolder.Contenetplace.setText(item.getPlace());

        viewHolder.location.setText(item.getLocation());
        viewHolder.Contenetlocation.setText(item.getLocation());

        viewHolder.dliverCost.setText(item.getDliverCost());
        viewHolder.ContenetdliverCost.setText(item.getDliverCost());

        viewHolder.classMatter.setText(item.getClassMatter());
        viewHolder.ContenetclassMatter.setText(item.getClassMatter());

        viewHolder.fixType.setText(item.getFixType());
        viewHolder.ContenetfixType.setText(item.getFixType());

        viewHolder.notes.setText(item.getNotes());
        viewHolder.file.setText(item.getFile());

        if (item.getPendingBtnClickListener() != null) {
            viewHolder.Pending.setOnClickListener(item.getPendingBtnClickListener());
        } else {
            // (optionally) add "default" handler if no handler found in item
            viewHolder.Pending.setOnClickListener(defaultPendingBtnClickListener);
        }

        if (item.getDeleteBtnClickListener() != null) {
            viewHolder.Delete.setOnClickListener(item.getDeleteBtnClickListener());
        } else {
            // (optionally) add "default" handler if no handler found in item
            viewHolder.Delete.setOnClickListener(defaultDeleteBtnClickListener);
        }

        if (item.getEnableBtnClickListener() != null) {
            viewHolder.Enable.setOnClickListener(item.getEnableBtnClickListener());
        } else {

            viewHolder.Enable.setOnClickListener(defaultEnableBtnClickListener);
        }

        if (item.getAddNotesBtnClickListener() != null) {
            viewHolder.AddNotes.setOnClickListener(item.getAddNotesBtnClickListener());
        } else {
            // (optionally) add "default" handler if no handler found in item
            viewHolder.AddNotes.setOnClickListener(defaultAddNotesBtnClickListener);
        }
        switch (view){
            case 1://mainForm
                viewHolder.AddNotes.setVisibility(View.VISIBLE);
                viewHolder.Enable.setVisibility(View.GONE);
                viewHolder.Delete.setVisibility(View.VISIBLE);
                viewHolder.Pending.setVisibility(View.VISIBLE);
                break;
            case 2://finished
                viewHolder.AddNotes.setVisibility(View.GONE);
                viewHolder.Enable.setVisibility(View.GONE);
                viewHolder.Delete.setVisibility(View.GONE);
                viewHolder.Pending.setVisibility(View.GONE);
                break;
            case 3://pending
                viewHolder.AddNotes.setVisibility(View.VISIBLE);
                viewHolder.Enable.setVisibility(View.VISIBLE);
                viewHolder.Delete.setVisibility(View.VISIBLE);
                viewHolder.Delete.setText("تم");
                viewHolder.Delete.setBackgroundColor(getContext().getResources().getColor(R.color.done));
                viewHolder.Pending.setVisibility(View.GONE);
                break;
            case 4://deleted
                viewHolder.AddNotes.setVisibility(View.GONE);
                viewHolder.Enable.setVisibility(View.VISIBLE);
                viewHolder.Delete.setVisibility(View.GONE);
                viewHolder.Pending.setVisibility(View.GONE);
                break;
        }
        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultPendingBtnClickListener() {
        return defaultPendingBtnClickListener;
    }

    public void setDefaultPendingBtnClickListener(View.OnClickListener defaultPendingBtnClickListener) {
        this.defaultPendingBtnClickListener = defaultPendingBtnClickListener;

    }

    private static class ViewHolder {
        TextView orderNum;
        TextView userName;
        TextView date;
        TextView time;
        TextView place;
        TextView location;
        TextView dliverCost;
        TextView fixType;
        TextView classMatter;
        TextView ContenetorderNum;
        TextView Contenetdate;
        TextView Contenettime;
        TextView Contenetplace;
        TextView Contenetlocation;
        TextView ContenetdliverCost;
        TextView ContenetfixType;
        TextView ContenetclassMatter;
        TextView notes;
        TextView file;

        TextView AddNotes;
        TextView Pending;
        TextView Delete;
        TextView Enable;

    }
}
