package com.example.client.screens.main.present;

import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.service.OrderService;
import com.example.client.api.service.UserService;
import com.example.client.app.Constants;
import com.example.client.app.Preferences;
import com.example.client.models.cart.CartModel;
import com.example.client.models.order.OrderModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.screens.main.activity.IMainView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresent implements IMainPresent {

    private IMainView mView;
    private ProfileModel user;

    public MainPresent(IMainView mView){
        user = Preferences.getInstance().getProfile();
        onSetUserActive();
        this.mView = mView;
    }

    @Override
    public void replaceFragment(int id) {
        switch (id){
            case R.id.menu_home:
                mView.showHomeScreen();
                break;
            case R.id.menu_wallet:
                mView.showNotiScreen();
                break;
            case R.id.menu_profile:
                mView.showProfileScreen();
                break;
        }
    }

    @Override
    public void onSetUserActive() {
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.getUserByEmail(user.getEmail()).enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                Preferences.getInstance().setProfile(response.body());
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void getCartFromRes() {
        CartModel cart = Preferences.getInstance().getCart() != null ? Preferences.getInstance().getCart() : new CartModel(new ArrayList<>());
        cart.getListProduct();
        for (int i = cart.getListProduct().size() - 1; i >= 0; i--) {
            if(cart.getListProduct().get(i).getQuantity() <= 0) {
                cart.getListProduct().remove(i);
            }
        }
        if (!Objects.requireNonNull(cart.getListProduct()).isEmpty()) {
            mView.showCart(cart.getListProduct().size());
        } else {
            mView.hideCart();
        }
    }

    @Override
    public void getListOrderFromService() {
        mView.showLoading();
        OrderService service = ApiClient.getInstance().create(OrderService.class);
        ProfileModel profile = Preferences.getInstance().getProfile();
        service.getByUser(profile.getId()).enqueue(new Callback<List<OrderModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<OrderModel>> call, @NotNull Response<List<OrderModel>> response) {
                if (response.body() == null) {
                    mView.hideOrder();
                    mView.hideOrderCount();
                    mView.hideLoading();
                    return;
                }
                if (response.body().isEmpty()) {
                    mView.hideOrder();
                    mView.hideOrderCount();
                    mView.hideLoading();
                    return;
                }
                int count = 0;
                for (OrderModel order: response.body()) {
                    if (!order.isComplete() && !order.isDestroy()) count++;
                }
                if (count > 1) {
                    mView.showOrderCount(count);
                } else {
                    mView.hideOrderCount();
                }
                mView.hideLoading();
                for (int i = 0; i< response.body().size();i++) {
                    if (!response.body().get(i).isDestroy() && !response.body().get(i).isComplete()) {
                        mView.showOrder(response.body().get(i));
                        mView.hideLoading();
                        return;
                    }
                }
                mView.hideOrder();

            }

            @Override
            public void onFailure(@NotNull Call<List<OrderModel>> call, @NotNull Throwable t) {
                mView.hideOrder();
                mView.hideOrderCount();
                mView.hideLoading();

            }
        });
    }

    private int getErrorMessage(int errorCode) {
        int errMessage = -1;
        switch (errorCode) {
            case Constants.ErrorCode.ERROR_1001:
                errMessage = R.string.err_code_1001;
                break;
            case Constants.ErrorCode.ERROR_1002:
                errMessage = R.string.err_code_1002;
                break;
            case Constants.ErrorCode.ERROR_1003:
                errMessage = R.string.err_code_1003;
                break;
            case Constants.ErrorCode.ERROR_1004:
                errMessage = R.string.err_code_1004;
                break;
            case Constants.ErrorCode.ERROR_1005:
                errMessage = R.string.err_code_1005;
                break;
            case Constants.ErrorCode.ERROR_1006:
                errMessage = R.string.err_code_1006;
                break;
            case Constants.ErrorCode.ERROR_1007:
                errMessage = R.string.err_code_1007;
                break;
            case Constants.ErrorCode.ERROR_1008:
                errMessage = R.string.err_code_1008;
                break;
            case Constants.ErrorCode.ERROR_1009:
                errMessage = R.string.err_code_1009;
                break;
            case Constants.ErrorCode.ERROR_1010:
                errMessage = R.string.err_code_1010;
                break;
            case Constants.ErrorCode.ERROR_1011:
                errMessage = R.string.err_code_1011;
                break;
            case Constants.ErrorCode.ERROR_1012:
                errMessage = R.string.err_code_1012;
                break;
            case Constants.ErrorCode.ERROR_1013:
                errMessage = R.string.err_code_1013;
                break;
        }
        return errMessage;
    }
}
