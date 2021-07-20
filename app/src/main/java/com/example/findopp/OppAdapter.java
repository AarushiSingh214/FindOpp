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
        public TextView tvTitle;
        private ImageView ivOpenHeart;

//        Intent i = new Intent(this, SearchResultsActivity.class);
//        i.putExtra("location", etLocation.getText().toString());

        //getting arrayList of the liked opportunities for current user that will be added to profile page
        ParseUser currentUser = ParseUser.getCurrentUser();
        //ArrayList<String> likedId = (ArrayList<String>) currentUser.get("userLikes");

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivOpenHeart = itemView.findViewById(R.id.ivOpenHeart);
        }


        public void bind(Opportunity opportunity) {
            tvTitle.setText(opportunity.getTitle());
            Intent i = new Intent(context, HomeFragment.class);
            i.putExtra("tvTitle", tvTitle.getText().toString());

            titleAction(opportunity);
            likeHeart(opportunity);
            saveHeart(opportunity);

        }

        //saves the likes after a user logs in and in between switching screens
        public void saveHeart(Opportunity opportunity) {
//            try {
//                if (!likedId.isEmpty()) {
//                    for (String user : likedId) {
//                        if (user.equals(opportunity.getName())) {
//                            ivOpenHeart.setImageResource(R.drawable.filled_heart);
//                        }
//                    }
//                }
//            } catch (NullPointerException e) {
//                Log.e(TAG, "nothing is hearted" + e);
//
//            }
            return;
        }

//        //action after title of the opportunity is clicked to see more details
        public void titleAction(Opportunity opportunity) {
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(context, OppDetailsActivity.class);
                    intent.putExtra("opportunity", Parcels.wrap(opportunity));
                    context.startActivity(intent);

//                    try {
//                        Intent intent = new Intent(context, OppDetailsActivity.class);
//                        intent.putExtra("opportunity", Parcels.wrap(opportunity));
//                        context.startActivity(intent);
//                    }catch(IllegalStateException e){
//                        Log.i(TAG, "illegal state exception " + e);
//                        return;
//
                    }
                });
            }


        //action after like heart button is clicked
        public void likeHeart(Opportunity opportunity) {
            ivOpenHeart.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objectId = opportunity.getName();
                    Likes likes = new Likes();


                    //if user has no likes
//                    if(likedId == null) {
//                        likedId = new ArrayList<String>();

                    //user adding a like
                    //if the user has not liked that specific opportunity, then like it
                    if (!likes.getUserName().equals(objectId)) {
                        Log.i(TAG, "adding this user: " + objectId);
                        ivOpenHeart.setImageResource(R.drawable.filled_heart);
                        //likedId.add(objectId);

                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    // if no success
                                    Log.e(TAG, "error saving " + e);
                                    return;
                                } else {
                                    Log.i(TAG, "adding heart" );
                                    likes.put("userName", currentUser);
                                    likes.put("opportunity", objectId);
                                }
                            }
                        });

                    //user removing a like
                    } else if (likes.getUserName().equals(objectId)) {
                        ivOpenHeart.setImageResource(R.drawable.open_heart);
                        //likedId.remove(objectId);

                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    return;
                                } else {
//                                    likes.remove("userName", currentUser);
//                                    likes.put("opportunity", objectId);
                                    Log.i(TAG, " removing heart" );

                                }

                            }

                        });

                    }
                }

            }));
        }
    }
}

