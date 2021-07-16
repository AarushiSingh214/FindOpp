package com.example.findopp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findopp.fragments.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class OppAdapter extends RecyclerView.Adapter<OppAdapter.ViewHolder>{
    private Context context;
    private List<Opportunity> opportunities;
    public static final String TAG = "Opp Adapter";
    private FirebaseAuth fireBaseUser;
    User user = new User();

    public OppAdapter(Context context, List<Opportunity> opportunities) {
        this.context = context;
        this.opportunities = opportunities;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_opp, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Opportunity opportunity = opportunities.get(position);
        holder.bind(opportunity);
    }

    public void clear() {
        opportunities.clear();
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return opportunities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView ivOpenHeart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivOpenHeart = itemView.findViewById(R.id.ivOpenHeart);
            //fireBaseUser = FirebaseAuth.getInstance();
        }

        public void bind(Opportunity opportunity) {
            tvTitle.setText(opportunity.getTitle());

            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OppDetailsActivity.class);
                    intent.putExtra("opportunity", Parcels.wrap(opportunity));
                    context.startActivity(intent);
                        }
                    });

            //THIS WORKS TO LIKE AND UNLIKE A HEART
            ivOpenHeart.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String objectId = opportunity.getObjectId();
                    if(!user.likedId.contains(objectId)){
                        ivOpenHeart.setImageResource(R.drawable.filled_heart);
                        user.likedId.add(user.getObjectId());
                        ParseUser.getCurrentUser().put("userLikes", user.likedId);
                    }else{
                        ivOpenHeart.setImageResource(R.drawable.open_heart);
                        user.likedId.remove(user.getObjectId());
                        ParseUser.getCurrentUser().delete("userLikes", user.likedId);

                    }
                }







//                    if (opportunity.getLikes() == "false"){
//                        ivOpenHeart.setImageResource(R.drawable.filled_heart);
//                        opportunity.setLikes("true");
//                        user.likedId.add(user.getObjectId());
//                        //ParseUser.getCurrentUser().put("likes", opportunity.getLikes());
//
////                        Intent intent = new Intent(context, ProfileFragment.class);
////                        intent.putExtra("opportunity", Parcels.wrap(opportunity));
////                        context.startActivity(intent);
//
//                    }else{
//                        ivOpenHeart.setImageResource(R.drawable.open_heart);
//                        opportunity.setLikes("false");
//
//                    }
//                }
//            }));

//            if(opportunity.getLikes() == "true"){
//                Intent intent = new Intent(context, ProfileFragment.class);
//                intent.putExtra("opportunity", Parcels.wrap(opportunity));
//                context.startActivity(intent);

//            }


//            private void isLikes(String postid, ImageView imageview){
//                FireBaseUser fireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
//            }

            //change item view to the heart
//            ivOpenHeart.setOnTouchListener(new View.OnTouchListener() {
//                GestureDetector gestureDetector = new GestureDetector(context.getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
//                    @Override
//                    public boolean onDoubleTap(MotionEvent e) {
//                        Toast.makeText(context.getApplicationContext(), "double tap", Toast.LENGTH_SHORT).show();
//                        //ivOpenHeart.setImageResource(R.drawable.filled_heart);
//                        return super.onDoubleTap(e);
//                    }
//
//                });
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    gestureDetector.onTouchEvent(event);
//                    return false;
//                }
//            });


        }));
     }

    }
}
