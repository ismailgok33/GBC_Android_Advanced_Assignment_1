package com.example.a1_ismail.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1_ismail.OnCharacterClickListener;
import com.example.a1_ismail.R;
import com.example.a1_ismail.databinding.ItemPeopleBinding;
import com.example.a1_ismail.models.People;

import java.util.ArrayList;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {
    private final Context context;
    private final ArrayList<People> peopleArrayList;
    ItemPeopleBinding binding;
    private final OnCharacterClickListener clickListener;

    public PeopleAdapter(Context context, ArrayList<People> peopleArrayList, OnCharacterClickListener clickListener){
        this.peopleArrayList = peopleArrayList;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PeopleViewHolder(ItemPeopleBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleViewHolder holder, int position) {
        People currentPeople = peopleArrayList.get(position);
        holder.bind(context, currentPeople, clickListener);
    }

    @Override
    public int getItemCount() {
        return this.peopleArrayList.size();
    }

    public static class PeopleViewHolder extends RecyclerView.ViewHolder{
        ItemPeopleBinding itemBinding;

        public PeopleViewHolder(ItemPeopleBinding binding){
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        public void bind(Context context, People currentPeople, OnCharacterClickListener clickListener){
            itemBinding.tvName.setText(currentPeople.getName());
            itemBinding.tvSpecies.setText(currentPeople.getSpecies().size() > 0 ? context.getString(R.string.not_human) : context.getString(R.string.human));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onCharacterItemClicked(currentPeople);
                }
            });
        }
    }
}
