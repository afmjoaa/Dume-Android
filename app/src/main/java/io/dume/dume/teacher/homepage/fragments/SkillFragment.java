package io.dume.dume.teacher.homepage.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hanks.htextview.scale.ScaleTextView;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import carbon.widget.ImageView;
import io.dume.dume.R;
import io.dume.dume.model.DumeModel;
import io.dume.dume.teacher.adapters.AcademicAdapter;
import io.dume.dume.teacher.adapters.SkillAdapter;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.teacher.pojo.Skill;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.GridSpacingItemDecoration;

public class SkillFragment extends Fragment {

    private SkillViewModel mViewModel;
    @BindView(R.id.skillRV)
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
    LinearLayout noDataBlock;
    @BindView(R.id.tipsTV)
    ScaleTextView scaleTextView;
    private TeacherActivtiy fragmentActivity;
    private TeacherDataStore teacherDataStore;
    private static SkillFragment skillFragment = null;
    private int itemWidth;

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
                    new DumeModel().getSkill(new TeacherContract.Model.Listener<ArrayList<Skill>>() {
                        @Override
                        public void onSuccess(ArrayList<Skill> list) {
                            teacherDataStore.setSkillArrayList(list);
                            skillRV.setAdapter(new SkillAdapter(fragmentActivity, SkillAdapter.FRAGMENT, itemWidth, list));
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
                    skillRV.setAdapter(new SkillAdapter(fragmentActivity, SkillAdapter.FRAGMENT, itemWidth, teacherDataStore.getSkillArrayList()));
                    if (teacherDataStore.getSkillArrayList().size() == 0) {
                        noDataBlock.setVisibility(View.VISIBLE);
                    } else {
                        noDataBlock.setVisibility(View.GONE);
                    }
                }
            }
        } else {
            //not visible here
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.skill_fragment, container, false);
        ButterKnife.bind(this, root);

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

        fragmentActivity = (TeacherActivtiy) getActivity();
        teacherDataStore = fragmentActivity != null ? fragmentActivity.teacherDataStore : null;
        if (teacherDataStore != null) {
            if (teacherDataStore.getSkillArrayList() == null) {
                new DumeModel().getSkill(new TeacherContract.Model.Listener<ArrayList<Skill>>() {
                    @Override
                    public void onSuccess(ArrayList<Skill> list) {
                        teacherDataStore.setSkillArrayList(list);
                        skillRV.setAdapter(new SkillAdapter(fragmentActivity, SkillAdapter.FRAGMENT, itemWidth, list));
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
                skillRV.setAdapter(new SkillAdapter(fragmentActivity, SkillAdapter.FRAGMENT, itemWidth, teacherDataStore.getSkillArrayList()));
                if (teacherDataStore.getSkillArrayList().size() == 0) {
                    noDataBlock.setVisibility(View.VISIBLE);
                } else {
                    noDataBlock.setVisibility(View.GONE);
                }
                //loadData();
            }
        } else {
            new DumeModel().getSkill(new TeacherContract.Model.Listener<ArrayList<Skill>>() {
                @Override
                public void onSuccess(ArrayList<Skill> list) {
                    teacherDataStore.setSkillArrayList(list);
                    skillRV.setAdapter(new SkillAdapter(fragmentActivity, SkillAdapter.FRAGMENT, itemWidth, list));
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
        scaleTextView.animateText(sequence);
        scaleTextView.setTypeface(Typeface.createFromAsset(Objects.requireNonNull(getContext()).getAssets(), "fonts/Cairo_Regular.ttf"));
        scaleTextView.setAnimationListener(hTextView -> {

        });
        scaleTextView.setSelected(true);
    }//  tips("Every person is a new door to a different world.");

}
