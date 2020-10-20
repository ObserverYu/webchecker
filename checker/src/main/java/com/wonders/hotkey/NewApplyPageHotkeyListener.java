package com.wonders.hotkey;

import com.melloware.jintellitype.HotkeyListener;
import com.ruiyun.jvppeteer.core.page.Page;
import com.wonders.WebCheckerContext;
import com.wonders.dao.entity.ApplyList;
import lombok.Data;

import java.util.concurrent.CountDownLatch;

/**
 * @author ObserverYu
 * @date 2020/10/15 13:01
 **/

@Data
public class NewApplyPageHotkeyListener implements HotkeyListener {

    private WebCheckerContext webCheckerContext;
    private CountDownLatch countDownLatch;
    private ApplyList applyList;
    private Page applyPage;



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
        if(this.applyList != null){
            Page nowApplyPage = webCheckerContext.getNowApplyPage();
            String url = nowApplyPage.mainFrame().url();
            System.out.println(url);
            applyList.setResult(result);
            this.applyList = null;
        }
        if(countDownLatch != null){
            countDownLatch.countDown();
            this.countDownLatch = null;
        }
    }
}
