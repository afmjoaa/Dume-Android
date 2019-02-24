package io.dume.dume.teacher.adapters;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import io.dume.dume.teacher.pojo.TabModel;
import q.rorbin.verticaltablayout.widget.ITabView;

public class MyTabAdapter implements q.rorbin.verticaltablayout.adapter.TabAdapter {
    private List<TabModel> tabModelArrayList;


    public MyTabAdapter(List<TabModel> tabModelArrayList) {
        super();
        this.tabModelArrayList = tabModelArrayList;


    }

    public void updateList(List<TabModel> tabModelArrayList) {
        this.tabModelArrayList.clear();
        this.tabModelArrayList.addAll(tabModelArrayList);
        notify();
        notifyAll();
    }

    @Override
    public int getCount() {
        return tabModelArrayList.size();
    }

    @Override
    public ITabView.TabBadge getBadge(int position) {
        if (tabModelArrayList.get(position).getBadge() > 0) {
            return new ITabView.TabBadge.Builder().setBadgeNumber(2).setBadgeText(tabModelArrayList.get(position).getBadge() + "").build();
        }
        return null;
    }

    @Override
    public ITabView.TabIcon getIcon(int position) {
        int normalIcon = tabModelArrayList.get(position).getNormalIcon();
        int selected = tabModelArrayList.get(position).getSelectedIcon();
        return new ITabView.TabIcon.Builder().setIcon(selected, normalIcon).build();
    }

    @Override
    public ITabView.TabTitle getTitle(int position) {
        return null;
    }

    @Override
    public int getBackground(int position) {
        return 0;
    }

}
