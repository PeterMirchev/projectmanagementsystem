package com.fixer.service;

import com.fixer.model.PlanType;
import com.fixer.model.Subscription;
import com.fixer.model.User;

public interface SubscriptionService {

    Subscription createSubscription(User user);

    Subscription getUserSubscription(Long userId);

    Subscription upgradeSubscription(Long userId, PlanType planType);

    boolean isValid(Subscription subscription);
}
