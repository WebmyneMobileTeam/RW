package com.webmyne.rightway.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nirav on 10-11-2014.
 */
public class ResponseMessage {

    @SerializedName("Response")
    public String Response;

    public ResponseMessage() {
    }

    public ResponseMessage(String response) {
        Response = response;
    }
}
