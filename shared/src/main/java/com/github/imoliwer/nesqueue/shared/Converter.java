package com.github.imoliwer.nesqueue.shared;

import com.github.imoliwer.nesqueue.shared.exception.ConversionFailedException;

/**
 * This interface represents a type of which can be converted to and from said type(s).
 */
public interface Converter<T, S> {
    /**
     * Convert an object of type {@link T} into {@link S}.
     *
     * @param it {@link T} the object to be converted.
     * @return {@link S} converted object, if successful.
     * @throws ConversionFailedException if the conversion failed or was impossible.
     */
    S convertFrom(T it) throws ConversionFailedException;

    /**
     * Convert an object of type {@link S} into {@link T}.
     *
     * @param it {@link S} the object to be converted.
     * @return {@link T} converted object, if successful.
     * @throws ConversionFailedException if the conversion failed or was impossible.
     */
    T convertTo(S it) throws ConversionFailedException;
}