package io.dume.dume.student.studentPayment;

public interface StudentPaymentContract {
    interface View {

        void configStudentPayment();

        void initStudentPayment();

        void findView();

    }

    interface Presenter {

        void studentPaymentEnqueue();

        void onStudentPaymentIntracted(android.view.View view);

    }

    interface Model {

        void studentPaymentHawwa();
    }
}
