package com.acpay.acapytrade.OrderOperations;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.acpay.acapytrade.OrderOperations.progress.boxesAdapter;
import com.ramotion.foldingcell.FoldingCell;
import com.acpay.acapytrade.R;

import java.util.HashSet;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class OrderFoldingCellAdapter extends ArrayAdapter<Order> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultPendingBtnClickListener;
    private View.OnClickListener defaultaddNotesBtnClickListener;
    private View.OnClickListener defaultEnableBtnClickListener;
    private View.OnClickListener defaultDeleteBtnClickListener;
    private View.OnClickListener defaultEditBtnClickListener;
    private View.OnClickListener defaulttogBtnClickListener;
    private View.OnClickListener toggle;

    private int PendingBtn;
    private int addNotes;
    private int EnableBtn;
    private int DeleteBtn;
    private int EditBtn;
    public OrderFoldingCellAdapter(Context context, List<Order> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        Order item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell rootview = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (rootview == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            rootview = (FoldingCell) vi.inflate(R.layout.orders_activity_list, parent, false);

            viewHolder.orderNum = rootview.findViewById(R.id.title_orderNum);
            viewHolder.content_orderNum = rootview.findViewById(R.id.content_orderNum);
            viewHolder.UserName = (TextView) rootview.findViewById(R.id.title_username);
            viewHolder.time = rootview.findViewById(R.id.title_time_label);
            viewHolder.date = rootview.findViewById(R.id.title_date_label);
            viewHolder.place = rootview.findViewById(R.id.title_place);
            viewHolder.location = rootview.findViewById(R.id.title_location);
            viewHolder.dliverCost = rootview.findViewById(R.id.title_dliverCost);
            viewHolder.classMatter = rootview.findViewById(R.id.title_matter);
            viewHolder.fixType = rootview.findViewById(R.id.title_fixType);

            viewHolder.content_time = rootview.findViewById(R.id.content_time_label);
            viewHolder.content_date = rootview.findViewById(R.id.content_date_label);
            viewHolder.content_place = rootview.findViewById(R.id.content_place);
            viewHolder.content_location = rootview.findViewById(R.id.content_location);
            viewHolder.content_dliverCost = rootview.findViewById(R.id.content_dliverCost);
            viewHolder.content_classMatter = rootview.findViewById(R.id.content_matter);
            viewHolder.content_fixType = rootview.findViewById(R.id.content_fixType);
            viewHolder.content_notes = rootview.findViewById(R.id.content_notes);

            viewHolder.progressList = rootview.findViewById(R.id.checkBoxesList);
            viewHolder.emptyList=rootview.findViewById(R.id.checkBoxesListNoItems);
            viewHolder.listProg=rootview.findViewById(R.id.checkBoxesListProgress);

            viewHolder.addNote = rootview.findViewById(R.id.content_note_btn);
            viewHolder.Delete = rootview.findViewById(R.id.content_delete_btn);
            viewHolder.Edite = rootview.findViewById(R.id.content_edite_btn);
            viewHolder.Active = rootview.findViewById(R.id.content_enable_btn);
            viewHolder.Pending = rootview.findViewById(R.id.content_pending_btn);

            viewHolder.tog=rootview.findViewById(R.id.toggle);
            rootview.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                rootview.unfold(true);
            } else {
                rootview.fold(true);
            }
            viewHolder = (ViewHolder) rootview.getTag();
        }

        if (null == item)
            return rootview;

        // bind data from selected element to view through view holder
        viewHolder.orderNum.setText(item.getOrderNum());
        viewHolder.content_orderNum.setText(item.getUserName());
        viewHolder.UserName.setText(item.getUserName());
        viewHolder.time.setText(item.getTime());
        viewHolder.date.setText(item.getDate());
        viewHolder.place.setText(item.getPlace());
        viewHolder.location.setText(item.getLocation());
        viewHolder.dliverCost.setText(item.getDliverCost());
        viewHolder.classMatter.setText(item.getClassMatter());
        viewHolder.fixType.setText(item.getFixType());
        viewHolder.content_time.setText(item.getTime());
        viewHolder.content_date.setText(item.getDate());
        viewHolder.content_place.setText(item.getPlace());
        viewHolder.content_location.setText(item.getLocation());
        viewHolder.content_dliverCost.setText(item.getDliverCost());
        viewHolder.content_classMatter.setText(item.getClassMatter());
        viewHolder.content_fixType.setText(item.getFixType());
        viewHolder.content_notes.setText(item.getNotes());
        if (item.getProgressList().size() > 0){
            boxesAdapter adapter = new boxesAdapter(getContext(),item.getProgressList());
            viewHolder.progressList.setAdapter(adapter);
            viewHolder.listProg.setVisibility(View.GONE);
            viewHolder.emptyList.setVisibility(View.GONE);
        }else {
            viewHolder.listProg.setVisibility(View.GONE);
            viewHolder.emptyList.setText("لا يوجد خطوات");
        }


        viewHolder.addNote.setVisibility(item.getAddNotes());
        viewHolder.Delete.setVisibility(item.getDeleteBtn());
        viewHolder.Edite.setVisibility(item.getEditBtn());
        viewHolder.Active.setVisibility(item.getEnableBtn());
        viewHolder.Pending.setVisibility(item.getPendingBtn());

        if (item.getAddNotes() != -1) {
            viewHolder.addNote.setVisibility(item.getAddNotes());
        } else {
            viewHolder.addNote.setVisibility(addNotes);
        }

        if (item.getEditBtn() != -1) {
            viewHolder.Edite.setVisibility(item.getEditBtn());
        } else {
            viewHolder.Edite.setVisibility(EditBtn);
        }

        if (item.getDeleteBtn() != -1) {
            viewHolder.Delete.setVisibility(item.getDeleteBtn());
        } else {
            viewHolder.Delete.setVisibility(DeleteBtn);
        }

        if (item.getEnableBtn() != -1) {
            viewHolder.Active.setVisibility(item.getEnableBtn());
        } else {
            viewHolder.Active.setVisibility(EnableBtn);
        }

        if (item.getPendingBtn() != -1) {
            viewHolder.Pending.setVisibility(item.getPendingBtn());
        } else {
            viewHolder.Pending.setVisibility(PendingBtn);
        }

        if (item.getAddNotesBtnClickListener() != null) {
            viewHolder.addNote.setOnClickListener(item.getAddNotesBtnClickListener());
        } else {
            viewHolder.addNote.setOnClickListener(defaultaddNotesBtnClickListener);
        }

        if (item.getEditBtnClickListener() != null) {
            viewHolder.Edite.setOnClickListener(item.getEditBtnClickListener());
        } else {
            viewHolder.Edite.setOnClickListener(defaultEditBtnClickListener);
        }

        if (item.getDeleteBtnClickListener() != null) {
            viewHolder.Delete.setOnClickListener(item.getDeleteBtnClickListener());
        } else {
            viewHolder.Delete.setOnClickListener(defaultDeleteBtnClickListener);
        }

        if (item.getEnableBtnClickListener() != null) {
            viewHolder.Active.setOnClickListener(item.getEnableBtnClickListener());
        } else {
            viewHolder.Active.setOnClickListener(defaultEnableBtnClickListener);
        }

        if (item.getPendingBtnClickListener() != null) {
            viewHolder.Pending.setOnClickListener(item.getPendingBtnClickListener());
        } else {
            viewHolder.Pending.setOnClickListener(defaultPendingBtnClickListener);
        }
        if (item.getTogBtnClickListener() != null) {
            viewHolder.tog.setOnClickListener(item.getTogBtnClickListener());
        } else {
            viewHolder.tog.setOnClickListener(defaulttogBtnClickListener);
        }

        return rootview;
    }

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

    private static class ViewHolder {
        TextView orderNum;

        TextView content_orderNum;
        TextView UserName;

        TextView date;
        TextView content_date;

        TextView time;
        TextView content_time;

        TextView place;
        TextView content_place;

        TextView location;
        TextView content_location;

        TextView dliverCost;
        TextView content_dliverCost;

        TextView fixType;
        TextView content_fixType;
        TextView content_notes;

        TextView classMatter;
        TextView content_classMatter;

        ListView progressList;
        TextView emptyList;
        ProgressBar listProg;

        TextView addNote;
        TextView Edite;
        TextView Delete;
        TextView Active;
        TextView Pending;

        ImageView tog;
    }
}
