package io.dume.dume.student.heatMap;

public interface HeatMapContract {
    interface View {

        void configHeatMap();

        void initHeatMap();

        void findView();

    }

    interface Presenter {

        void heatMapEnqueue();

        void onHeatMapViewIntracted(android.view.View view);

    }

    interface Model {

        void heatMaphawwa();
    }
}