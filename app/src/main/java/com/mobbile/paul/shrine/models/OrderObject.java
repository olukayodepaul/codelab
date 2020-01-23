package com.mobbile.paul.shrine.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class OrderObject implements Serializable {


    public String No;

    public String Client;

    @ServerTimestamp
    public Date DateAdded = new Date();

    public String Status;

    public Map<String, Object> Messages;

    public String OrderType;

    public OrderObject() {
    }

    public OrderObject(String No, String Client, String Status, Map<String, Object> Messages, String OrderType) {

        this.No = No;
        this.Client = Client;
        this.Status = Status;
        this.Messages = Messages;
        this.OrderType = OrderType;
    }
}
