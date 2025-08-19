package com.santeut.community.common.response;


import com.santeut.community.common.response.BasicResponse;
import com.santeut.community.common.response.ErrorResponse;
import com.santeut.community.common.response.PagingResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static ResponseEntity<BasicResponse> buildBasicResponse(HttpStatus status, Object data) {
        BasicResponse basicResponse = new BasicResponse(status.value(), data);
        return new ResponseEntity<>(basicResponse, status);
    }

    public static ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String errorName, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), errorName, message);
        return new ResponseEntity<>(errorResponse, status);
    }

    public static ResponseEntity<PagingResponse> buildPagingResponse(
            HttpStatus status, PagingResponse pagingResponse) {
        return new ResponseEntity<>(pagingResponse, status);
    }

    public static ResponseEntity<PagingResponse> buildPagingResponse(
            HttpStatus status,
            Page<?> page,
            boolean sorted,
            boolean asc,
            boolean filtered) {

        PagingResponse pagingResponse = new PagingResponse(
                status.value(),
                page,
                sorted,
                asc,
                filtered
        );
        return new ResponseEntity<>(pagingResponse, status);
    }
}