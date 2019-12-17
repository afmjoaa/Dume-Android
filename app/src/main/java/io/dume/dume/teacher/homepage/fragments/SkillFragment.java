package io.dume.dume.teacher.homepage.fragments;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hanks.htextview.scale.ScaleTextView;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import carbon.widget.ImageView;
import io.dume.dume.R;
import io.dume.dume.model.DumeModel;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.teacher.adapters.SkillAdapter;
import io.dume.dume.teacher.crudskill.CrudSkillActivity;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.teacher.pojo.Skill;
import io.dume.dume.teacher.skill.SkillActivity;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.GridSpacingItemDecoration;

public class SkillFragment extends Fragment {

    private SkillViewModel mViewModel;
    @BindView(R.id.skillRV)
    public
    RecyclerView skillRV;
    @BindView(R.id.more_btn)
    Button openSkillBtn;
    @BindView(R.id.add_regular_dume_btn)
    ImageView addRegularDBtn;
    @BindView(R.id.add_dume_gang_btn)
    ImageView addDumeGangBtn;
    @BindView(R.id.add_instant_btn)
    ImageView addInstantDBtn;
    @BindView(R.id.no_data_block)
    public
    LinearLayout noDataBlock;

    private TeacherActivtiy fragmentActivity;
    private TeacherDataStore teacherDataStore;
    private static SkillFragment skillFragment = null;
    public int itemWidth;
    private Map<String, Object> documentSnapshot;
    private int percentage;
    private Context context;
    public SkillAdapter skillAdapter;


    public static SkillFragment getInstance() {
        if (skillFragment == null) {
            skillFragment = new SkillFragment();
        }
        return skillFragment;
    }

    public static SkillFragment newInstance() {
        return new SkillFragment();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (fragmentActivity != null && teacherDataStore != null) {
                if (teacherDataStore.getSkillArrayList() == null) {
                    new DumeModel(getContext()).getSkill(new TeacherContract.Model.Listener<ArrayList<Skill>>() {
                        @Override
                        public void onSuccess(ArrayList<Skill> list) {
                            teacherDataStore.setSkillArrayList(list);
                            skillAdapter = new SkillAdapter(fragmentActivity, SkillAdapter.FRAGMENT, itemWidth, list);
                            skillRV.setAdapter(skillAdapter);
                            if (list.size() == 0) {
                                noDataBlock.setVisibility(View.VISIBLE);

                            } else {
                                noDataBlock.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    skillAdapter = new SkillAdapter(fragmentActivity, SkillAdapter.FRAGMENT, itemWidth, teacherDataStore.getSkillArrayList());
                    skillRV.setAdapter(skillAdapter);
                    if (teacherDataStore.getSkillArrayList().size() == 0) {
                        noDataBlock.setVisibility(View.VISIBLE);

                    } else {
                        noDataBlock.setVisibility(View.GONE);
                    }
                }
                changeAddSkillBtnColor();
            }
        } else {
            //not visible here
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        documentSnapshot = TeacherDataStore.getInstance().getDocumentSnapshot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.skill_fragment, container, false);
        ButterKnife.bind(this, root);

        //scaleTextView.setTypeface(Typeface.createFromAsset(Objects.requireNonNull(getContext()).getAssets(), "fonts/Cairo_Regular.ttf"));
        assert container != null;
        int[] wh = DumeUtils.getScreenSize(container.getContext());
        float mDensity = getResources().getDisplayMetrics().density;
        int spacing = (int) ((wh[0] - ((336) * (getResources().getDisplayMetrics().density))) / 3);
        int availableWidth = (int) (wh[0] - (93 * mDensity));
        skillRV.setLayoutManager(new GridLayoutManager(getContext(), 2));
        skillRV.addItemDecoration(new GridSpacingItemDecoration(2, (int) (10 * mDensity), true));

        //getting the item width
        itemWidth = (int) ((availableWidth - (30 * mDensity)) / 2);

        //btn size fix
        int bottomBtnWidth = (int) ((availableWidth - (23 * mDensity)) / 4);
        int paddingMore = (int) ((bottomBtnWidth - (24 * mDensity)) / 2);
        int paddingDefault = (int) ((bottomBtnWidth - (22 * mDensity)) / 2);
        ViewGroup.LayoutParams layoutParams = openSkillBtn.getLayoutParams();
        layoutParams.width = (bottomBtnWidth);
        openSkillBtn.setLayoutParams(layoutParams);
        openSkillBtn.setPadding(paddingMore, 0, paddingMore, 0);

        ViewGroup.LayoutParams layoutParamsRegular = addRegularDBtn.getLayoutParams();
        layoutParamsRegular.width = (bottomBtnWidth);
        addRegularDBtn.setLayoutParams(layoutParamsRegular);
        addRegularDBtn.setPadding(paddingDefault, 0, paddingDefault, 0);

        ViewGroup.LayoutParams layoutParamsGang = addDumeGangBtn.getLayoutParams();
        layoutParamsGang.width = (bottomBtnWidth);
        addDumeGangBtn.setLayoutParams(layoutParamsGang);
        addDumeGangBtn.setPadding(paddingDefault, 0, paddingDefault, 0);

        ViewGroup.LayoutParams layoutParamsInstant = addInstantDBtn.getLayoutParams();
        layoutParamsInstant.width = (bottomBtnWidth);
        addInstantDBtn.setLayoutParams(layoutParamsInstant);
        addInstantDBtn.setPadding(paddingDefault, 0, paddingDefault, 0);

        openSkillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SkillActivity.class).setAction("fromFrag"));
            }
        });

        addRegularDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isProfileOK()) {
                    TeacherDataStore.getInstance().setPackageName(SearchDataStore.REGULAR_DUME);
                    goToCrudActivity("frag_" + DumeUtils.TEACHER);
                }
            }
        });

        addDumeGangBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isProfileOK()) {
                    TeacherDataStore.getInstance().setPackageName(SearchDataStore.DUME_GANG);
                    goToCrudActivity("frag_" + DumeUtils.BOOTCAMP);
                }
            }
        });

        addInstantDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isProfileOK()) {
                    TeacherDataStore.getInstance().setPackageName(SearchDataStore.INSTANT_DUME);
                    goToCrudActivity("frag_" + DumeUtils.TEACHER);
                }
                /*documentSnapshot = TeacherDataStore.getInstance().getDocumentSnapshot();
                final Map<String, Boolean> achievements = (Map<String, Boolean>) documentSnapshot.get("achievements");
                assert achievements != null;
                Boolean premier = achievements.get("premier");
                if (isProfileOK()) {
                    if (premier!= null && premier) {
                        goToCrudActivity("frag_" + DumeUtils.TEACHER);
                    } else {
                        tips("Unlocking Premier Badge is must...");
                    }
                }*/
            }
        });

        changeAddSkillBtnColor();
        fragmentActivity = (TeacherActivtiy) getActivity();
        teacherDataStore = TeacherDataStore.getInstance();
        if (teacherDataStore != null) {
            if (teacherDataStore.getSkillArrayList() == null) {
                new DumeModel(getContext()).getSkill(new TeacherContract.Model.Listener<ArrayList<Skill>>() {
                    @Override
                    public void onSuccess(ArrayList<Skill> list) {
                        teacherDataStore.setSkillArrayList(list);
                        skillAdapter = new SkillAdapter(fragmentActivity, SkillAdapter.FRAGMENT, itemWidth, list);
                        skillRV.setAdapter(skillAdapter);

                        if (list.size() == 0) {
                            noDataBlock.setVisibility(View.VISIBLE);
                            DumeUtils.notifyDialog(getContext(), false, true, "Add your skill to get User !!", "Right now you do not have any skill. To get students nearest you, You should add your skill right now. If you do not add skill, You will never get tution offer from student.", "Add Skill", new TeacherContract.Model.Listener<Boolean>() {
                                @Override
                                public void onSuccess(Boolean yes) {
                                    if (yes) {
                                        startActivity(new Intent(getContext(), SkillActivity.class));
                                    }
                                }

                                @Override
                                public void onError(String msg) {
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                                }
                            });
                        } else {
                            noDataBlock.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                skillAdapter = new SkillAdapter(fragmentActivity, SkillAdapter.FRAGMENT, itemWidth, teacherDataStore.getSkillArrayList());
                skillRV.setAdapter(skillAdapter);
                if (teacherDataStore.getSkillArrayList().size() == 0) {
                    noDataBlock.setVisibility(View.VISIBLE);
                } else {
                    noDataBlock.setVisibility(View.GONE);
                }
                //loadData();
            }
        }
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SkillViewModel.class);
        // TODO: Use the ViewModel
    }

    public void tips(CharSequence sequence) {
        //scaleTextView.setSelected(true);
        //scaleTextView.animateText(sequence);
        /*scaleTextView.setAnimationListener(hTextView -> {
        });*/

    }


    protected boolean isProfileOK() {
        documentSnapshot = TeacherDataStore.getInstance().getDocumentSnapshot();

        if (documentSnapshot != null) {
            String beh = (String) documentSnapshot.get("pro_com_%");
            percentage = Integer.parseInt(beh);
            if (percentage >= 95) {
                return true;
            }
        }
        tips("Profile should be 95% completed...");
        return false;

    }

    public void goToCrudActivity(String action) {
        Intent intent = new Intent(context, CrudSkillActivity.class).setAction(action);
        startActivity(intent);
    }

    public void changeAddSkillBtnColor() {
        documentSnapshot = TeacherDataStore.getInstance().getDocumentSnapshot();
        Map<String, Boolean> achievements = (Map<String, Boolean>) TeacherDataStore.getInstance().getDocumentSnapshot().get("achievements");
        Boolean premier;
        if (documentSnapshot != null) {
            /*premier = achievements.get("premier");
            if (premier!= null && premier) {
                addInstantDBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.dume_instant_image));
            } else {
                addInstantDBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.dume_instant_grayscale_image));
            }*/
            String beh = (String) documentSnapshot.get("pro_com_%");
            assert beh != null;
            int percentage = Integer.parseInt(beh);
            if (percentage >= 95) {
                try {
                    addRegularDBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.dume_regular_image));
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                addDumeGangBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.dume_gang_image));
                addInstantDBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.dume_instant_image));
            } else {
                try {
                    addRegularDBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.dume_regular_grayscale_image));
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                addDumeGangBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.dume_gang_grayscale_image));
                addInstantDBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.dume_instant_grayscale_image));
            }
        }
    }
}

/*else {
            new DumeModel(getContext()).getSkill(new TeacherContract.Model.Listener<ArrayList<Skill>>() {
                @Override
                public void onSuccess(ArrayList<Skill> list) {
                    //TeacherDataStore.getInstance().setSkillArrayList(list);
                    if (skillRV.getAdapter() != null && skillAdapter!= null) {
                        skillAdapter.update(list);
                    }else {
                        skillAdapter = new SkillAdapter(fragmentActivity, SkillAdapter.FRAGMENT, itemWidth, list);
                        skillRV.setAdapter(skillAdapter);
                    }
                    if (list.size() == 0) {
                        noDataBlock.setVisibility(View.VISIBLE);
                    } else {
                        noDataBlock.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(String msg) {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        }*/