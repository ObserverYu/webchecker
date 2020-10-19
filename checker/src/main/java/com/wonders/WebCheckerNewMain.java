package com.wonders;

import com.wonders.dao.ItemListNewService;
import com.wonders.spider.CorporatePageNewSpider;
import com.wonders.ui.WebCheckerUi;
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
        WebCheckerUi.build(checkerContext);
        CorporatePageNewSpider corporatePageSpider = CorporatePageNewSpider.build(null, checkerContext);
        ItemListNewService dao = ItemListNewService.build(null, null, null, checkerContext);
        dao.startUpdate();
        corporatePageSpider.start(dao.getList());

    }

}
