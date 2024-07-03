package com.zzz.platform.api.invoice.service;

import com.zzz.platform.api.invoice.domain.InvoiceDeleteForm;
import com.zzz.platform.api.invoice.domain.InvoiceListVO;
import com.zzz.platform.api.invoice.domain.InvoiceQueryForm;
import com.zzz.platform.common.domain.PageResult;
import com.zzz.platform.common.domain.ResponseDTO;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/2
 */
public interface InvoiceDbService {

    ResponseDTO<PageResult<InvoiceListVO>> list(InvoiceQueryForm invoiceQueryForm);

    ResponseDTO<String> delete(InvoiceDeleteForm deleteForm);
}
