package com.example.findopp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findopp.fragments.HomeFragment;
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
    //public ImageView ivOpenHeart;
    //public ImageSwitcher ivOpenHeart;
    //private ImageView ivOpenHeart;
    private Context context;
    private List<Opportunity> opportunities;
    private ArrayList<Likes> likedOpps = new ArrayList<Likes>();
    private ArrayList<Likes> savedOpps = new ArrayList<Likes>();
    private ArrayList<Likes> oppsLikes = new ArrayList<Likes>();


    public static final String TAG = "Opp Adapter";
    String objectId;

    public OppAdapter(Context context, List<Opportunity> opportunities) {
        this.context = context;
        this.opportunities = opportunities;
        saveHeart();


    }

    public void saveHeart() {
        ParseQuery<Likes> query = ParseQuery.getQuery(Likes.class);

        //trying to query where the user equals the current user
        //query.include()
        ParseUser currentUser = ParseUser.getCurrentUser();
        //query.whereEqualTo("opportunity", opportunity);
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
//        private ArrayList<Likes> likedOpps = new ArrayList<Likes>();
//        private ArrayList<Likes> savedOpps = new ArrayList<Likes>();


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivOpenHeart = itemView.findViewById(R.id.ivOpenHeart);

//            int position = getBindingAdapterPosition();
//            // make sure the position is valid, i.e. actually exists in the view
//            if (position != RecyclerView.NO_POSITION) {
//                // get the movie at the position, this won't work if the class is static
//                Opportunity opportunity = opportunities.get(position);
//                saveHeart(opportunity);
//            }

            //titleAction(opportunity);


//            tvTitle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getBindingAdapterPosition();
//                    // make sure the position is valid, i.e. actually exists in the view
//                    if (position != RecyclerView.NO_POSITION) {
//                        // get the movie at the position, this won't work if the class is static
//                        Opportunity opportunity = opportunities.get(position);
//                        // create intent for the new activity
//                        Intent intent = new Intent(context, OppDetailsActivity.class);
//                        // serialize the movie using parceler, use its short name as a key
//                        intent.putExtra("opportunity", Parcels.wrap(opportunity));
//                        // show the activity
//                        context.startActivity(intent);
//                    }
//                }
//            });


        }


        public void bind(Opportunity opportunity) {
            tvTitle.setText(opportunity.getTitle());
            Intent intent = new Intent(context, HomeFragment.class);
            intent.putExtra("tvTitle", tvTitle.getText().toString());

            titleAction(opportunity);
            //saveHeart();

            ivOpenHeart.setTag(R.drawable.open_heart);
            for (int i = 0; i < oppsLikes.size(); i++) {
                Log.i(TAG, "opportunity.getObjectId " + opportunity.getObjectId());
                Log.i(TAG, "oppsLikes.get(i).getObjectId() " + oppsLikes.get(i).getOpp().getObjectId());

                if (oppsLikes.get(i).getOpp().getObjectId().equals(opportunity.getObjectId())) {
                    ivOpenHeart.setImageResource(R.drawable.filled_heart);
                    ivOpenHeart.setTag(R.drawable.filled_heart);
                } else{
                    ivOpenHeart.setTag(R.drawable.open_heart);
                }
            }

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

        private void likes(Opportunity opportunity){
            if((int) ivOpenHeart.getTag() == R.drawable.open_heart){
                Likes likes = new Likes();
                likes.setUpdate(ParseUser.getCurrentUser(), opportunity);
                likes.saveInBackground(e -> {
                    if (e==null){
                        //Save was done
                        oppsLikes.add(likes);
                        ivOpenHeart.setImageResource(R.drawable.filled_heart);
                        ivOpenHeart.setTag(R.drawable.filled_heart);
                        Log.i(TAG, "everything was successful- adding like");
                    }else{
                        //Something went wrong
                        Log.i(TAG, "something went wrong-adding like");

                    }
                });
            } else {
                for (int i = 0; i < oppsLikes.size(); i++) {
                    if (oppsLikes.get(i).getOpp().getObjectId().equals(opportunity.getObjectId())) {

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Likes");
                        //query.whereEqualTo("opportunity", opportunity);

                        // Retrieve the object by id
                        int finalI = i;
                        String objectId = oppsLikes.get(i).getObjectId();
                        Log.i(TAG, "objectId " + objectId.equals("l4bAkDRfv6"));
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
//            Likes likes = new Likes();
//            likes.setUpdate(ParseUser.getCurrentUser(), opportunity);
//            likes.saveInBackground(e -> {
//                if (e==null){
//                    //Save was done
//                    oppsLikes.add(likes);
//                    ivOpenHeart.setImageResource(R.drawable.filled_heart);
//                    Log.i(TAG, "everything was successful- adding like");
//                }else{
//                    //Something went wrong
//                    Log.i(TAG, "something went wrong-adding like");
//
//                }
//            });
        }













        //saves the likes after a user logs in and in between switching screens
//        public void saveHeart() {
//            ParseQuery<Likes> query = ParseQuery.getQuery(Likes.class);
//
//            //trying to query where the user equals the current user
//            //query.include()
//            ParseUser currentUser = ParseUser.getCurrentUser();
//            //query.whereEqualTo("opportunity", opportunity);
//            query.whereEqualTo("user", currentUser);
//
//            query.findInBackground(new FindCallback<Likes>() {
//                @Override
//                public void done(List<Likes> likes, ParseException e) {
//                    // check for errors
//                    if (e != null) {
//                        Log.e(TAG, "Issue with getting likes (saveHeart)", e);
//                        return;
//                    } else {
//                        Log.i(TAG, "size of likes (saveHeart)" + likes.size());
//                        savedOpps.addAll(likes);
//                        //for(Likes save: savedOpps) {
//                        if(!savedOpps.isEmpty()) {
//                            ivOpenHeart.setImageResource(R.drawable.filled_heart);
//                        }
//                        //notifyDataSetChanged();
//                    }
//                }
//            });


        //action after like heart button is clicked
//        public void likeHeart(Opportunity opportunity) {
//
//            ivOpenHeart.setOnClickListener((new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    queryLikes(opportunity);
//                }

        //sees if that user liked that opportunity
//            private void queryLikes(Opportunity opportunity) {
//                Log.i(TAG, "opportunity: " + opportunity);
//                ParseQuery<Likes> query = ParseQuery.getQuery(Likes.class);
//                //query.include(Likes.KEY_USER);
//                Likes currentLike = new Likes();
//                //trying to query where the user equals the current user and the current opportunity
//                ParseUser currentUser = ParseUser.getCurrentUser();
//                query.whereEqualTo("user", currentUser);
//                query.whereEqualTo("opportunity", opportunity);
//
//                query.findInBackground(new FindCallback<Likes>() {
//                    @Override
//                    public void done(List<Likes> likes, ParseException e) {
//                        // check for errors
//                        if (e != null) {
//                            Log.e(TAG, "Issue with getting likes", e);
//                            return;
//                        } else {
//                            Log.i(TAG, "likes:: " + likes);
//                            likedOpps.addAll(likes);
//                            //notifyDataSetChanged();
//
//                            Log.i(TAG, "size of likedArray " + likedOpps.size());
//                            //Log.i(TAG, "get(0): " + likedOpps.get(0));
//                            //if (!likedOpps.equals(opportunity.getObjectId())) {
//                            if (likedOpps.isEmpty()) {
//                                Log.i(TAG, "adding like" + likedOpps);
//                                ivOpenHeart.setImageResource(R.drawable.filled_heart);
//                                currentLike.put("opportunity", opportunity);
//                                currentLike.put("user", ParseUser.getCurrentUser());
//
//                                //Log.i(TAG, "GETIGN FROM GET: " + allLikes.get("user"));
//
//                                currentLike.saveInBackground();
//                                //} else if(likedOpps.equals(opportunity.getObjectId())) {
//                            }else if(!likedOpps.isEmpty()){
//                                queryRemoveLike(opportunity);
//                                Log.i(TAG, "removing like");
//
//                            }
//                        }
//
//                    }
//
//                });
//            }
//
//                //private method to remove like
//            private void queryRemoveLike(Opportunity opportunity) {
//                ParseQuery<Likes> query = ParseQuery.getQuery(Likes.class);
//                query.include(Likes.KEY_USER);
//                //trying to query where the user equals the current user and the current opportunity
//                ParseUser currentUser = ParseUser.getCurrentUser();
//                query.whereEqualTo("user", currentUser);
//                query.whereEqualTo("opportunity", opportunity);
//
//                query.findInBackground(new FindCallback<Likes>() {
//                    @Override
//                    public void done(List<Likes> objects, ParseException e) {
//                        try {
//                            for (ParseObject object : objects) {
//                                ivOpenHeart.setImageResource(R.drawable.open_heart);
//                                likedOpps.clear();
//                                object.delete();
//                                object.saveInBackground();
//                            }
//                        } catch (ParseException parseException) {
//                            parseException.printStackTrace();
//                        }
//                    }
//                });
//            }
//

    }
}
















//        public void likeHeart(Opportunity opportunity) {
//            ivOpenHeart.setOnClickListener((new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    objectId = opportunity.getName();
//                    //Likes likes = new Likes();
//
//
//                    //if user has no likes
////                    if(likedId == null) {
////                        likedId = new ArrayList<String>();
//
//                    //user adding a like
//                    //if the user has not liked that specific opportunity, then like it
//                    if (!likes.getUserLikes().equals(objectId)) {
//                        Log.i(TAG, "adding this user: " + objectId);
//                        ivOpenHeart.setImageResource(R.drawable.filled_heart);
//                        //likedId.add(objectId);
//
//                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
//                            @Override
//                            public void done(ParseException e) {
//                                if (e != null) {
//                                    // if no success
//                                    Log.e(TAG, "error saving " + e);
//                                    return;
//                                } else {
//                                    Log.i(TAG, "adding heart" );
//                                    likes.setUserLikes(currentUser);
//
//                                    //not sure if this works, does it know which opp to put in database?
//                                    likes.setOppLikes(opportunity);
//
//                                    //likes.put("user", currentUser);
//                                    //likes.put("opportunity", objectId);
//                                }
//                            }
//                        });
//
//                    //user removing a like
//                    } else if (likes.getUserLikes().equals(objectId)) {
//                        ivOpenHeart.setImageResource(R.drawable.open_heart);
//                        //likedId.remove(objectId);
//
//                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
//                            @Override
//                            public void done(ParseException e) {
//                                if (e != null) {
//                                    return;
//                                } else {
////                                    likes.remove("userName", currentUser);
////                                    likes.put("opportunity", objectId);
//                                    Log.i(TAG, " removing heart" );
//
//                                }
//
//                            }
//
//                        });
//
//                    }
//                }
//
//            }));
//        }



