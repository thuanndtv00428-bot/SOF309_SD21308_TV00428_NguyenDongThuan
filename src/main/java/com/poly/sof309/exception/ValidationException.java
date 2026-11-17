package com.poly.sof309.exception;

import java.util.List;

/** Ném ra khi dữ liệu nhập vào vi phạm quy tắc nghiệp vụ. */
public class ValidationException extends RuntimeException {
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        super(String.join("; ", errors));
        this.errors = errors;
    }

    public List<String> getErrors() { return errors; }
}
