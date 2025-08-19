package com.santeut.community.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class PagingResponse<T> {
    private int status;
    private PagingDataResponse<T> data;

    public PagingResponse(int status, Page<?> page, boolean sorted, boolean asc, boolean filtered) {
        this.status = status;
        this.data = new PagingDataResponse(page.getContent(), page.isFirst(), page.isLast(),
                page.getNumber(), page.getTotalPages(), page.getTotalElements(), page.getSize(), sorted,
                asc, filtered);
    }
}
