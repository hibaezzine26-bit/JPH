package com.ocp.jph.web.exception;

public record ValidationError(String field, String message) {
}
