package com.example.findopp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findopp.fragments.HomeFragment;
import com.example.findopp.fragments.ProfileFragment;
import com.example.findopp.models.Likes;
import com.example.findopp.models.Opportunity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class OppAdapter extends RecyclerView.Adapter<OppAdapter.ViewHolder> {
    private Context context;
    private List<Opportunity> opportunities;
    private ArrayList<Likes> oppsLikes = new ArrayList<Likes>();
    public static final String TAG = "Opp Adapter";

    public OppAdapter(Context context, List<Opportunity> opportunities) {
        this.context = context;
        this.opportunities = opportunities;
        saveHeart();
    }

    //gets all the opportunities that were liked by the user
    public void saveHeart() {
        ParseQuery<Likes> query = ParseQuery.getQuery(Likes.class);

        //trying to query where the user equals the current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        query.whereEqualTo("user", currentUser);

        query.findInBackground(new FindCallback<Likes>() {
            @Override
            public void done(List<Likes> likes, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting likes (saveHeart)", e);
                    return;
                } else {
                    Log.i(TAG, "size of likes (saveHeart)" + likes.size());
                    oppsLikes.addAll(likes);
                }
            }
        });
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
        public ImageView ivOpenHeart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivOpenHeart = itemView.findViewById(R.id.ivOpenHeart);

        }


        public void bind(Opportunity opportunity) {
            //tvTitle.setText(opportunity.getTitle());
            try {
                tvTitle.setText(opportunity.getTitle());
                Log.i(TAG, "opportunity.getTitle()" + opportunity.getTitle());


//                Intent intent = new Intent(context, HomeFragment.class);
//                intent.putExtra("tvTitle", tvTitle.getText().toString());
//
//                tvTitle.setText(opportunity.getTitle());
//                Intent intent2 = new Intent(context, ProfileFragment.class);
//                intent.putExtra("tvTitle", tvTitle.getText().toString());
            }catch(IllegalStateException e){
                Log.i(TAG, "error with tvTitle" + e);
            }

            titleAction(opportunity);
            displayLikes(opportunity);

            ivOpenHeart.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "in ivopenheart");
                    likes(opportunity);
                }
            }));

        }

        //action after title of the opportunity is clicked to see more details
        private void titleAction(Opportunity opportunity) {
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OppDetailsActivity.class);
                    intent.putExtra("opportunity", Parcels.wrap(opportunity));
                    context.startActivity(intent);
                }
            });
        }

        //saves the likes after a user logs in and in between switching screens
        private void displayLikes(Opportunity opportunity) {
            ivOpenHeart.setTag(R.drawable.open_heart);
            for (int i = 0; i < oppsLikes.size(); i++) {
                Log.i(TAG, "opportunity.getObjectId " + opportunity.getObjectId());
                Log.i(TAG, "oppsLikes.get(i).getObjectId() " + oppsLikes.get(i).getOpp().getObjectId());

                if (oppsLikes.get(i).getOpp().getObjectId().equals(opportunity.getObjectId())) {
                    ivOpenHeart.setImageResource(R.drawable.filled_heart);
                    ivOpenHeart.setTag(R.drawable.filled_heart);
                } else {
                    ivOpenHeart.setTag(R.drawable.open_heart);
                }
            }

        }

        //action after like heart button is clicked
        private void likes(Opportunity opportunity) {
            //checking tag to see if opportunity is not liked and then liking that opportunity
            if ((int) ivOpenHeart.getTag() == R.drawable.open_heart) {
                Likes likes = new Likes();
                likes.setUpdate(ParseUser.getCurrentUser(), opportunity);
                likes.saveInBackground(e -> {
                    if (e == null) {
                        //Save was done
                        oppsLikes.add(likes);
                        ivOpenHeart.setImageResource(R.drawable.filled_heart);
                        ivOpenHeart.setTag(R.drawable.filled_heart);
                        Log.i(TAG, "everything was successful- adding like");
                    } else {
                        //Something went wrong
                        Log.i(TAG, "something went wrong-adding like");

                    }
                });
            } else {
                //checking tag to see if opportunity is liked and then unliking that opportunity
                for (int i = 0; i < oppsLikes.size(); i++) {
                    if (oppsLikes.get(i).getOpp().getObjectId().equals(opportunity.getObjectId())) {

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Likes");

                        // Retrieve the object by id
                        int finalI = i;
                        String objectId = oppsLikes.get(i).getObjectId();
                        query.getInBackground(objectId, (object, e) -> {
                            if (e == null) {
                                //Object was fetched
                                //Deletes the fetched ParseObject from the database
                                object.deleteInBackground(e2 -> {
                                    if (e2 == null) {
                                        Log.i(TAG, "this is successful- removing like");
                                        oppsLikes.remove(finalI);
                                        ivOpenHeart.setImageResource(R.drawable.open_heart);
                                        ivOpenHeart.setTag(R.drawable.open_heart);

                                    } else {
                                        Log.i(TAG, "something went wrong with deleting- removing like");

                                    }
                                });
                            } else {
                                //Something went wrong
                                Log.i(TAG, "something went wrong with querying- removing like" + e);
                            }
                        });
                    }
                }
            }
        }
    }
}

