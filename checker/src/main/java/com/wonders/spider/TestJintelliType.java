package com.wonders.spider;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

/**
 * 
 *  
 * @author ObserverYu
 * @date 2020/10/15 11:38
 **/
 
public class TestJintelliType {

    public static void main(String[] args)throws Exception{
//        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
//        InputStream resourceAsStream = systemClassLoader.getResourceAsStream("com/melloware/jintellitype/windows/JIntellitype64.dll");
        //添加快捷键
        JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_CONTROL, (int) '1');//win+A为快捷键
        JIntellitype.getInstance().registerHotKey(2, JIntellitype.MOD_ALT + JIntellitype.MOD_SHIFT, (int) 'B');//alt+shift+B为快捷键

        //添加监听
        JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {//实现HotkeyListener
            public void onHotKey(int aIdentifier) {
                switch (aIdentifier){//
                    case  1:
                        System.out.println("我按到了c + 1");break;
                    case  2:
                        System.out.println("我按到了2");break;
                }
            }
        });
        //JIntellitype.getInstance().cleanUp();//清除快捷键
    }
}
