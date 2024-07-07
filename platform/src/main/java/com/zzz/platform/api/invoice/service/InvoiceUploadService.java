package com.zzz.platform.api.invoice.service;

import com.zzz.platform.api.invoice.domain.InvoiceJsonVO;
import com.zzz.platform.common.domain.ResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;

public interface InvoiceUploadService {

    ResponseDTO<InvoiceJsonVO> upload(BigInteger userId, MultipartFile file);
}
