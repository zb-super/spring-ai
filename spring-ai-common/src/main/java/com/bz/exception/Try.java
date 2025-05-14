package com.bz.exception;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Description
 * Copyright © bz 版权所有
 *
 * @author：bz
 * @date： 2025/5/13
 */
public interface Try<T> extends Serializable {

    T get();

    Throwable getCause();

    boolean isFailure();

    boolean isSuccess();

    static <T> Try<T> of(Supplier<T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        try {
            return new Success<>(supplier.get());
        }catch (Throwable t){
            return new Failure<>(t);
        }
    }

    static <T> Try<T> success(T value) {
        return new Success<>(value);
    }

    static <T> Try<T> failure(Throwable exception) {
        return new Failure<>(exception);
    }

    default Try<T> onFailure(Consumer<Throwable> consumer) {
        Objects.requireNonNull(consumer, "consumer is null");
        if (isFailure()) {
            consumer.accept(getCause());
        }
        return this;
    }

    default Try<T> onSuccess(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer, "consumer is null");
        if (isSuccess()) {
            consumer.accept(get());
        }
        return this;
    }

    default Try<T> andFinally(Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        try {
            runnable.run();
            return this;
        }catch (Throwable t){
            return new Failure<>(t);
        }
    }

    default T getOrElseGet(Function<? super Throwable, ? extends T> other) {
        Objects.requireNonNull(other, "other is null");
        if (isFailure()) {
            return other.apply(getCause());
        } else {
            return get();
        }
    }

    default <X> X fold(Function<? super Throwable, ? extends X> ifFail, Function<? super T, ? extends X> f) {
        if (isFailure()) {
            return ifFail.apply(getCause());
        } else {
            return f.apply(get());
        }
    }

    default Try<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (isSuccess()) {
            action.accept(get());
        }
        return this;
    }

    default <X extends Throwable> Try<T> recover(Class<X> exceptionType, Function<? super X, ? extends T> f) {
        Objects.requireNonNull(exceptionType, "exceptionType is null");
        Objects.requireNonNull(f, "f is null");
        if (isFailure()) {
            final Throwable cause = getCause();
            if (exceptionType.isAssignableFrom(cause.getClass())) {
                return Try.of(() -> f.apply((X) cause));
            }
        }
        return this;
    }

    default <U> U transform(Function<? super Try<T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    final class Success<T> implements Try<T>, Serializable {

        private final T value;

        public Success(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public Throwable getCause() {
            throw new UnsupportedOperationException("getCause on Success");
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }
    }

    final class Failure<T> implements Try<T>, Serializable {

        private final Throwable value;

        public Failure(Throwable cause) {
            this.value = cause;
        }

        @Override
        public T get() {
            return sneakyThrow(value);
        }

        @Override
        public Throwable getCause() {
            return value;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }
    }

    static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }
}
