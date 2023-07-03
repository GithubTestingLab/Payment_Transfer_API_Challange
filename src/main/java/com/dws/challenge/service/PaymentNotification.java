package com.dws.challenge.service;

import org.springframework.stereotype.Service;

@Service
public class PaymentNotification implements NotificationService{
    @Override
    public String sendNotification(Long accountId, String message) {

        System.out.println(message);
        return message;
    }
}
