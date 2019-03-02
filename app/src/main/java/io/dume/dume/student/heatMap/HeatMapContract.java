package io.dume.dume.student.heatMap;

public interface HeatMapContract {
    interface View {

        void configHeatMap();

        void initHeatMap();

        void findView();

        void viewMuskClicked();

        void flush(String msg);

        void onCenterCurrentLocation();
    }

    interface Presenter {

        void heatMapEnqueue();

        void onHeatMapViewIntracted(android.view.View view);

    }

    interface Model {

        void heatMaphawwa();
    }
}
