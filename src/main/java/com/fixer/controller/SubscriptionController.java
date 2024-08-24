package com.fixer.controller;

import com.fixer.model.PlanType;
import com.fixer.model.Subscription;
import com.fixer.model.User;
import com.fixer.service.SubscriptionService;
import com.fixer.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;

    public SubscriptionController(SubscriptionService subscriptionService,
                                  UserService userService) {

        this.subscriptionService = subscriptionService;
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<Subscription> getUserSubscription(@RequestHeader("Authorisation") String token) throws Exception {

        User user = userService.findUserProfileByJwt(token);

        Subscription subscription = subscriptionService.getUserSubscription(user.getId());

        return ResponseEntity.ok(subscription);
    }

    @PatchMapping("/upgrade")
    public ResponseEntity<Subscription> upgradeSubscription(@RequestHeader("Authorisation") String token,
                                                            @RequestParam PlanType planType) throws Exception {

        User user = userService.findUserProfileByJwt(token);

        Subscription subscription = subscriptionService.upgradeSubscription(user.getId(), planType);

        return ResponseEntity.ok(subscription);
    }
}
