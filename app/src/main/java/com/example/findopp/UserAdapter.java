package com.example.findopp;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findopp.models.User;

import java.util.List;

public class UserAdapter {
    private Context context;
    private List<User> users;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
//    @Override
//    public OppAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_opp, parent, false);
//        return new UserAdapter.ViewHolder(view);
//
//    }

//    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUserName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
        }

        public void bind(User user) {
            tvUserName.setText(user.getUserName());
        }
    }

}




