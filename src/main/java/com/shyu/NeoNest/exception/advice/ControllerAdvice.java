package com.shyu.NeoNest.exception.advice;

import com.shyu.NeoNest.dto.response.ExResponseDto;
import com.shyu.NeoNest.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(DuplicateReviewException.class)
    public ResponseEntity<ExResponseDto> DuplicateReviewExHandler(DuplicateReviewException e) {
        log.error("DuplicateReviewException : {}", e.getMessage(), e);
        ExResponseDto exDto = new ExResponseDto("fail", e.getMessage());

        return new ResponseEntity<>(exDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicationMemberNameException.class)
    public ResponseEntity<ExResponseDto> DuplicationMemberNameExHandler(DuplicationMemberNameException e) {
        log.error("DuplicationMemberNameException : {}", e.getMessage(), e);
        ExResponseDto exDto = new ExResponseDto("fail", e.getMessage());

        return new ResponseEntity<>(exDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidOrderException.class)
    public ResponseEntity<ExResponseDto> InvalidOrderExHandler(InvalidOrderException e) {
        log.error("InvalidOrderException : {}", e.getMessage(), e);
        ExResponseDto exDto = new ExResponseDto("fail", e.getMessage());

        return new ResponseEntity<>(exDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ExResponseDto> memberNotFoundExHandler(MemberNotFoundException e) {
        log.error("MemberNotFoundException : {}", e.getMessage(), e);
        ExResponseDto exDto = new ExResponseDto("fail", e.getMessage());

        return new ResponseEntity<>(exDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<ExResponseDto> NotEnoughStockExHandler(NotEnoughStockException e) {
        log.error("NotEnoughStockException : {}", e.getMessage(), e);
        ExResponseDto exDto = new ExResponseDto("fail", e.getMessage());

        return new ResponseEntity<>(exDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExResponseDto> ProductNotFoundExHandler(ProductNotFoundException e) {
        log.error("ProductNotFoundException : {}", e.getMessage(), e);
        ExResponseDto exDto = new ExResponseDto("fail", e.getMessage());

        return new ResponseEntity<>(exDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotInOrderException.class)
    public ResponseEntity<ExResponseDto> ProductNotInOrderExHandler(ProductNotInOrderException e) {
        log.error("ProductNotInOrderException : {}", e.getMessage(), e);
        ExResponseDto exDto = new ExResponseDto("fail", e.getMessage());

        return new ResponseEntity<>(exDto, HttpStatus.BAD_REQUEST);
    }

}
