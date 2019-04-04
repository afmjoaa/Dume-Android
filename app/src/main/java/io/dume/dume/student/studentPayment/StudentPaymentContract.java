package io.dume.dume.student.studentPayment;

public interface StudentPaymentContract {
    interface View {

        void configStudentPayment();

        void initStudentPayment();

        void findView();

        void onAddPromoCodeApplied();

        void onViewPromotionsClicked();

        void onTransactionHistoryClicked();

        void onObligtionClaimClicked();

    }

    interface Presenter {

        void studentPaymentEnqueue();

        void onStudentPaymentIntracted(android.view.View view);

    }

    interface Model {

        void studentPaymentHawwa();
    }
}
