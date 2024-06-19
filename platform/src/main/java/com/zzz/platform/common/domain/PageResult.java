package com.zzz.platform.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
public class PageResult<T> {

    /**
     * page num
     */
    @Schema(description = "page num")
    private Long pageNum;

    /**
     * page size
     */
    @Schema(description = "page size")
    private Long pageSize;

    /**
     * total count
     */
    @Schema(description = "total count")
    private Long total;

    /**
     * total pages
     */
    @Schema(description = "total pages")
    private Long pages;

    /**
     * result set
     */
    @Schema(description = "result set")
    private List<T> list;

    @Schema(description = "is empty")
    private Boolean emptyFlag;
}
