package io.dume.dume.auth.code_verification;

public class PhoneVerficationPresenter implements PhoneVerificationContract.Presenter {
    PhoneVerificationContract.View view;
    PhoneVerificationContract.Model model;

    public PhoneVerficationPresenter(PhoneVerificationContract.View view, PhoneVerificationContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void enqueue() {
        view.findView();
        view.initActionBar();
        view.init();

    }
}
