package io.dume.dume.teacher.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.materialrangebar.RangeBar;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import carbon.widget.RelativeLayout;
import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.model.DumeModel;
import io.dume.dume.student.common.ReviewAdapter;
import io.dume.dume.student.common.ReviewHighlightData;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.teacher.model.KeyMap;
import io.dume.dume.teacher.model.LocalDb;
import io.dume.dume.teacher.pojo.Skill;
import io.dume.dume.teacher.skill.SkillActivity;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.VisibleToggleClickListener;

import static io.dume.dume.util.DumeUtils.getLast;
import static io.dume.dume.util.DumeUtils.giveIconOnCategoryName;

public class SkillAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static int FRAGMENT = 1;
    public static int ACTIVITY = 2;
    private int itemWidth;
    private List<String> endOfNest = null;
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
    private BottomSheetDialog mUpdateSkill;
    private View updateSheetRootView;
    private TextView updateMainText;
    private TextView updateSubText;
    private Button updateYesBtn;
    private Button updateNoBtn;
    private EditText chosenSubjects;
    private TextView salaryTV;
    private RangeBar salaryBar;
    private String[] subjectList;
    private boolean[] checkedItems;
    private List<String> wantedList;
    private List<String> selectedSubjects;


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public SkillAdapter(int layoutSize) {
        this.skillList = new ArrayList<>();
        this.layoutSize = layoutSize;
        localDb = new LocalDb();
        endOfNest = DumeUtils.getEndOFNest();
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
        endOfNest = DumeUtils.getEndOFNest();
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
        endOfNest = DumeUtils.getEndOFNest();
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

        //testing skill updater here
        mUpdateSkill = new BottomSheetDialog(context);
        updateSheetRootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.skill_updater_dialog, null);
        mUpdateSkill.setContentView(updateSheetRootView);
        updateMainText = mUpdateSkill.findViewById(R.id.main_text);
        updateSubText = mUpdateSkill.findViewById(R.id.sub_text);
        updateYesBtn = mUpdateSkill.findViewById(R.id.cancel_yes_btn);
        updateNoBtn = mUpdateSkill.findViewById(R.id.cancel_no_btn);
        chosenSubjects = mUpdateSkill.findViewById(R.id.input_subjects);
        salaryTV = mUpdateSkill.findViewById(R.id.minSal);
        salaryBar = mUpdateSkill.findViewById(R.id.rangeSlider);

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
        Integer likes = 0;
        Integer dislikes = 0;
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
            String package_name = skill.getPackage_name();
            if (package_name != null) {
                if(package_name.equals(SearchDataStore.REGULAR_DUME)){
                    package_name = "Monthly Tutor";
                } else if (package_name.equals(SearchDataStore.INSTANT_DUME)) {
                    package_name = "Weekly Tutor";
                }else {
                    package_name = "Couching Service";
                }
            }
            detailList.add(new KeyMap("Package Name", package_name));
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
                                        String thisSkillTitile = myViewHolder.skillTitleTV.getText().toString();
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
                                                        Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
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
                                    if (updateMainText != null && updateSubText != null && updateYesBtn != null && updateNoBtn != null) {
                                        String thisSkillTitile = myViewHolder.skillTitleTV.getText().toString();
                                        updateSubText.setText(String.format("Please update your %s skill salary & subject. Note salary is modified as,%nSalary = 0.25 * Mentor_Asked_Salary+((0.75 * Mentor_Asked_Salary) / Mentor_Selected_Subject) * Student_Searched_subject.", thisSkillTitile));
                                        Skill hereSkill = skillList.get(i);
                                        int salary1 = (int) hereSkill.getSalary();
                                        String format2 = NumberFormat.getCurrencyInstance(Locale.US).format(salary1);
                                        salaryTV.setText(format2.substring(1, format2.length() - 3) + " ৳");
                                        int barIndex = (salary1 / 1000) - 1;
                                        salaryBar.setSeekPinByIndex(barIndex);
                                        //testing here
                                        wantedList = new ArrayList<>();
                                        int threshold = hereSkill.getPackage_name().equals(SearchDataStore.DUME_GANG) ? 4 : 3;
                                        int level = hereSkill.getQuery_list().size() - threshold;
                                        LocalDb localDb = new LocalDb();
                                        switch (level) {
                                            case 1:
                                                wantedList = localDb.getLevelOne(giveIconOnCategoryName(hereSkill.getQuery_list().get(0)));
                                                break;
                                            case 2:
                                                wantedList = localDb.getLevelTwo(hereSkill.getQuery_list().get(1), hereSkill.getQuery_list().get(0));
                                                break;
                                            case 3:
                                                wantedList = localDb.getLevelThree(hereSkill.getQuery_list().get(2), hereSkill.getQuery_list().get(1));
                                                break;
                                            case 4:
                                                wantedList = localDb.getLevelFour(hereSkill.getQuery_list().get(3), hereSkill.getQuery_list().get(2), hereSkill.getQuery_list().get(1));
                                                break;
                                        }
                                        //get the two array
                                        subjectList = new String[wantedList.size()];
                                        subjectList = wantedList.toArray(subjectList);
                                        checkedItems = new boolean[wantedList.size()];
                                        selectedSubjects = new ArrayList<>();
                                        String alreadySelectedSubjects = (String) hereSkill.getJizz().get(wantedList.toString());
                                        for (int j = 0; j < wantedList.size(); j++) {
                                            if (alreadySelectedSubjects.contains(wantedList.get(j))) {
                                                checkedItems[j] = true;
                                                selectedSubjects.add(subjectList[j]);
                                            }
                                        }
                                        chosenSubjects.setText(alreadySelectedSubjects);

                                        chosenSubjects.setOnClickListener(new View.OnClickListener() {
                                            private AlertDialog mDegreeDialog;

                                            @Override
                                            public void onClick(View view) {
                                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context, R.style.RadioDialogTheme);
                                                mBuilder.setTitle("Select filter degrees");
                                                mBuilder.setMultiChoiceItems(subjectList, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                                                        if (isChecked) {
//                                                           /*((AlertDialog) mDegreeDialog).getListView().setItemChecked(position, false);
//                                                                checkedItems[position] = false;*/
                                                            if (!selectedSubjects.contains(subjectList[position])) {
                                                                selectedSubjects.add(subjectList[position]);
                                                            }
                                                            checkedItems[position] = true;
                                                        } else {
                                                            if (selectedSubjects.contains(subjectList[position])) {
                                                                selectedSubjects.remove(subjectList[position]);
                                                            }
                                                            checkedItems[position] = false;
                                                        }
                                                    }
                                                });

                                                mBuilder.setCancelable(false);
                                                mBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int which) {
                                                        if (selectedSubjects.size() > 0) {
                                                            StringBuilder item = new StringBuilder();
                                                            for (int i = 0; i < selectedSubjects.size(); i++) {
                                                                item.append(selectedSubjects.get(i));
                                                                if (i != selectedSubjects.size() - 1) {
                                                                    item.append(", ");
                                                                }
                                                            }
                                                            chosenSubjects.setText(item);
                                                        } else {
                                                            chosenSubjects.setText("");
                                                        }
                                                    }
                                                });

                                               /* mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                });*/

                                                mBuilder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int which) {
                                                        for (int i = 0; i < checkedItems.length; i++) {
                                                            checkedItems[i] = false;
                                                            selectedSubjects.clear();
                                                            chosenSubjects.setText("");
                                                        }
                                                    }
                                                });

                                                mDegreeDialog = mBuilder.create();
                                                mDegreeDialog.show();
                                            }
                                        });

                                        salaryBar.setFormatter(value -> Integer.parseInt(value) + "k");
                                        //testing the textview onclickListener here
                                        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(Locale.US);
                                        salaryTV.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Dialog d = new Dialog(context);
                                                d.setTitle("NumberPicker");
                                                d.setContentView(R.layout.number_picker_dialog_layout);
                                                Button setBtn = (Button) d.findViewById(R.id.set_btn);
                                                NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                                                TextView tittleTV = d.findViewById(R.id.sub_text);
                                                tittleTV.setText("Select Salary");
                                                np.setMaxValue(40);
                                                np.setMinValue(1);
                                                Field f = null;
                                                try {
                                                    f = NumberPicker.class.getDeclaredField("mInputText");
                                                } catch (NoSuchFieldException e) {
                                                    e.printStackTrace();
                                                }
                                                if (f != null) {
                                                    f.setAccessible(true);
                                                }
                                                EditText inputText = null;
                                                try {
                                                    inputText = (EditText) f.get(np);
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                }
                                                if (inputText != null) {
                                                    inputText.setFilters(new InputFilter[0]);
                                                }

                                                NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
                                                    @Override
                                                    public String format(int value) {
                                                        int temp = value * 1000;
                                                        String format = currencyInstance.format(temp);
                                                        return format.substring(1, format.length() - 3) + " ৳";
                                                    }
                                                };
                                                np.setFormatter(formatter);
                                                np.setWrapSelectorWheel(true);
                                                np.setValue(Integer.parseInt(salaryBar.getRightPinValue()));


                                                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                                    @Override
                                                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                                                        String format1 = currencyInstance.format(i1 * 1000);
                                                        salaryTV.setText("Salary = " + format1.substring(1, format1.length() - 3) + " ৳");
                                                        salaryBar.setSeekPinByIndex(i1 - 1);
                                                        salaryBar.setFormatter(value -> Integer.parseInt(value) + "k");
                                                    }
                                                });

                                                setBtn.setOnClickListener(v -> {
                                                    tittleTV.setText("Select Salary");
                                                    String format1 = currencyInstance.format(np.getValue() * 1000);
                                                    salaryTV.setText("Salary = " + format1.substring(1, format1.length() - 3) + " ৳");
                                                    salaryBar.setSeekPinByIndex(np.getValue() - 1);
                                                    salaryBar.setFormatter(value -> Integer.parseInt(value) + "k");
                                                    d.dismiss();
                                                });
                                                d.show();
                                            }
                                        });

                                        salaryBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                                            @Override
                                            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                                                NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(Locale.US);
                                                String format1 = currencyInstance.format(Integer.parseInt(rightPinValue) * 1000);
                                                salaryTV.setText("Salary = " + format1.substring(1, format1.length() - 3) + " ৳");
                                            }
                                        });

                                        updateYesBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                mUpdateSkill.dismiss();
                                                Map<String, Object> updateData = new HashMap<>();
                                                //make the hashmap here
                                                //get the datas now
                                                int rightIndex = salaryBar.getRightIndex() + 1;
                                                updateData.put("salary", rightIndex * 1000);
                                                Map<String, Object> jizz = hereSkill.getJizz();
                                                String shortSalary = rightIndex + "k";
                                                jizz.put("Salary", shortSalary);
                                                jizz.put(wantedList.toString(), chosenSubjects.getText().toString());
                                                updateData.put("jizz", jizz);
                                                List<String> query_list = hereSkill.getQuery_list();
                                                List<String> query_list_name = hereSkill.getQuery_list_name();

                                                for (int i = 0; i < query_list_name.size(); i++) {
                                                    if (query_list_name.get(i).equals("Salary")) {
                                                        query_list.set(i, shortSalary);
                                                    } else if (query_list_name.get(i).equals(wantedList.toString())) {
                                                        query_list.set(i, chosenSubjects.getText().toString());
                                                    }
                                                }
                                                updateData.put("query_list", query_list);

                                                Map<String, Object> preLikes = hereSkill.getLikes();
                                                Map<String, Object> preDisLikes = hereSkill.getDislikes();

                                                for (int i = 0; i < checkedItems.length; i++) {
                                                    if (checkedItems[i]) {
                                                        Object o = preLikes.get(subjectList[i]);
                                                        if (o == null) {
                                                            preLikes.put(subjectList[i], 1);
                                                            preDisLikes.put(subjectList[i], 0);
                                                        }
                                                    }
                                                }
                                                updateData.put("likes", preLikes);
                                                updateData.put("dislikes", preDisLikes);
                                                new DumeModel(context).updateSkill(hereSkill.getId(), updateData, new TeacherContract.Model.Listener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void list) {
                                                        Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onError(String msg) {
                                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });

                                        updateNoBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                mUpdateSkill.dismiss();
                                            }
                                        });
                                    }
                                    mUpdateSkill.show();
                                    break;
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });

            int salary1 = (int) skillList.get(i).getSalary();
            String format2 = NumberFormat.getCurrencyInstance(Locale.US).format(salary1);
            myViewHolder.salaryTV.setText(format2.substring(1, format2.length() - 3) + " BDT");
            myViewHolder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    //testing here
                    boolean obligation = Google.getInstance().isObligation();
                    boolean accountActive = (boolean) TeacherDataStore.getInstance().getDocumentSnapshot().get("account_active");
                    if (obligation) {
                        myViewHolder.switchCompat.setChecked(false);
                        flush("Please pay your due obligation to dume to make your skill active again...");
                    } else if (!accountActive) {
                        myViewHolder.switchCompat.setChecked(false);
                        flush("Your account status is inactive.Please toggle your account status first to activate skill...");
                    } else {
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
                }
            });
            myViewHolder.enrolledTV.setText("Enrolled Students : " + skillList.get(i).getEnrolled());

            //init review recycler
            new DumeModel(context).loadReview(skill.getId(), null, new TeacherContract.Model.Listener<List<ReviewHighlightData>>() {
                @Override
                public void onSuccess(List<ReviewHighlightData> list) {

                    lastReviewData = list.get(list.size() - 1);
                    reviewRecyAda = new ReviewAdapter(context, list, skill.getId(),true);
                    myViewHolder.reviewRecycler.setAdapter(reviewRecyAda);
                    myViewHolder.reviewRecycler.setLayoutManager(new LinearLayoutManager(context));
                    myViewHolder.loadMoreBTN.setEnabled(false);
                    myViewHolder.loadMoreBTN.setVisibility(View.GONE);
                    /*if (list.size() >= 10) {
                        myViewHolder.loadMoreBTN.setEnabled(true);
                    } else {
                        myViewHolder.loadMoreBTN.setEnabled(false);
                        myViewHolder.loadMoreBTN.setVisibility(View.GONE);
                    }*/
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

            /*myViewHolder.loadMoreBTN.setOnClickListener(view -> {
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
            });*/


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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (d instanceof Animatable) {
                            ((Animatable) d).start();
                        }
                        ((Animatable2) d).registerAnimationCallback(new Animatable2.AnimationCallback() {
                            public void onAnimationEnd(Drawable drawable) {
                                //Do something
                                if (visible) {
                                    myViewHolder.reviewShowBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, context.getResources().getDrawable(R.drawable.ic_up_arrow_small));
                                    myViewHolder.hostingRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                                } else {
                                    myViewHolder.reviewShowBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, context.getResources().getDrawable(R.drawable.ic_down_arrow_small));
                                    myViewHolder.hostingRelativeLayout.setBackground(context.getResources().getDrawable(R.drawable.bg_white_bottom_round));
                                }
                                myViewHolder.reviewShowBtn.setEnabled(true);
                            }
                        });
                    } else {
                        if (visible) {
                            myViewHolder.reviewShowBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, context.getResources().getDrawable(R.drawable.ic_up_arrow_small));
                            myViewHolder.reviewShowBtn.setEnabled(true);
                            myViewHolder.hostingRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));

                        } else {
                            myViewHolder.reviewShowBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, context.getResources().getDrawable(R.drawable.ic_down_arrow_small));
                            myViewHolder.reviewShowBtn.setEnabled(true);
                            myViewHolder.hostingRelativeLayout.setBackground(context.getResources().getDrawable(R.drawable.bg_white_bottom_round));
                        }
                    }
                }
            });

            HashMap<String, Object> jizz = skillList.get(i).getJizz();
            if (getLast(jizz) != null) {
                final Object o = getLast(jizz);
                myViewHolder.skillTitleTV.setText(o.toString() + " / " + jizz.get("Category"));
                myViewHolder.categoryAvatar.setImageResource(iconList.get(jizz.get("Category")));
                String mainSsss = o.toString();
                splitMainSsss = mainSsss.split("\\s*(=>|,)\\s*");
            }
            for (String splited : splitMainSsss) {
                likes = likes + Integer.parseInt(skillList.get(i).getLikes().get(splited).toString());
                dislikes = dislikes + Integer.parseInt(skillList.get(i).getDislikes().get(splited).toString());

            }
            myViewHolder.likeTV.setText((likes - splitMainSsss.length) + " likes");
            likes = 0;

        } else {//fragment start here
            SkillFVH myFragmentHolder = (SkillFVH) holder;
            myFragmentHolder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    boolean obligation = Google.getInstance().isObligation();
                    boolean accountActive = (boolean) TeacherDataStore.getInstance().getDocumentSnapshot().get("account_active");
                    if (obligation) {
                        if (compoundButton.isChecked() != false || compoundButton.isChecked() != skillList.get(i).isStatus()) {
                            myFragmentHolder.switchCompat.setChecked(false);
                            flush("Please pay your due obligation to dume to make your skill active again...");
                        }
                    } else if (!accountActive) {
                        if (compoundButton.isChecked() != false || compoundButton.isChecked() != skillList.get(i).isStatus()) {
                            myFragmentHolder.switchCompat.setChecked(false);
                            flush("Your account status is inactive.Please toggle your account status first to activate skill...");
                        }
                    } else {
                        if (compoundButton.isChecked() != false || compoundButton.isChecked() != skillList.get(i).isStatus()) {
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
                    }
                }
            });
            myFragmentHolder.itemView.setOnClickListener(view -> {
                context.startActivity(new Intent(context, SkillActivity.class));
            });

            //fixing width
            ViewGroup.LayoutParams layoutParams = myFragmentHolder.hostingRelative.getLayoutParams();
            layoutParams.width = (itemWidth);
            myFragmentHolder.hostingRelative.setLayoutParams(layoutParams);


            HashMap<String, Object> jizz = skillList.get(i).getJizz();
            if (getLast(jizz) != null) {
                String o = getLast(jizz);
                myFragmentHolder.skillTitleTV.setText(o + " / " + jizz.get("Category"));
                splitMainSsss = o.split("\\s*(=>|,)\\s*");
            }
            myFragmentHolder.switchCompat.setChecked(skillList.get(i).isStatus());
            for (String splited : splitMainSsss) {
                try{
                    likes = likes + Integer.parseInt(skillList.get(i).getLikes().get(splited).toString());
                    dislikes = dislikes + Integer.parseInt(skillList.get(i).getDislikes().get(splited).toString());
                }catch (Exception e){
                    likes = 0;
                    dislikes = 0;
                }
            }


            int totalCount = likes + dislikes;
            if (totalCount == 0) {
                myFragmentHolder.likeTV.setText("n/a");
            } else {
                int likeP = (int) (likes / totalCount) * 100;
                myFragmentHolder.likeTV.setText(likeP + "% liked");
            }
            int salary = (int) (skillList.get(i).getSalary()) / 1000;

            String package_name = skillList.get(i).getPackage_name();
            if (package_name != null) {
                if(package_name.equals(SearchDataStore.REGULAR_DUME)){
                    package_name = "Monthly Tutor";
                } else if (package_name.equals(SearchDataStore.INSTANT_DUME)) {
                    package_name = "Weekly Tutor";
                }else {
                    package_name = "Couching Service";
                }
            }
            myFragmentHolder.packageName.setText(package_name + " / " + salary + " k");
        }
    }

    public void flush(String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

   /* public String getLast(int i) {
        HashMap<String, Object> jizz = skillList.get(i).getJizz();
        for (int j = 0; j < endOfNest.size(); j++) {
            if (jizz.containsKey(endOfNest.get(j))) {
                return endOfNest.get(j);
            }
        }
        return null;
    }*/


    public void update(ArrayList<Skill> skillList) {
        this.skillList.clear();
        this.skillList.addAll(skillList);
        this.notifyDataSetChanged();
        Log.w(TAG, "getItemCount: " + skillList.toString());
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
        } else {
            Log.w(TAG, "getItemCount: " + skillList.size());
            return 0;
        }
    }

    class SkillAVH extends RecyclerView.ViewHolder {

        @BindView(R.id.skillDetailsRV)
        RecyclerView detailsRV;
        @BindView(R.id.skillSalaryTV)
        TextView salaryTV;
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
