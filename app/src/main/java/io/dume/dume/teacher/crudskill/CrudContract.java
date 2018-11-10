package io.dume.dume.teacher.crudskill;

import java.util.List;

import io.dume.dume.util.MyBaseContract;

public interface CrudContract {
    interface View extends MyBaseContract.View {
        void setUpRecyclerView(List<String> categoryList, List<Integer> drawableList);
    }

    interface Model extends MyBaseContract.Model {

    }

    interface Presenter extends MyBaseContract.Presenter {

    }
}
