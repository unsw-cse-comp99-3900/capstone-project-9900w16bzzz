package com.zzz.platform.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Data
public class PageParam {

    @Schema(description = "page num (not null)", example = "1")
    @NotNull(message = "The paging parameter cannot be null")
    private Long pageNum;

    @Schema(description = "page size (not null)", example = "10")
    @NotNull(message = "The page size cannot be null")
    @Max(value = 500, message = "maximum 500")
    private Long pageSize;

    @Schema(description = "search count")
    protected Boolean searchCount;

    @Schema(description = "Sorted item Collection")
    @Size(max = 10, message = "maximum 10")
    @Valid
    private List<SortItem> sortItemList;

    /**
     * sorting DTO class
     */
    @Data
    public static class SortItem {

        @Schema(description = "true asc | false desc")
        @NotNull(message = "Sorting rules cannot be empty")
        private Boolean isAsc;

        @Schema(description = "Sort Fields")
        @NotBlank(message = "The sort field cannot be empty")
        @Length(max = 30, message = "maximum 30")
        private String column;
    }
}
