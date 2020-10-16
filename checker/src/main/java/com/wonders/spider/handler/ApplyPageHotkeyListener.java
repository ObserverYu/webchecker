package com.wonders.spider.handler;

import com.melloware.jintellitype.HotkeyListener;
import com.wonders.spider.entity.ItemList;

import java.util.concurrent.CountDownLatch;

/**
 * @author ObserverYu
 * @date 2020/10/15 13:01
 **/

public class ApplyPageHotkeyListener implements HotkeyListener {

    CountDownLatch countDownLatch;
    ItemList itemList;

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public ItemList getItemList() {
        return itemList;
    }

    public void setItemList(ItemList itemList) {
        this.itemList = itemList;
    }


    /**
     * Event fired when a WM_HOTKEY message is received that was initiated
     * by this application.
     * <p>
     *
     * @param identifier the unique Identifer the Hotkey was assigned
     */
    @Override
    public void onHotKey(int identifier) {
        String result = "未处理";
        switch (identifier) {
            case 1:
                System.out.println("我按到了ctrl + 1");
                result = "正常";
                break;
            case 2:
                System.out.println("我按到了ctrl + 2");
                result = "异常";
                break;
            default:
                break;
        }
        if(this.itemList != null){
            itemList.setResult(result);
            this.itemList = null;
        }
        this.itemList = null;
        if(countDownLatch != null){
            countDownLatch.countDown();
            this.countDownLatch = null;
        }
    }
}
