package com.fixer.service.impl;

import com.fixer.model.PlanType;
import com.fixer.model.Subscription;
import com.fixer.model.User;
import com.fixer.repository.SubscriptionRepository;
import com.fixer.service.SubscriptionService;
import com.fixer.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {


        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Subscription createSubscription(User user) {

        Subscription persistedSubscription = mapPersistedSubscription(user);

        return subscriptionRepository.save(persistedSubscription);
    }

    @Override
    public Subscription getUserSubscription(Long userId) {

        Subscription subscription = subscriptionRepository.findByUserId(userId);

        if (!isValid(subscription)) {

            subscription.setPlanType(PlanType.FREE);
            subscription.setSubscriptionStartDate(LocalDate.now());
            subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
        }

        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription upgradeSubscription(Long userId, PlanType planType) {

        Subscription subscription = subscriptionRepository.findByUserId(userId);

        subscription.setPlanType(planType);
        subscription.setSubscriptionStartDate(LocalDate.now());

        if (subscription.getPlanType().equals(PlanType.MONTHLY)) {
            subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(1));
        } else {
            subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
        }

        return subscriptionRepository.save(subscription);
    }

    @Override
    public boolean isValid(Subscription subscription) {

        if (subscription.getPlanType().equals(PlanType.FREE)) {
            return true;
        }

        LocalDate endDate = subscription.getSubscriptionEndDate();
        LocalDate currentDate = LocalDate.now();

        return endDate.isAfter(currentDate) || endDate.isEqual(currentDate);
    }

    protected static Subscription mapPersistedSubscription(User user) {
        Subscription subscription = new Subscription();

        subscription.setUser(user);
        subscription.setSubscriptionStartDate(LocalDate.now());
        subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
        subscription.setValid(true);
        subscription.setPlanType(PlanType.FREE);
        return subscription;
    }
}
