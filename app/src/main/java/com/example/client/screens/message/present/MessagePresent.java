package com.example.client.screens.message.present;

import com.example.client.models.message.MessengerModel;
import com.example.client.screens.message.activity.IMessageView;

import java.util.ArrayList;
import java.util.List;

public class MessagePresent implements IMessagePresent{
    private IMessageView mView;

    public MessagePresent(IMessageView mView) {
        this.mView = mView;
    }

    @Override
    public void onShowMessages() {
        List<MessengerModel> list = new ArrayList<>();
        list.add(new MessengerModel(1,"Hello world 1","20-12-2022 14:00",false));
        list.add(new MessengerModel(1,"Hello world 2","20-12-2022 14:00",true));
        list.add(new MessengerModel(1,"Hello world 3","20-12-2022 14:00",false));
        list.add(new MessengerModel(1,"Hello world 4","20-12-2022 14:00",false));
        list.add(new MessengerModel(1,"Hello world 5","20-12-2022 14:00",true));
        list.add(new MessengerModel(1,"Hello world 6","20-12-2022 14:00",true));
        list.add(new MessengerModel(1,"Hello world 7","20-12-2022 14:00",false));
        list.add(new MessengerModel(1,"Hello world 8","20-12-2022 14:00",false));
        list.add(new MessengerModel(1,"Hello world 9","20-12-2022 14:00",true));
        list.add(new MessengerModel(1,"Hello world 10","20-12-2022 14:00",false));
        list.add(new MessengerModel(1,"Hello world 11","20-12-2022 14:00",true));
        list.add(new MessengerModel(1,"Hello world 12","20-12-2022 14:00",true));
        list.add(new MessengerModel(1,"Hello world 13","20-12-2022 14:00",false));
        list.add(new MessengerModel(1,"Hello world 14","20-12-2022 14:00",false));


        mView.showMessages(list);

    }


}
