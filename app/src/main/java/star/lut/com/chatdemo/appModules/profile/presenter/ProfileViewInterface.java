package star.lut.com.chatdemo.appModules.profile.presenter;

import star.lut.com.chatdemo.Base.MvpView;
import star.lut.com.chatdemo.dataModels.receiveModel.Profile;

/**
 * Created by kamrulhasan on 14/11/18.
 */
public interface ProfileViewInterface extends MvpView {
    void onProfilePicUpdated();
    void onLogout();
}
