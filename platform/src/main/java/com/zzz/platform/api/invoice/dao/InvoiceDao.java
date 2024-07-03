package com.zzz.platform.api.invoice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzz.platform.api.invoice.domain.InvoiceListVO;
import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InvoiceDao extends BaseMapper<InvoiceEntity> {

    InvoiceListVO getFileById(Long userId);

    InvoiceEntity getFileByName(String fileName);

    void addFile(InvoiceEntity invoiceEntity);

    List<InvoiceEntity> searchFile(InvoiceEntity invoiceEntity);

    void deleteFile(InvoiceEntity invoiceEntity);

}
