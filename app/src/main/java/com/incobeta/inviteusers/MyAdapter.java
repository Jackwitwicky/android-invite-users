package com.incobeta.inviteusers;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by witwicky on 16/02/18.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.PersonViewHolder> {

    private List<Person> people;
    private OnItemClickListener mItemClickListener;

    public MyAdapter(List<Person> persons) {
        this.people = persons;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_invite, viewGroup, false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.personName.setText(people.get(i).getName());
        personViewHolder.personPhone.setText(people.get(i).getPhone());
        personViewHolder.personPhoto.setImageBitmap(people.get(i).getPhoto());
        personViewHolder.cbSelected.setChecked(people.get(i).isSelected());

    }

    public void setItemSelected(int position, boolean isSelected) {
        if (position != -1) {
            people.get(position).setSelected(isSelected);
            notifyDataSetChanged();
        }
    }

    List<Person> getPeople(){
        return people;
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        CardView cv;
        TextView personName;
        TextView personPhone;
        ImageView personPhoto;
        CheckBox cbSelected;

        PersonViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cv = itemView.findViewById(R.id.cv);
            personName = itemView.findViewById(R.id.person_name);
            personPhone = itemView.findViewById(R.id.person_phone);
            personPhoto = itemView.findViewById(R.id.person_photo);
            cbSelected = itemView.findViewById(R.id.cbSelected);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
