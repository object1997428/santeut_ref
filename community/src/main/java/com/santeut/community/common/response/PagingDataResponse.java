package com.santeut.community.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagingDataResponse<T> {

    T content;
    private boolean isFirst;
    private boolean isLast;
    private int page;
    private int totalPage;
    private Long totalElements;
    private int size;
    private boolean sorted = false;
    private boolean asc = false;
    private boolean filtered = false;

}
