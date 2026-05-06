package com.readflow.api.exception.global;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private int status;
    private String error;
    private Map<String, String> errors;

    public ErrorResponse(int status, String error, Map<String, String> errors) {
        this.status = status;
        this.error = error;
        this.errors = errors;
    }

    public int getStatus() { return status; }
    public String getError() { return error; }
    public Map<String, String> getErrors() { return errors; }
}
