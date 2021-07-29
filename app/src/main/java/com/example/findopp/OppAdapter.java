package com.example.findopp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    private List<Likes> likesList;
    private Context context;
    private List<Opportunity> opportunities;
    public ArrayList<Likes> oppsLikes;

    //reference to main activity
    private MainActivity mainActivity;

    public static final String TAG = "Opp Adapter";

    public OppAdapter(Context context, List<Opportunity> opportunities, MainActivity mainActivity) {
        this.context = context;
        this.opportunities = opportunities;
        this.mainActivity = mainActivity;
    }

//    public OppAdapter(Context context, List<Opportunity> opportunities, List<Likes> likes) {
//        this.context = context;
//        this.opportunities = opportunities;
//        this.likesList = likes;
//    }


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
            oppsLikes = new ArrayList<Likes>();
            int position = getBindingAdapterPosition();

            //gesture for double click to like and single click to see details activity
            itemView.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        Log.i(TAG, "onDoubleTap");
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Opportunity opportunity = opportunities.get(position);
                            likes(opportunity);
                        }
                        return super.onDoubleTap(e);
                    }

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        Log.i(TAG, "onSingleTapConfirmed");
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Opportunity opportunity = opportunities.get(position);
                            titleAction(opportunity);
                        }
                        return super.onSingleTapConfirmed(e);
                    }
                });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });
        }

        public void bind(Opportunity opportunity) {
            tvTitle.setText(opportunity.getTitle());
            displayLikes(opportunity);
            //displayLikes(opportunity);
        }

        //action after title of the opportunity is clicked to see more details
        private void titleAction(Opportunity opportunity) {
            Intent intent = new Intent(context, OppDetailsActivity.class);
            intent.putExtra("opportunity", Parcels.wrap(opportunity));
            context.startActivity(intent);
        }

//        private void displayLikes(Opportunity opportunity) {
//            ivOpenHeart.setTag(R.drawable.open_heart);
//            Log.i(TAG, "oppLikes OPP ADAPTER: " + mainActivity.oppsLikes.size());
//
//            for (int i = 0; i < mainActivity.oppsLikes.size(); i++) {
//                //for (int i = 0; i < getItemCount(); i++) {
//                Log.i(TAG, "opportunity.getObjectId " + opportunity.getObjectId() + "i: " + i);
//                Log.i(TAG, "mainactivity.oppsLikes.get(i).getObjectId() " + mainActivity.oppsLikes.get(i).getOpp().getObjectId());
//
//
//                if (mainActivity.oppsLikes.get(i).getOpp().getObjectId().equals(opportunity.getObjectId())) {
//                    Log.i(TAG, "fill heart");
//                    ivOpenHeart.setImageResource(R.drawable.filled_heart);
//                    ivOpenHeart.setTag(R.drawable.filled_heart);
//                } else {
//                    Log.i(TAG, "open heart");
//                    ivOpenHeart.setTag(R.drawable.open_heart);
//                }
//            }
//        }

        //this version is doing mainActivity.saveHeart()
        private void displayLikes(Opportunity opportunity) {
            //mainActivity.saveHeart();
            ivOpenHeart.setTag(R.drawable.open_heart);
            Log.i(TAG, "oppLikes OPP ADAPTER: " + mainActivity.oppsLikes.size());

            for (int i = 0; i < mainActivity.oppsLikes.size(); i++) {
                Log.i(TAG, "opportunity.getObjectId " + opportunity.getObjectId() + "i: " + i);
                Log.i(TAG, "mainactivity.oppsLikes.get(i).getObjectId() " + mainActivity.oppsLikes.get(i).getOpp().getObjectId());


                if (mainActivity.oppsLikes.get(i).getOpp().getObjectId().equals(opportunity.getObjectId())) {
                    Log.i(TAG, "fill heart");
                    ivOpenHeart.setImageResource(R.drawable.filled_heart);
                    ivOpenHeart.setTag(R.drawable.filled_heart);
                } else {
                    Log.i(TAG, "open heart");
                    ivOpenHeart.setTag(R.drawable.open_heart);
                }
            }
        }

//            private void displayLikes() {
//                ParseQuery<Opportunity> query = ParseQuery.getQuery(Opportunity.class);
//                query.include("objectId");
//
//                //trying to query where the user equals the current user
//                //                    ParseUser currentUser = ParseUser.getCurrentUser();
//                //                    query.whereEqualTo("user", currentUser);
//
//                query.findInBackground(new FindCallback<Opportunity>() {
//                    @Override
//                    public void done(List<Opportunity> opportunities, ParseException e) {
//                        // check for errors
//                        if (e != null) {
//                            Log.e(TAG, "Issue with getting likes (saveHeart)", e);
//                            return;
//                        } else {
//                            //Log.i(TAG, "size of likes (saveHeart MAIN)" + likes.size());
//                            ivOpenHeart.setTag(R.drawable.open_heart);
//                            Log.i(TAG, "oppLikes OPP ADAPTER: " + mainActivity.oppsLikes.size());
//
//                            for (int i = 0; i < mainActivity.oppsLikes.size(); i++) {
//                                //for (int i = 0; i < getItemCount(); i++) {
//                                Log.i(TAG, "opportunity.getObjectId " + opportunities.get(i).getObjectId() +"i: " + i);
//                                Log.i(TAG, "oppsLikes.get(i).getObjectId() " + mainActivity.oppsLikes.get(i).getOpp().getObjectId());
//
//                                if (mainActivity.oppsLikes.get(i).getOpp().getObjectId().equals(opportunities.get(i).getObjectId())) {
//                                    Log.i(TAG, "fill heart");
//                                    ivOpenHeart.setImageResource(R.drawable.filled_heart);
//                                    ivOpenHeart.setTag(R.drawable.filled_heart);
//                                } else {
//                                    Log.i(TAG, "open heart");
//                                    ivOpenHeart.setTag(R.drawable.open_heart);
//                                }
//                            }
//                        }
//                    }
//                });
//            }

    //


                //action after like heart button is clicked
                private void likes(Opportunity opportunity) {
                    //checking tag to see if opportunity is not liked and then liking that opportunity
                    if ((int) ivOpenHeart.getTag() == R.drawable.open_heart) {
                        Likes likes = new Likes();
                        likes.setUpdate(ParseUser.getCurrentUser(), opportunity);
                        likes.saveInBackground(e -> {
                            if (e == null) {
                                //Save was done
                                //mainActivity.oppsLikes.add(likes);
                                mainActivity.oppsLikes.add(likes);
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
                        for (int i = 0; i < mainActivity.oppsLikes.size(); i++) {
//                            for (int i = 0; i < mainActivity.oppsLikes.size(); i++) {
                            //Log.i(TAG, "mainActivity.oppsLikes.size() " + mainActivity.oppsLikes.size());
                            if (mainActivity.oppsLikes.get(i).getOpp().getObjectId().equals(opportunity.getObjectId())) {
//                                if (mainActivity.oppsLikes.get(i).getOpp().getObjectId().equals(opportunity.getObjectId())) {

                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Likes");

                                // Retrieve the object by id
                                int finalI = i;
                                //String objectId = mainActivity.oppsLikes.get(i).getObjectId();
                                String objectId = mainActivity.oppsLikes.get(i).getObjectId();
                                query.getInBackground(objectId, (object, e) -> {
                                    if (e == null) {
                                        //Object was fetched
                                        //Deletes the fetched ParseObject from the database
                                        object.deleteInBackground(e2 -> {
                                            if (e2 == null) {
                                                Log.i(TAG, "OP this is successful- removing like");
                                                mainActivity.oppsLikes.remove(finalI);
//                                                mainActivity.oppsLikes.remove(finalI);
                                                ivOpenHeart.setImageResource(R.drawable.open_heart);
                                                ivOpenHeart.setTag(R.drawable.open_heart);

                                            } else {
                                                Log.i(TAG, "OP something went wrong with deleting- removing like");

                                            }
                                        });
                                    } else {
                                        //Something went wrong
                                        Log.i(TAG, "OP something went wrong with querying- removing like" + e);
                                    }
                                });
                            }
                        }
                    }
                }
        }
    }




