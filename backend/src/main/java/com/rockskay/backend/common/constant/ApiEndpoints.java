package com.rockskay.backend.common.constant;

public final class ApiEndpoints {

    private ApiEndpoints() {}

    public static final String API_V1 = "/api/v1";
    public static final String API_V2 = "/api/v2";

    //version-1 Apis
    public static final String AUTH = API_V1 + "/auth";
    public static final String OTP = API_V1 + "/otp";
    public static final String USERS = API_V1 + "/users";
    public static final String JOBS = API_V1 + "/jobs";
    public static final String POSTS = API_V1 + "/posts";

    // Infrastructure
    public static final String API_DOCS = "/v3/api-docs";
    public static final String SWAGGER = "/swagger-ui";
    public static final String ACTUATOR = "/actuator";
}