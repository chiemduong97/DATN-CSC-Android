package com.example.client.screens.category.item;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.service.SubjectService;
import com.example.client.app.Constrants;
import com.example.client.models.category.CategoryModel;
import com.example.client.models.message.MessageModel;
import com.example.client.models.subject.SubjectModel;
import com.example.client.screens.subject.activity.ISubjectView;
import com.example.client.screens.subject.activity.SubjectMoreActivity;
import com.example.client.screens.subject.item.SubjectVerticalItem;
import com.example.client.screens.subject.present.SubjectPresent;
import com.example.client.utils.OnShowSubjects;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryItem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CategoryModel> items;
    private Context context;

    public CategoryItem(List<CategoryModel> items, Context context){
        this.items = items;
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements ISubjectView{
        private TextView name;
        private TextView more;
        private RecyclerView recyclerView;
        private ImageView empty;
        private SubjectPresent sPresent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            more = itemView.findViewById(R.id.more);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            empty = itemView.findViewById(R.id.empty);
            sPresent = new SubjectPresent(this);
        }
        public void showSubjects(int category){
            sPresent.onShowSubjectsByCategory(category);
        }


        @Override
        public void showSubject(SubjectModel item) {

        }

        @Override
        public void showSubjectsByCategory(List<SubjectModel> items) {
            if(items == null || items.size() == 0){
                empty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            else {
                empty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                LinearLayoutManager managerHightLight = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
                SubjectVerticalItem subjectVerticalItem = new SubjectVerticalItem(items, context);
                recyclerView.setLayoutManager(managerHightLight);
                recyclerView.setAdapter(subjectVerticalItem);
            }
        }

        @Override
        public void showMoreSubjects(List<SubjectModel> items) {

        }

        @Override
        public void showSubjectByUser(List<SubjectModel> items) {

        }

        @Override
        public void registerSubject(MessageModel message) {

        }

        @Override
        public void checkEnableRegister(MessageModel message) {

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CategoryModel item = items.get(position);
        ((MyViewHolder)holder).name.setText(item.getName());
        ((MyViewHolder)holder).showSubjects(item.getId());
        ((MyViewHolder)holder).more.setOnClickListener(v -> {
            Intent intent = new Intent(context, SubjectMoreActivity.class);
            intent.putExtra("id", item.getId());
            intent.putExtra("name",item.getName());
            intent.putExtra("method", Constrants.MORE.CATEGORY);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
