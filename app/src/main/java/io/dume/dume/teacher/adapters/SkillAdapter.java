package io.dume.dume.teacher.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import carbon.widget.RelativeLayout;
import io.dume.dume.R;
import io.dume.dume.student.common.ReviewAdapter;
import io.dume.dume.student.common.ReviewHighlightData;
import io.dume.dume.teacher.model.KeyMap;
import io.dume.dume.teacher.model.LocalDb;
import io.dume.dume.teacher.pojo.Skill;
import io.dume.dume.teacher.skill.SkillActivity;
import io.dume.dume.util.VisibleToggleClickListener;

public class SkillAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static int FRAGMENT = 1;
    public static int ACTIVITY = 2;
    private ArrayList<String> endOfNest = null;
    private int layoutSize;
    private ArrayList<Skill> skillList;
    private View inflate;
    private Context context;
    private String TAG = "SkillAdapter";
    private HashMap<String, Integer> iconList;
    private LocalDb localDb;


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public SkillAdapter(int layoutSize) {
        this.skillList = new ArrayList<>();
        this.layoutSize = layoutSize;
        localDb = new LocalDb();
        endOfNest = new ArrayList<>(Arrays.asList("Subject", "Field", "Software", "Language", "Flavour", "Type", "Course", " Language "));
        iconList = new HashMap<>();
        iconList.put(localDb.getCategories().get(0), R.drawable.education);
        iconList.put(localDb.getCategories().get(0 + 1), R.drawable.software);
        iconList.put(localDb.getCategories().get(1 + 1), R.drawable.programming);
        iconList.put(localDb.getCategories().get(2 + 1), R.drawable.language);
        iconList.put(localDb.getCategories().get(3 + 1), R.drawable.dance);
        iconList.put(localDb.getCategories().get(4 + 1), R.drawable.art);
        iconList.put(localDb.getCategories().get(5 + 1), R.drawable.cooking);
        iconList.put(localDb.getCategories().get(6 + 1), R.drawable.music);
        iconList.put(localDb.getCategories().get(7 + 1), R.drawable.others);
    }

    public SkillAdapter(int layoutSize, ArrayList<Skill> skillList) {
        this.layoutSize = layoutSize;
        this.skillList = skillList;
        localDb = new LocalDb();
        endOfNest = new ArrayList<>(Arrays.asList("Subject", "Field", "Software", "Language", "Flavour", "Type", "Course", " Language "));
        iconList = new HashMap<>();
        iconList.put(localDb.getCategories().get(0), R.drawable.education);
        iconList.put(localDb.getCategories().get(0 + 1), R.drawable.software);
        iconList.put(localDb.getCategories().get(1 + 1), R.drawable.programming);
        iconList.put(localDb.getCategories().get(2 + 1), R.drawable.language);
        iconList.put(localDb.getCategories().get(3 + 1), R.drawable.dance);
        iconList.put(localDb.getCategories().get(4 + 1), R.drawable.art);
        iconList.put(localDb.getCategories().get(5 + 1), R.drawable.cooking);
        iconList.put(localDb.getCategories().get(6 + 1), R.drawable.music);
        iconList.put(localDb.getCategories().get(7 + 1), R.drawable.others);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        if (layoutSize == FRAGMENT) {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.skill_item_small, viewGroup, false);
            return new SkillFVH(inflate);
        } else if (layoutSize == ACTIVITY) {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.skill_item_large, viewGroup, false);
            return new SkillAVH(inflate);
        }
        inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.skill_item_large, viewGroup, false);
        return new SkillAVH(inflate);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (layoutSize == ACTIVITY) {
            SkillAVH myViewHolder = (SkillAVH) holder;
            ArrayList<KeyMap> detailList = new ArrayList<>();
            Skill skill = skillList.get(i);

            detailList.add(new KeyMap("Enrolled Students", skill.getEnrolled()));
            detailList.add(new KeyMap("Package Name", "Regular Dume"));
            detailList.add(new KeyMap("Salary", skill.getSalary() + "k BDT"));
            detailList.add(new KeyMap("Skill Visibility", skill.isStatus() ? "Public" : "Private (Inactive)"));
            detailList.add(new KeyMap("Rating", ((int) skill.getRating()) + "/ 옷" + skill.getTotalRating()));
            detailList.add(new KeyMap("Skill Type", skill.getJizz().get("Category")));
            detailList.add(new KeyMap("Gender Filter", skill.getJizz().get("Gender").equals("Any") ? "None" : skill.getJizz().get("Gender")));

            SkillDetailsAdapter skillDetailsAdapter = new SkillDetailsAdapter(detailList);
            myViewHolder.detailsRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            myViewHolder.detailsRV.setAdapter(skillDetailsAdapter);
            myViewHolder.itemView.setOnClickListener(new VisibleToggleClickListener() {

                @SuppressLint("CheckResult")
                @Override
                protected void changeVisibility(boolean visible) {
                    TransitionSet set = new TransitionSet()
                            .addTransition(new Fade())
                            .addTransition(new Slide(Gravity.TOP))
                            .setInterpolator(visible ? new LinearOutSlowInInterpolator() : new FastOutLinearInInterpolator())
                            .addListener(new Transition.TransitionListener() {
                                @Override
                                public void onTransitionStart(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionEnd(@NonNull Transition transition) {
                                    if (!visible) {
                                        myViewHolder.recycleHostingLinear.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onTransitionCancel(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionPause(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionResume(@NonNull Transition transition) {

                                }
                            });
                    TransitionManager.beginDelayedTransition(myViewHolder.recycleHostingLinear, set);
                    if (visible) {
                        myViewHolder.recycleHostingLinear.setVisibility(View.VISIBLE);
                    } else {
                        myViewHolder.recycleHostingLinear.setVisibility(View.INVISIBLE);
                    }
                }
            });

            myViewHolder.moreVertSkill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(context, view);
                    popup.getMenuInflater().inflate(R.menu.menu_edit_remove, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            switch (id) {
                                case R.id.action_remove:
                                    Toast.makeText(context, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.action_edit:
                                    Toast.makeText(context, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = format.format(skillList.get(i).getCreation());
            myViewHolder.salaryTV.setText((int) skillList.get(i).getSalary() + " tk");
            myViewHolder.switchCompat.setChecked(skillList.get(i).isStatus());
            myViewHolder.publishDate.setText(dateString);
            myViewHolder.enrolledTV.setText("Enrolled Students : " + skillList.get(i).getEnrolled());
            myViewHolder.likeTV.setText((int) (skillList.get(i).getRating() * 100) / 5 + " likes");

            //init review recycler
            List<ReviewHighlightData> reviewData = new ArrayList<>();
            ReviewAdapter reviewRecyAda = new ReviewAdapter(context, reviewData, true);
            myViewHolder.reviewRecycler.setAdapter(reviewRecyAda);
            myViewHolder.reviewRecycler.setLayoutManager(new LinearLayoutManager(context));
            myViewHolder.reviewShowBtn.setOnClickListener(new VisibleToggleClickListener() {

                @SuppressLint("CheckResult")
                @Override
                protected void changeVisibility(boolean visible) {
                    TransitionSet set1 = new TransitionSet()
                            .addTransition(new Fade())
                            .addTransition(new Slide(Gravity.TOP))
                            .setInterpolator(visible ? new LinearOutSlowInInterpolator() : new FastOutLinearInInterpolator())
                            .addListener(new Transition.TransitionListener() {
                                @Override
                                public void onTransitionStart(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionEnd(@NonNull Transition transition) {
                                    if (!visible) {
                                        myViewHolder.reviewLayoutVertical.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onTransitionCancel(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionPause(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionResume(@NonNull Transition transition) {

                                }
                            });
                    TransitionManager.beginDelayedTransition(myViewHolder.reviewHostLayout, set1);
                    if (visible) {
                        myViewHolder.reviewLayoutVertical.setVisibility(View.VISIBLE);
                    } else {
                        myViewHolder.reviewLayoutVertical.setVisibility(View.INVISIBLE);
                    }
                }
            });

        } else {//fragment start here
            SkillFVH myFragmentHolder = (SkillFVH) holder;
            myFragmentHolder.itemView.setOnClickListener(view -> {
                context.startActivity(new Intent(context, SkillActivity.class));
            });

            //common code here
            /*HashMap<String, Object> jizz = skillList.get(i).getJizz();
            if (getLast(i) != null) {
                final Object o = jizz.get(getLast(i));
                myFragmentHolder.skillTitleTV.setText(o.toString() + " | " + jizz.get("Category"));
                myFragmentHolder.categoryAvatar.setImageResource(iconList.get(jizz.get("Category")));
            }*/

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = format.format(skillList.get(i).getCreation());
            myFragmentHolder.salaryTV.setText((int) skillList.get(i).getSalary() + " tk");
            myFragmentHolder.switchCompat.setChecked(skillList.get(i).isStatus());
            myFragmentHolder.publishDate.setText(dateString);
            myFragmentHolder.likeTV.setText((int) (skillList.get(i).getRating() * 100) / 5 + " likes");
        }
    }

    public String getLast(int i) {
        HashMap<String, Object> jizz = skillList.get(i).getJizz();
        for (int j = 0; j < endOfNest.size(); j++) {
            if (jizz.containsKey(endOfNest.get(j))) {
                return endOfNest.get(j);
            }
        }
        return null;
    }


    public void update(ArrayList<Skill> skillList) {
        this.skillList.clear();
        this.skillList.addAll(skillList);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (skillList != null) {
            if (layoutSize == FRAGMENT) {
                if (skillList.size() > 4) {
                    return 4;
                } else return skillList.size();
            } else {
                return skillList.size();
            }
        }
        return 0;
    }

    class SkillAVH extends RecyclerView.ViewHolder {

        @BindView(R.id.skillDetailsRV)
        RecyclerView detailsRV;
        @BindView(R.id.skillSalaryTV)
        TextView salaryTV;
        @BindView(R.id.skillAddDateTV)
        TextView publishDate;
        @BindView(R.id.skillTitleTV)
        TextView skillTitleTV;
        @BindView(R.id.skillStatus)
        SwitchCompat switchCompat;
        @BindView(R.id.enrolledTV)
        TextView enrolledTV;
        @BindView(R.id.likeTV)
        TextView likeTV;
        @BindView(R.id.skillDots)
        carbon.widget.ImageView moreVertSkill;
        @BindView(R.id.hosting_relative_layout)
        RelativeLayout hostingRelativeLayout;
        @BindView(R.id.recycle_hosting_linear)
        LinearLayout recycleHostingLinear;
        @BindView(R.id.categoryAvaterIV)
        ImageView categoryAvatar;
        @BindView(R.id.review_layout_vertical)
        LinearLayout reviewLayoutVertical;
        @BindView(R.id.recycler_view_reviews)
        RecyclerView reviewRecycler;
        @BindView(R.id.review_info_btn)
        Button reviewShowBtn;
        @BindView(R.id.review_host_linearlayout)
        LinearLayout reviewHostLayout;


        public SkillAVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class SkillFVH extends RecyclerView.ViewHolder {

        @BindView(R.id.skillSalaryTV)
        TextView salaryTV;
        @BindView(R.id.skillAddDateTV)
        TextView publishDate;
        @BindView(R.id.skillTitleTV)
        TextView skillTitleTV;
        @BindView(R.id.skillStatus)
        SwitchCompat switchCompat;
        @BindView(R.id.likeTV)
        TextView likeTV;

        public SkillFVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
