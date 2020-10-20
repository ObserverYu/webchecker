package com.wonders;

import com.wonders.dao.service.ApplyListService;
import com.wonders.spider.ApplyListPageSpider;
import com.wonders.ui.WebCheckerUiImp;
import lombok.Data;

import java.io.IOException;

/**
 * @author YuChen
 * @date 2020/10/12 9:43
 **/

@Data
public class WebCheckerNewMain {

    public static void main(String[] args) throws IOException {
        WebCheckerContext checkerContext = new WebCheckerContext();
        WebCheckerUiImp.build(checkerContext);
        ApplyListPageSpider corporatePageSpider = ApplyListPageSpider.build(null, checkerContext);
        ApplyListService dao = ApplyListService.build(null, null, null, checkerContext);
        dao.startUpdate();
        corporatePageSpider.start(dao.getList());

    }

}
