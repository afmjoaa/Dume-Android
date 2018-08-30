package io.dume.dume.student.grabingInfo;

public class RecycleData {
    String options;
}

/*
//put this inside placeholderFragment which extends fragment
    private RecyclerAdapter recyclerAdapter;
    public static List<RecycleData> getData(){
            List<RecycleData> data = new ArrayList<>();
            String[] titles = {"Bangla Medium", "Bangla Version", "English Medium", "English Version", "Others"};
            for (String title : titles) {
                RecycleData current = new RecycleData();
                current.options = title;
                data.add(current);
            }
            return data;
        }
// put this inside the onCreateView
        RecyclerView mRecyclerView = radioButtonLayout.findViewById(R.id.recycler_view_list);
        recyclerAdapter = new RecyclerAdapter(getActivity(), getData());
        mRecyclerView.setAdapter(recyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
*/
