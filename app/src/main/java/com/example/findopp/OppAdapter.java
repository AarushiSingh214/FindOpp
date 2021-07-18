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

import com.example.findopp.fragments.HomeFragment;
import com.example.findopp.fragments.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class OppAdapter extends RecyclerView.Adapter<OppAdapter.ViewHolder> {
    private Context context;
    private List<Opportunity> opportunities;
    public static final String TAG = "Opp Adapter";
    String objectId;

    public OppAdapter(Context context, List<Opportunity> opportunities) {
        this.context = context;
        this.opportunities = opportunities;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView ivOpenHeart;

        //getting arrayList of the liked opportunities for current user that will be added to profile page
        ParseUser currentUser = ParseUser.getCurrentUser();
        ArrayList<String> likedId = (ArrayList<String>) currentUser.get("userLikes");

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivOpenHeart = itemView.findViewById(R.id.ivOpenHeart);
        }

        public void bind(Opportunity opportunity) {
            tvTitle.setText(opportunity.getTitle());
            likeHeart(opportunity);
            titleAction(opportunity);

        }

        //action after title of the opportunity is clicked
        public void titleAction(Opportunity opportunity) {
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OppDetailsActivity.class);
                    intent.putExtra("opportunity", Parcels.wrap(opportunity));
                    context.startActivity(intent);
                }
            });

        }

        //action after like heart button is clicked
        public void likeHeart(Opportunity opportunity) {
            ivOpenHeart.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objectId = opportunity.getName();
                    if (!likedId.contains(objectId)) {
                        Log.i(TAG, "adding this user: " + objectId);
                        ivOpenHeart.setImageResource(R.drawable.filled_heart);
                        likedId.add(objectId);

                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    // if no success
                                    Log.e(TAG, "error saving " + e);
                                    return;
                                } else {
                                    Log.i(TAG, "adding heart" + likedId);
                                    ParseUser.getCurrentUser().put("userLikes", likedId);
                                    ParseUser.getCurrentUser().put("test", "hello");
                                }
                            }
                        });

                    } else if (likedId.contains(objectId)) {

                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    return;
                                } else {
                                    ivOpenHeart.setImageResource(R.drawable.open_heart);
                                    likedId.remove(objectId);
                                    ParseUser.getCurrentUser().put("userLikes", likedId);
                                    //ParseUser.getCurrentUser().put("userLikes", likedId);
                                    Log.i(TAG, " removing heart" + likedId);
                                    ParseUser.getCurrentUser().put("test", "bye");
                                }

                            }

                        });


                    }
                }

            }));
        }
    }
}



//        public void createLikes(RecyclerView rvOpps) {
//            //trying to get the heart to appear when u login again
//            if(likedId != null) {
//                for (int i = 0; i <= likedId.size(); i++) {
//                    //how does it know which heart to fill
//                    ivOpenHeart.setImageResource(R.drawable.filled_heart);
//                    Log.i(TAG, "array: " + oppUser);
//
//                }
//            }
//
//        }




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



