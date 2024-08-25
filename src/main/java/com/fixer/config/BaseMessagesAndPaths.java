package com.fixer.config;

import lombok.Data;

@Data
public class BaseMessagesAndPaths {

    public static String AUTHORISATION = "Authorization";

    public static final String HOST_3000 = "http://localhost:3000/";
    public static final String HOST_5173 = "http://localhost:5173/";
    public static final String HOST_4200 = "http://localhost:4200/";

    public static final String INVALID_TOKEN = "Invalid token!";
    public static final String EMAIL = "email";
    public static final String AUTHORITIES = "authorities";

    //AuthController
    public static final String USER_ALREADY_EXIST = "{%s} already exist within another account.";
    public static final String SIGNUP_SUCCESS = "Successful signup.";
    public static final String SIGNING_SUCCESS = "Successful signing.";

    public static final String INVALID_USERNAME = "Invalid username.";
    public static final String INVALID_PASSWORD = "Invalid password.";

    //CommentController
    public static final String COMMENT_CONTROLLER_BASE_PATH = "/api/comments";
    public static final String COMMENT_DELETED = "Comment deleted successfully.";
    public static final String COMMENT_ID = "{commentId}";
    public static final String ISSUE_ID = "/{issueId}";

    //IssueController
    public static final String ISSUE_CONTROLLER_BASE_PATH = "/api/issues";
    public static final String PROJECT_PROJECT_ID = "/project/{projectId}";
    public static final String DELETED_ISSUE = "Issue deleted successfully.";
    public static final String SEARCH = "/search";
    public static final String ISSUE_ASSIGNEE_USER_ID = "{issueId}/assignee/{userId}";
    public static final String ISSUE_STATUS = "/{issueId}/status/{status}";

    //MessageController
    public static final String MESSAGE_CONTROLLER_BASE_PATH = "/api/messages";
    public static final String CHAT_NOT_FOUND = "Chat not found.";
    public static final String CHAT_PROJECT_ID = "/chat/{projectId}";

    //PaymentController
    public static final String PAYMENT_CONTROLLER_BASE_PATH = "/api/payment";
    public static final String CALLBACK_URL = "callback_url";
    public static final String CALLBACK_URL_TWO = "http://localhost:5173/upgrade_plan/success?planType";
    public static final String SORT_URL = "sort_url";
}
