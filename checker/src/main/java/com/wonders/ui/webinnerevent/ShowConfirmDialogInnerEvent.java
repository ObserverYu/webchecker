package com.wonders.ui.webinnerevent;

/**
 * 爬虫代码触发的某些内部事件
 *
 * @author ObserverYu
 * @date 2020/10/19 16:16
 **/
public class ShowConfirmDialogInnerEvent implements InnerEvent<String> {

    private String message;
    private Integer code;
    private String data;

    public ShowConfirmDialogInnerEvent(String data) {
        this.message = "修改Info";
        this.code = 0;
        this.data = data;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getData() {
        return data;
    }
}
