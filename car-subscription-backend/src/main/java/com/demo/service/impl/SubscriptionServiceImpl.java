package com.demo.service.impl;

import com.demo.model.Subscription;
import com.demo.repository.SubscriptionRepository;
import com.demo.service.SubscriptionService;

import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository repo;

    public SubscriptionServiceImpl(SubscriptionRepository repo) {
        this.repo = repo;
    }

    @Override
    public Subscription saveOrder(Subscription s) {
        return repo.save(s);
    }
}
