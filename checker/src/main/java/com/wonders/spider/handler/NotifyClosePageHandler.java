package com.wonders.spider.handler;

import com.ruiyun.jvppeteer.events.EventHandler;

import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 *  
 * @author ObserverYu
 * @date 2020/10/15 11:19
 **/
 
public class NotifyClosePageHandler implements EventHandler<Object> {

    private ReentrantLock lock;
    @Override
    public void onEvent(Object event) {

    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        while (true){
            String s = scanner.nextLine();
            System.out.println(s);
        }
    }
}
