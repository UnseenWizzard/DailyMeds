package org.port0.nriedmann.dailymeds;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by captain on 30.08.2015.
 */
public class MedViewAdapter extends RecyclerView.Adapter<MedViewAdapter.MedViewHolder> {

    private ArrayList<Med> medList;
    private ViewGroup parent;
    private MainActivity parentAcitvity;

    public MedViewAdapter(MainActivity parentActivity, ArrayList<Med> list){
        super();
        this.parentAcitvity=parentActivity;
        this.medList=list;
    }
    public void updateMedList(ArrayList<Med> list){
        this.medList=list;
    }

    @Override
    public MedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent,false);
        this.parent=parent;
        return new MedViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(final MedViewHolder holder, final int position) {
        Med m = medList.get(position);
        holder.medIcon.setBackgroundColor(m.getColor(parent.getContext()));
        holder.medIcon.setImageDrawable(m.getImage(parent.getContext()));
        //holder.medIcon.getLayoutParams().height=holder.backgroundLayout.getLayoutParams().height;
//        holder.backgroundLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    //    holder.medIcon.getLayoutParams().height=holder.backgroundLayout.getHeight();
        holder.medName.setText(m.getName());
        holder.medDesc.setText(m.getDescription());
        holder.medTake.setText(m.getTakeDays(parent.getContext()) + " - " + m.getTakeHour() +":"+m.getTakeMinute());
        if (!m.isTakeTimeSet()){
            holder.medTake.setVisibility(View.GONE);
        } else {
            holder.medTake.setVisibility(View.VISIBLE);
        }
        if(m.isSilenced()){
            holder.silence.setImageResource(R.drawable.ic_alarm_off_black_24dp);
        } else {
            holder.silence.setImageResource(R.drawable.ic_alarm_on_black_24dp);
        }
        holder.medWait.setText(m.getWaitTime() + " min");
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), EditActivity.class);
                //add current med id from card view to intent
                intent.putExtra(Med.MED_ID, medList.get(position).getId());
                parentAcitvity.startActivityForResult(intent, 1);
            }
        });
        holder.silence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toast = parentAcitvity.getString(R.string.reminders_off);
                if (medList.get(position).isSilenced()){
                    toast = parentAcitvity.getString(R.string.reminders_on);
                    medList.get(position).setSilenced(false);
                    holder.silence.setImageResource(R.drawable.ic_alarm_on_black_24dp);
                    parentAcitvity.scheduleTakeAlarm(medList.get(position));
                } else {
                    medList.get(position).setSilenced(true);
                    holder.silence.setImageResource(R.drawable.ic_alarm_off_black_24dp);
                    parentAcitvity.cancelTakeAlarm(medList.get(position));
                }
                Toast.makeText(parent.getContext(), toast, Toast.LENGTH_LONG).show();
            }
        });
        holder.taken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //start takencounter
                TakeTimerScheduler.scheduleWaitAlarm(parentAcitvity, medList.get(position).getId(),medList.get(position).getName(),medList.get(position).getColor(parentAcitvity), medList.get(position).getWaitTime());
            }
        });
        holder.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandView(holder);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentAcitvity.deleteMed(medList.get(position).getId());
                expandView(holder);
            }
        });
    }

    private void expandView(MedViewHolder holder){
        if (!holder.expanded){
            holder.medDesc.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
            holder.expand.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_expand_less_white_24dp));
            holder.expanded=true;
            holder.medIcon.getLayoutParams().height=holder.bigHeight;
        } else {
            holder.medDesc.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            holder.expand.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_expand_more_white_24dp));
            holder.medIcon.getLayoutParams().height=holder.smallHeight;
            holder.expanded=false;
        }
    }

    @Override
    public int getItemCount() {
        return medList.size();
    }



    public static class MedViewHolder extends RecyclerView.ViewHolder{
        protected RelativeLayout backgroundLayout;
        protected ImageView medIcon;
        protected int smallHeight,bigHeight;
        protected TextView medName;
        protected TextView medDesc;
        protected TextView medTake;
        protected TextView medWait;
        protected ImageButton edit;
        protected ImageButton silence;
        protected ImageButton delete;
        protected ImageButton expand;
        protected boolean expanded;
        protected Button taken;

        public MedViewHolder(View v){
            super(v);
            backgroundLayout = (RelativeLayout) v.findViewById(R.id.cardBackground);
            medIcon = (ImageView) v.findViewById(R.id.cMedIcon);
            medName = (TextView) v.findViewById(R.id.cMedTop);
            medDesc = (TextView) v.findViewById(R.id.cMedDesc);
            medTake = (TextView) v.findViewById(R.id.cMedTake);
            medWait = (TextView) v.findViewById(R.id.cMedWait);
            edit = (ImageButton) v.findViewById(R.id.cButtonEdit);
            silence = (ImageButton) v.findViewById(R.id.cButtonSilence);
            delete = (ImageButton) v.findViewById(R.id.cButtonDelete);
            expand = (ImageButton) v.findViewById(R.id.cButtonExpand);
            expanded=false;
            taken = (Button) v.findViewById(R.id.cButtonTaken);
            ViewTreeObserver observer = backgroundLayout.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {

                    bigHeight=backgroundLayout.getHeight();
                    smallHeight=bigHeight-medDesc.getHeight();
                    medDesc.setVisibility(View.GONE);
                    medIcon.getLayoutParams().height=smallHeight;
                    backgroundLayout.getViewTreeObserver().removeGlobalOnLayoutListener(
                            this);
                }
            });
        }
    };
}
