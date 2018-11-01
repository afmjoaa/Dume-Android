package io.dume.dume.teacher.crudskill;

import java.util.List;

import io.dume.dume.util.BaseContract;

public interface CrudContract {
    interface View extends BaseContract.View {
        void setUpRecyclerView(List<String> categoryList, List<Integer> drawableList);
    }

    interface Model extends BaseContract.Model {

    }

    interface Presenter extends BaseContract.Presenter {

    }
}
