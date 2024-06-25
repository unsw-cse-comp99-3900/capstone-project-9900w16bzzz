package com.zzz.platform.api.invoice.service;

import com.zzz.platform.common.domain.ResponseDTO;

public interface DeleteFileService {
    ResponseDTO<String> deleteFile(Integer fileId, Long userId);
}
