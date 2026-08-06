package com.ocp.jph.exception;

import java.util.List;

public record ValidationErrorResponse(List<ValidationError> errors) {
}
