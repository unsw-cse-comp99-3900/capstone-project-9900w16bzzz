package com.zzz.platform.api.invoice.service;

import com.zzz.platform.api.invoice.domain.InvoiceJsonVO;
import com.zzz.platform.common.domain.ResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface InvoiceUploadService {

    ResponseDTO<InvoiceJsonVO> upload(Long userId, MultipartFile file);
}
