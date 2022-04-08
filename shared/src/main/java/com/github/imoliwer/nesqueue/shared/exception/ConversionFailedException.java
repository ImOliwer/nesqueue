package com.github.imoliwer.nesqueue.shared.exception;

import static java.lang.String.format;

/**
 * This {@link RuntimeException} represents the failed attempts at converting
 * from and to specific types.
 */
public final class ConversionFailedException extends RuntimeException {
    /**
     * @param converter {@link String} the converter involved in this failure.
     */
    public ConversionFailedException(String converter) {
        super(format(
                "Failed during an attempt at conversion (converter='%s').",
                converter
        ));
    }
}