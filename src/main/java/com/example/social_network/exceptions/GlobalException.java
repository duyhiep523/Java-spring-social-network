package com.example.social_network.exceptions;

import com.example.social_network.response.Error;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;


@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Error> handleRuntimeException(RuntimeException ex) {
        Error errorResponse = new Error(ex.getMessage(), "500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<?> handleDateTimeParseException(DateTimeParseException ex) {
        Error errorResponse = new Error(
                "Ngày sinh không đúng định dạng. Định dạng đúng là yyyy-MM-dd",
                "BAD_REQUEST"
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Error> handleBadRequestException(IllegalArgumentException ex) {
        Error error = new Error("Invalid request: " + ex.getMessage(), "BAD_REQUEST");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Error> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Error error = new Error("Không tìm thấy tài nguyên " + ex.getMessage(), "NOT_FOUND");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<Error> handleDatabaseException(DatabaseException ex) {
        Error error = new Error("Database error: " + ex.getMessage(), "DATABASE_ERROR");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Error> handleInvalidFormatException(InvalidFormatException ex) {
        String errorMessage = "Giá trị của enum không hợp lệ: " + ex.getValue();
        Error error = new Error(errorMessage, "400");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Xử lý lỗi JsonMappingException khi không thể giải mã JSON thành đối tượng
    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<Error> handleJsonMappingException(JsonMappingException ex) {
        String errorMessage = "Định dạng đầu vào không hợp lệ: " + ex.getOriginalMessage();
        Error error = new Error(errorMessage, "400");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Xử lý lỗi khi không thể đọc dữ liệu JSON (ví dụ: thông tin không hợp lệ)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            if (((InvalidFormatException) cause).getTargetType() == java.time.LocalDate.class) {
                return handleDateTimeParseException((DateTimeParseException) cause.getCause());
            }
            return handleInvalidFormatException((InvalidFormatException) cause);
        }
        String errorMessage = "Yêu cầu JSON không đúng định dạng: " + ex.getMessage();
        Error error = new Error(errorMessage, "400");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }




    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<Error> handleFileUploadException(FileUploadException ex) {
        Error error = new Error("Image upload error: " + ex.getMessage(), "BAD_REQUEST");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleGeneralException(Exception ex) {
        // Tạo một đối tượng lỗi với thông báo mặc định và mã lỗi nội bộ
        Error error = new Error("An unexpected error occurred: " + ex.getMessage(), "INTERNAL_SERVER_ERROR");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
