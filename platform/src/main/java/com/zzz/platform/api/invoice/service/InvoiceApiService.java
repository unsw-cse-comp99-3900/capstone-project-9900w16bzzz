package com.zzz.platform.api.invoice.service;

import com.zzz.platform.api.invoice.domain.InvoiceJsonVO;
import com.zzz.platform.api.invoice.domain.ValidateResultVO;
import com.zzz.platform.api.invoice.domain.api.EssInvoiceValidateForm;
import com.zzz.platform.api.invoice.domain.api.UpbrainExtractorForm;
import com.zzz.platform.common.domain.ResponseDTO;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/1
 */
public interface InvoiceApiService {

    /**
     * convert pdf file to json file
     * @param upbrainExtractorForm request body
     * @return json file content
     */
    ResponseDTO<InvoiceJsonVO> convertPdfToJson(UpbrainExtractorForm upbrainExtractorForm);

    /**
     * validate invoice using ess service
     * @param essInvoiceValidateForm request parameters
     * @return validation report
     */
    ResponseDTO<ValidateResultVO> essValidateInvoice(EssInvoiceValidateForm essInvoiceValidateForm);
}
