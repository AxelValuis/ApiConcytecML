package org.concytec.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ErrorDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime registrationDate;
    private final String title;
    private final String message;;
    private final HttpStatus httpStatus;
    private final String path;

    public ErrorDTO(LocalDateTime registrationDate, String title, String message, HttpStatus httpStatus, String path) {
        this.registrationDate = registrationDate;
        this.title = title;
        this.message = message;
        this.httpStatus = httpStatus;
        this.path = path;
    }

    public static ErrorDTO from(GenericClientException exception, String path){
        return new ErrorDTO(exception.getRegistrationDate(), exception.getTitle(), exception.getMessage(), exception.getHttpStatus(), path);
    }
}
