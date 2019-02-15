package io.dume.dume.teacher.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import carbon.widget.RelativeLayout;
import io.dume.dume.R;
import io.dume.dume.model.DumeModel;
import io.dume.dume.student.common.ReviewAdapter;
import io.dume.dume.student.common.ReviewHighlightData;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.searchResult.SearchResultActivity;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.model.KeyMap;
import io.dume.dume.teacher.model.LocalDb;
import io.dume.dume.teacher.pojo.Skill;
import io.dume.dume.teacher.skill.SkillActivity;
import io.dume.dume.util.VisibleToggleClickListener;

import static io.dume.dume.util.DumeUtils.getLast;

public class SkillAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static int FRAGMENT = 1;
    public static int ACTIVITY = 2;
    private int itemWidth;
    private ArrayList<String> endOfNest = null;
    private int layoutSize;
    private ArrayList<Skill> skillList;
    private View inflate;
    private Context context;
    private String TAG = "SkillAdapter";
    private HashMap<String, Integer> iconList;
    private LocalDb localDb;
    private float mDensity;
    private ReviewAdapter reviewRecyAda;
    private ReviewHighlightData lastReviewData;
    private BottomSheetDialog mBackBSD;
    private View backsheetRootView;
    private TextView backMainText;
    private TextView backSubText;
    private Button backYesBtn;
    private Button backNoBtn;
    private String[] splitMainSsss;
    private Integer likes = 0;
    private Integer dislikes= 0;


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

    public SkillAdapter(Context context, int layoutSize, int itemWidth, ArrayList<Skill> skillList) {
        this.layoutSize = layoutSize;
        this.skillList = skillList;
        this.itemWidth = itemWidth;
        this.context = context;
        mDensity = context.getResources().getDisplayMetrics().density;
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
        //init the back dialog
        mBackBSD = new BottomSheetDialog(context);
        backsheetRootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_bottom_sheet_dialogue_cancel, null);
        mBackBSD.setContentView(backsheetRootView);
        backMainText = mBackBSD.findViewById(R.id.main_text);
        backSubText = mBackBSD.findViewById(R.id.sub_text);
        backYesBtn = mBackBSD.findViewById(R.id.cancel_yes_btn);
        backNoBtn = mBackBSD.findViewById(R.id.cancel_no_btn);
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
            myViewHolder.switchCompat.setChecked(skill.isStatus());
            int salary = (int) skill.getSalary();
            String format1 = NumberFormat.getCurrencyInstance(Locale.US).format(salary);

            detailList.add(new KeyMap("Salary", format1.substring(1, format1.length() - 3) + " BDT"));
            detailList.add(new KeyMap("Skill Visibility", skill.isStatus() ? "Public" : "Private (Inactive)"));
            detailList.add(new KeyMap("Rating", ((int) skill.getRating()) + "/ 옷" + skill.getTotalRating()));
            detailList.add(new KeyMap("Package Name", skill.getPackage_name()));
            for (int j = 0; j < skill.getQuery_list().size() - 2; j++) {
                detailList.add(new KeyMap(skill.getQuery_list_name().get(j), skill.getQuery_list().get(j)));
            }
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
                                    if (backMainText != null && backSubText != null && backYesBtn != null && backNoBtn != null) {
                                        backMainText.setText("Remove skill ?");
                                        final String thisSkillTitile = myViewHolder.skillTitleTV.getText().toString();
                                        backSubText.setText(String.format("Confirming will remove your %s skill...", thisSkillTitile));
                                        backYesBtn.setText("Yes, Remove");
                                        backNoBtn.setText("No");

                                        backYesBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                mBackBSD.dismiss();
                                                new DumeModel(context).deleteSkill(skill.getId(), new TeacherContract.Model.Listener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void list) {
                                                        Toast.makeText(context, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onError(String msg) {
                                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });

                                        backNoBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                mBackBSD.dismiss();
                                            }
                                        });
                                    }
                                    mBackBSD.show();
                                    break;
                                case R.id.action_edit:
                                    Toast.makeText(context, "Feature is Coming Soon...", Toast.LENGTH_SHORT).show();
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
            int salary1 = (int) skillList.get(i).getSalary();
            String format2 = NumberFormat.getCurrencyInstance(Locale.US).format(salary1);
            myViewHolder.salaryTV.setText(format2.substring(1, format2.length() - 3) + " BDT");
            myViewHolder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    new DumeModel(context).swithSkillStatus(skill.getId(), b, new TeacherContract.Model.Listener<Void>() {
                        @Override
                        public void onSuccess(Void list) {
                            String foo = b ? "Active" : "Inactive";
                            Toast.makeText(context, "Skill Status Changed To " + foo, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String msg) {
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
            myViewHolder.publishDate.setText(dateString);
            myViewHolder.enrolledTV.setText("Enrolled Students : " + skillList.get(i).getEnrolled());

            //init review recycler
            new DumeModel(context).loadReview(skill.getId(), null, new TeacherContract.Model.Listener<List<ReviewHighlightData>>() {
                @Override
                public void onSuccess(List<ReviewHighlightData> list) {

                    lastReviewData = list.get(list.size() - 1);
                    reviewRecyAda = new ReviewAdapter(context, list, true);
                    myViewHolder.reviewRecycler.setAdapter(reviewRecyAda);
                    myViewHolder.reviewRecycler.setLayoutManager(new LinearLayoutManager(context));
                    if (list.size() >= 10) {
                        myViewHolder.loadMoreBTN.setEnabled(true);
                    } else {
                        myViewHolder.loadMoreBTN.setEnabled(false);
                        myViewHolder.loadMoreBTN.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(String msg) {

                    myViewHolder.reviewHostLayout.setVisibility(View.GONE);
                    if (msg.equals("No review")) {
                        return;
                    }
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });

            myViewHolder.loadMoreBTN.setOnClickListener(view -> {
                view.setEnabled(false);
                new DumeModel(context).loadReview(skill.getId(), lastReviewData.getDoc_id(), new TeacherContract.Model.Listener<List<ReviewHighlightData>>() {
                    @Override
                    public void onSuccess(List<ReviewHighlightData> list) {
                        view.setEnabled(true);
                        lastReviewData = list.get(list.size() - 1);
                        reviewRecyAda.addMore(list);
                    }

                    @Override
                    public void onError(String msg) {
                        view.setEnabled(true);
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            });


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
                    myViewHolder.reviewShowBtn.setEnabled(false);
                    if (visible) {
                        myViewHolder.reviewLayoutVertical.setVisibility(View.VISIBLE);
                    } else {
                        myViewHolder.reviewLayoutVertical.setVisibility(View.INVISIBLE);
                    }
                    Drawable[] compoundDrawables = myViewHolder.reviewShowBtn.getCompoundDrawables();
                    Drawable d = compoundDrawables[3];
                    if (d instanceof Animatable) {
                        ((Animatable) d).start();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ((Animatable2) d).registerAnimationCallback (new Animatable2.AnimationCallback(){
                            public void onAnimationEnd(Drawable drawable){
                                //Do something
                                if(visible){
                                    myViewHolder.reviewShowBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, context.getResources().getDrawable(R.drawable.ic_up_arrow_small));
                                } else {
                                    myViewHolder.reviewShowBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, context.getResources().getDrawable(R.drawable.ic_down_arrow_small));
                                }
                                myViewHolder.reviewShowBtn.setEnabled(true);
                            }
                        });
                    }
                }
            });

            HashMap<String, Object> jizz = skillList.get(i).getJizz();
            if (getLast(i) != null) {
                final Object o = jizz.get(getLast(i));
                myViewHolder.skillTitleTV.setText(o.toString() + " / " + jizz.get("Category"));
                myViewHolder.categoryAvatar.setImageResource(iconList.get(jizz.get("Category")));
                String mainSsss = o.toString();
                splitMainSsss = mainSsss.split("\\s*(=>|,|\\s)\\s*");
            }
            for (String splited : splitMainSsss) {
                likes = likes + Integer.parseInt(skillList.get(i).getLikes().get(splited).toString());
                dislikes = dislikes +  Integer.parseInt(skillList.get(i).getDislikes().get(splited).toString());
            }
            myViewHolder.likeTV.setText((likes-3) + " likes");

        } else {//fragment start here
            SkillFVH myFragmentHolder = (SkillFVH) holder;
            myFragmentHolder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    new DumeModel(context).swithSkillStatus(skillList.get(i).getId(), b, new TeacherContract.Model.Listener<Void>() {
                        @Override
                        public void onSuccess(Void list) {
                            String foo = b ? "Active" : "Inactive";
                            Toast.makeText(context, "Skill Status Changed To " + foo, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String msg) {
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            });
            myFragmentHolder.itemView.setOnClickListener(view -> {
                context.startActivity(new Intent(context, SkillActivity.class));
            });


            //fixing width
            ViewGroup.LayoutParams layoutParams = myFragmentHolder.hostingRelative.getLayoutParams();
            layoutParams.width = (itemWidth);
            myFragmentHolder.hostingRelative.setLayoutParams(layoutParams);
            //common code here
            HashMap<String, Object> jizz = skillList.get(i).getJizz();
            if (getLast(i) != null) {
                final Object o = jizz.get(getLast(i));
                myFragmentHolder.skillTitleTV.setText(o.toString() + " / " + jizz.get("Category"));
                String mainSsss = o.toString();
                splitMainSsss = mainSsss.split("\\s*(=>|,|\\s)\\s*");
            }
            myFragmentHolder.switchCompat.setChecked(skillList.get(i).isStatus());

            for (String splited : splitMainSsss) {
                likes = likes + Integer.parseInt(skillList.get(i).getLikes().get(splited).toString());
                dislikes = dislikes +  Integer.parseInt(skillList.get(i).getDislikes().get(splited).toString());
            }


            int totalCount =likes + dislikes;
            if (totalCount == 0) {
                myFragmentHolder.likeTV.setText("n/a");
            } else {
                int likeP = (int) (likes/ totalCount) * 100;
                myFragmentHolder.likeTV.setText(likeP + "% liked");
            }
            int salary = (int) (skillList.get(i).getSalary()) / 1000;


            myFragmentHolder.packageName.setText(skillList.get(i).getPackage_name() + " / " + salary + " k");


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
        @BindView(R.id.load_more_review_btn)
        Button loadMoreBTN;


        public SkillAVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class SkillFVH extends RecyclerView.ViewHolder {

        @BindView(R.id.skillTitleTV)
        TextView skillTitleTV;
        @BindView(R.id.skillStatus)
        SwitchCompat switchCompat;
        @BindView(R.id.likeTV)
        TextView likeTV;
        @BindView(R.id.package_name)
        TextView packageName;
        @BindView(R.id.hosting_relative_layout)
        RelativeLayout hostingRelative;

        public SkillFVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
