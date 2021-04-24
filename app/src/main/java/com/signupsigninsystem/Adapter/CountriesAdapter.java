package com.signupsigninsystem.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.signupsigninsystem.Model.CountryModel;
import com.signupsigninsystem.R;
import com.signupsigninsystem.RegisterActivity;

import java.util.ArrayList;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CountryModel> countryModelList;
    private Dialog countryDialog;
    private ImageView countryImg;
    private TextView txtDiaCode;

    public CountriesAdapter(Context context, ArrayList<CountryModel> countryModelList, Dialog countryDialog, ImageView countryImg, TextView txtDiaCode) {
        this.context = context;
        this.countryModelList = countryModelList;
        this.countryDialog = countryDialog;
        this.countryImg = countryImg;
        this.txtDiaCode = txtDiaCode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(countryModelList.get(position).getFlag(), countryModelList.get(position).getName(), countryModelList.get(position).getDialCode(), countryModelList.get(position).getFlag(),countryModelList.get(position).getCode());
    }

    @Override
    public int getItemCount() {
        return countryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout layoutClick;
        private ImageView flag;
        private TextView name, code;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flag = itemView.findViewById(R.id.country_flag);
            name = itemView.findViewById(R.id.country_title);
            code = itemView.findViewById(R.id.country_code);
            layoutClick = itemView.findViewById(R.id.rootView);
        }

        private void setData(final int imageFlag, final String countryName, final String countryCode, int flagImg,String codeStr) {
            flag.setImageResource(imageFlag);
            name.setText(countryName);
            code.setText(countryCode);

            layoutClick.setOnClickListener(v -> {
                countryImg.setImageResource(flagImg);
                switch (context.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK) {
                    case Configuration.UI_MODE_NIGHT_NO:  // Light Theme
                        txtDiaCode.setTextColor(context.getResources().getColor(R.color.black));
                        break;
                    case Configuration.UI_MODE_NIGHT_YES: // Dark Theme
                        txtDiaCode.setTextColor(context.getResources().getColor(R.color.white));
                        break;
                }
                RegisterActivity.codeStr = codeStr;
                RegisterActivity.countryName = countryName;
                RegisterActivity.dialCode = countryCode;
                txtDiaCode.setText(countryCode);
                countryDialog.dismiss();
            });
        }
    }

    public void filterList(ArrayList<CountryModel> filteredList) {
        countryModelList = filteredList;
        notifyDataSetChanged();
    }
}
