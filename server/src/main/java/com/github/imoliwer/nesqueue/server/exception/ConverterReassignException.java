package com.github.imoliwer.nesqueue.server.exception;

import static java.lang.String.format;

/**
 * This {@link RuntimeException} represents the attempt at reassigning a converter
 * in f.e {@link com.github.imoliwer.nesqueue.server.NesQueueServer}.
 */
public final class ConverterReassignException extends RuntimeException {
    /**
     * @param converter {@link String} the converter involved in an attempt at reassignment.
     */
    public ConverterReassignException(String converter) {
        super(format(
                "Converter '%s' cannot be reassigned.",
                converter
        ));
    }
}