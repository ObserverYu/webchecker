package com.wonders;

import com.wonders.ui.WebCheckerUi;
import lombok.Data;

/**
 * @author YuChen
 * @date 2020/10/12 9:43
 **/

@Data
public class WebCheckerRunner {

    public static void main(String[] args) {
        WebCheckerContext checkerContext = new WebCheckerContext();
        WebCheckerUi.build(checkerContext);
    }

}
