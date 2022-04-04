package com.example.client.screens.wallet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.client.R;
import com.example.client.app.Constants;
import com.example.client.dialog.PrimaryDialog;
import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.models.subject.SubjectModel;
import com.example.client.models.transaction.TransactionModel;
import com.example.client.screens.wallet.activity.RechargeActivity;
import com.example.client.screens.wallet.item.TransactionItem;
import com.example.client.screens.wallet.present.WalletPresent;
import com.google.android.material.tabs.TabLayout;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class WalletFragment extends Fragment implements IWalletView, View.OnClickListener {
    private TextView wallet;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private ProfileModel user;
    private WalletPresent wPresent;
    private TextView request;
    private ImageView empty;
    private SwipeRefreshLayout refreshLayout;
    private PrimaryDialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet,null);
        wallet = view.findViewById(R.id.wallet);
        tabLayout = view.findViewById(R.id.tabLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        request = view.findViewById(R.id.request);
        empty = view.findViewById(R.id.empty);
        refreshLayout = view.findViewById(R.id.container);
        dialog = new PrimaryDialog();
        dialog.getInstance(getContext());

        refreshLayout.setOnRefreshListener(() -> {
            wPresent.onRefeshUserActive(user.getEmail());
            tabLayout.selectTab(tabLayout.getTabAt(0));
            wPresent.onShowTransaction(Constants.TRANSACTION.INPUT, user.getId());
        });

        tabLayout.addTab(tabLayout.newTab().setText("NẠP TIỀN"));
        tabLayout.addTab(tabLayout.newTab().setText("GIAO DỊCH"));
        wPresent = new WalletPresent(this);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    wPresent.onShowTransaction(Constants.TRANSACTION.INPUT, user.getId());
                }
                else {
                    wPresent.onShowTransaction(Constants.TRANSACTION.OUPUT, user.getId());
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        request.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        wPresent.onShowInfoUser();
        wPresent.onShowTransaction(Constants.TRANSACTION.INPUT, user.getId());
        tabLayout.selectTab(tabLayout.getTabAt(0));
    }

    @Override
    public void showInfoUser(ProfileModel user) {
        this.user = user;
        Locale localeVN = new Locale("vi","VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeVN);
        wallet.setText(format.format(user.getWallet()));
    }

    @Override
    public void showTransaction(List<TransactionModel> items, String method) {
        if(items == null || items.size() == 0){
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            TransactionItem item = new TransactionItem(getContext(),items,method, ordercode -> {
                dialog.setDescription("Chắc chắn hủy yêu cầu?");
                dialog.setOKListener(()->wPresent.onDeleteRecharge(ordercode));
                dialog.setCancelListener(()->{});
                dialog.show();
            });
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(item);
        }

    }

    @Override
    public void showTransactionByOrderCode(TransactionModel item) {

    }

    @Override
    public void getSubject(SubjectModel item) {

    }

    @Override
    public void deleteRecharge(MessageModel message) {
        if(message.isStatus()){
            dialog.setDescription("Hủy thành công");
            dialog.hideBtnCancel();
            dialog.show();
            dialog.setOKListener(()->onResume());
        }
        else {
            switch (message.getCode()){
                case Constants.ErrorCode.ERROR_1001:
                    dialog.setDescription(getString(R.string.err_code_1001));
                    break;
            }
            dialog.setOKListener(()->{});
            dialog.hideBtnCancel();
            dialog.show();
        }
    }

    @Override
    public void requestRecharge(MessageModel message) {

    }

    @Override
    public void refreshUserActive(MessageModel message) {
        if(message.isStatus()){
            refreshLayout.setRefreshing(false);
            wPresent.onShowInfoUser();
        }
        else {
            refreshLayout.setRefreshing(false);
            Toast.makeText(getContext(),"Đã xảy ra lỗi, vui lòng thử lại",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.request:
                startActivity(new Intent(getActivity(), RechargeActivity.class));
                break;
        }
    }

}
