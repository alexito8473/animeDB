package com.animeplatform.anime_platform.utils;

import com.animeplatform.anime_platform.config.exceptions.ResourceNotFoundException;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class CheckAndThrows {

    static public <T> T checkValueIfNullRuntimeException(T value, Supplier<RuntimeException> exceptionSupplier){
        if(Objects.isNull(value)){
            throw exceptionSupplier.get();
        }
        return value;
    }
    static public <T> T checkValueIfNullResourceNotFoundException(T value, String message){
        return checkValueIfNullRuntimeException(value, () -> new ResourceNotFoundException(message));
    }
    static public <T> T checkOptionalIfNullRuntimeException(Optional<T> value, Supplier<RuntimeException> exceptionSupplier){
        return value.orElseThrow(exceptionSupplier);
    }

    static public <T> T checkOptionalIfNullResourceNotFoundException(Optional<T> value, String message){
        return checkOptionalIfNullRuntimeException(value, () -> new ResourceNotFoundException(message));
    }
}
