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
import com.h6ah4i.android.materialshadowninepatch.MaterialShadowContainerView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.skyfishjy.library.RippleBackground;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

public class OppAdapter extends RecyclerView.Adapter<OppAdapter.ViewHolder> {
    private Context context;
    private List<Opportunity> opportunities;
    public ArrayList<Likes> oppsLikes;

    public static final String TAG = "Opp Adapter";

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
        public ImageView ivOpenHeart;
        final RippleBackground rippleBackground;
        MaterialShadowContainerView shadowView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivOpenHeart = itemView.findViewById(R.id.ivOpenHeart);
            rippleBackground = (RippleBackground) itemView.findViewById(R.id.content);
            //shadowView = (MaterialShadowContainerView) itemView.findViewById(R.id.shadow_item_container);

//            float density = itemView.getResources().getDisplayMetrics().density;
//            shadowView.setShadowTranslationZ(density * 2.0f); // 2.0 dp
//            shadowView.setShadowElevation(density * 4.0f); // 4.0 dp

            oppsLikes = new ArrayList<Likes>();
            itemViewTouch();

        }

        //gesture for double click to like and single click to see details activity
        private void itemViewTouch(){
            itemView.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                    //if the user double taps on itemView, it calls the liks method
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

                    //if the user single taps on the itemView, it calls the titleAction method
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
        }

        //action after title of the opportunity is clicked to see more details
        private void titleAction(Opportunity opportunity) {
            //rippleBackground.startRippleAnimation();
            Intent intent = new Intent(context, OppDetailsActivity.class);
            intent.putExtra("opportunity", Parcels.wrap(opportunity));
            context.startActivity(intent);
            //rippleBackground.stopRippleAnimation();
        }

        //this method parses through the Likes class in the database and displays the opportunities liked
        private void displayLikes(Opportunity opportunity) {
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
                        oppsLikes.addAll(likes);
                        ivOpenHeart.setTag(R.drawable.open_heart);

                        for (int i = 0; i < oppsLikes.size(); i++) {
                            if (oppsLikes.get(i).getOpp().getObjectId().equals(opportunity.getObjectId())) {
                                ivOpenHeart.setImageResource(R.drawable.filled_heart);
                                ivOpenHeart.setTag(R.drawable.filled_heart);
                            } else {
                                ivOpenHeart.setTag(R.drawable.open_heart);
                            }
                        }
                    }
                }
            });
        }

        //action after like heart button is clicked
        private void likes(Opportunity opportunity) {
            //checking tag to see if opportunity is not liked and then liking that opportunity
            if ((int) ivOpenHeart.getTag() == R.drawable.open_heart) {
                addLike(opportunity);
            } else {
                removeLike(opportunity);
            }
        }

        //adds likes to database and fills ivOpenHeart
        private void addLike(Opportunity opportunity){
            Likes likes = new Likes();
            likes.setUpdate(ParseUser.getCurrentUser(), opportunity);
            likes.saveInBackground(e -> {
                if (e == null) {
                    //Save was done
                    oppsLikes.add(likes);
                    //rippleBackground.startRippleAnimation();
                    ivOpenHeart.setImageResource(R.drawable.filled_heart);
                    ivOpenHeart.setTag(R.drawable.filled_heart);
                    //rippleBackground.stopRippleAnimation();
                    Log.i(TAG, "everything was successful- adding like");
                } else {
                    //Something went wrong
                    Log.i(TAG, "something went wrong-adding like");

                }
            });
        }

        //removes likes from database and makes ivOpenHeart open again
        private void removeLike(Opportunity opportunity){
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
                                    Log.i(TAG, "OP this is successful- removing like");
                                    oppsLikes.remove(finalI);
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



