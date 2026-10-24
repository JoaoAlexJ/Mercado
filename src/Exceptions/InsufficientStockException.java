package Exceptions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class InsufficientStockException extends RuntimeException {

    private final List<String> errors;

    public InsufficientStockException(List<String> errors) {
        super("Stock insufficient for one or more products.");

        this.errors = errors;
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
