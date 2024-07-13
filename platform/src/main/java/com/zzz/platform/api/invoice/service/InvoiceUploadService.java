package com.zzz.platform.api.invoice.service;

import com.zzz.platform.api.invoice.domain.InvoiceUploadResultVO;
import com.zzz.platform.common.domain.ResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;

public interface InvoiceUploadService {

    ResponseDTO<InvoiceUploadResultVO> upload(BigInteger userId, MultipartFile file);
}
