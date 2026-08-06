package com.ocp.jph.exception;

public record ValidationError(String field, String message) {
}
