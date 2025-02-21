package com.ruifenglb.www.bean;

public class GetUrlEvent {
    public String url;
    public String title;

    public GetUrlEvent(String url,String title) {
        this.url = url;
        this.title = title;
    }
}
