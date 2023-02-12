package org.tms.client;

import org.tms.core.Message;

public class Main {
    public static void main(String[] args) {
        Message someMessage = new Message("WOW THIS WORKS");
        System.out.println(someMessage.getMsg());
    }
}