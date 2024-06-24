package com.zzz.platform.api.invoice.service;

import com.zzz.platform.api.invoice.domain.UploadFileForm;
import com.zzz.platform.common.domain.ResponseDTO;

public interface UploadFileService {
    ResponseDTO<String> uploadFile(UploadFileForm uploadFileForm, Long userId);
}
