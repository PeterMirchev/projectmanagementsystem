package com.fixer.controller;

import com.fixer.model.PlanType;
import com.fixer.model.User;
import com.fixer.response.PaymentLinkResponse;
import com.fixer.service.UserService;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String secret;

    private final UserService userService;

    public PaymentController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/{planType}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable(name = "planType") PlanType planType,
                                                                 @RequestHeader("Authorisation") String token) throws Exception {

        User user = userService.findUserProfileByJwt(token);

        int amount = 799 * 100;

        if (planType.equals(PlanType.ANNUALLY)) {
            amount = amount * 12;
            amount = (int) (amount * 0.7);
        }

        RazorpayClient razorpay = new RazorpayClient(apiKey, secret);

        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", amount);
        paymentLinkRequest.put("currency", "INR");

        JSONObject customer = new JSONObject();

        customer.put("name", user.getFullName());
        customer.put("email", user.getEmail());

        paymentLinkRequest.put("customer", customer);

        JSONObject notify = new JSONObject();
        notify.put("email", true);

        paymentLinkRequest.put("notify", notify);
        paymentLinkRequest.put("callback_url", "http://localhost:5173/upgrade_plan/success?planType" + planType);

        PaymentLink paymentLink = razorpay.paymentLink.create(paymentLinkRequest);

        String paymentLinkUrl = paymentLink.get("sort_url");
        String paymentLinkId = paymentLink.get("id");

        PaymentLinkResponse response = new PaymentLinkResponse();
        response.setPaymentLinkUrl(paymentLinkUrl);
        response.setPaymentLinkId(paymentLinkId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
