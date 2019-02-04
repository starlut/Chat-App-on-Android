package star.lut.com.chatdemo.Base;

/**
 * Created by kamrulhasan on 29/10/18.
 */
public interface MvpView {
    void showProgress();

    void hideProgress();

    void onError(Throwable t);
}
