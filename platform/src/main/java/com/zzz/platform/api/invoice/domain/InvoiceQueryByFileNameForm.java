package com.zzz.platform.api.invoice.domain;

import com.zzz.platform.common.domain.PageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceQueryByFileNameForm extends PageParam {

    @NotNull
    private String fileName;

    @NotNull
    private BigInteger userId;
}
