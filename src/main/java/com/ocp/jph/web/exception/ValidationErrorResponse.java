package com.ocp.jph.web.exception;

import java.util.List;

public record ValidationErrorResponse(List<ValidationError> errors) {
}
