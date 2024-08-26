package org.concytec.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class GenericClientException extends RuntimeException {
    private final String title;
    private final HttpStatus httpStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime registrationDate;

    public GenericClientException(String title, String message, HttpStatus httpStatus) {
        super(message);
        this.title = title;
        this.httpStatus = httpStatus;
        this.registrationDate = LocalDateTime.now();
    }

    public static GenericClientException create(String title, String message, HttpStatus httpStatus) {
        return new GenericClientException(title, message, httpStatus);
    }
}
