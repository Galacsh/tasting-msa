package com.galacsh;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

/**
 * <p>
 *     A generic response object that wraps the data to be returned to the client.
 *     Note that this class is for successful responses only.
 *     For error responses, use {@link org.springframework.http.ProblemDetail}.
 * </p>
 *
 * <p>
 *     Also, there is a builder method {@link #forStatus(HttpStatusCode)}
 *     that can be used to create a {@link ResponseEntity}.
 * </p>
 *
 * <p>
 *     Example usage:
 *     <pre>{@code
 *         Response<?> response = Response.ok("Hello, World!");
 *         ResponseEntity<?> responseEntity = Response.forStatus(HttpStatusCode.NO_CONTENT).build();
 *     }</pre>
 *
 *
 * @param <T> The type of the data to be returned.
 */
public class Response<T> {
    private final T data;

    private Response(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public static <T> Response<T> ok(T data) {
        return new Response<>(data);
    }

    public static <T> ResponseEntityBuilder<T> forStatus(HttpStatusCode status) {
        Assert.isTrue(!status.isError(), "Think of using ProblemDetail for error status");
        return new ResponseEntityBuilder<T>().status(status);
    }

    public static class ResponseEntityBuilder<T> {
        private HttpStatusCode status;
        private HttpHeaders headers;
        private Response<T> data;

        public ResponseEntityBuilder<T> status(HttpStatusCode status) {
            this.status = status;
            return this;
        }

        public ResponseEntityBuilder<T> headers(HttpHeaders headers) {
            this.headers = headers;
            return this;
        }

        public ResponseEntityBuilder<T> data(T data) {
            this.data = new Response<>(data);
            return this;
        }

        public ResponseEntity<Response<T>> build() {
            return new ResponseEntity<>(data, headers, status);
        }
    }
}
