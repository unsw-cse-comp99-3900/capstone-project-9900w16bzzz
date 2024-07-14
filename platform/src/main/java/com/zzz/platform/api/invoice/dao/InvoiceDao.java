package com.zzz.platform.api.invoice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzz.platform.api.invoice.domain.InvoiceListVO;
import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface InvoiceDao extends BaseMapper<InvoiceEntity> {

    InvoiceListVO getFileById(BigInteger userId);

    InvoiceEntity getFileByName(String fileName);

    void addFile(InvoiceEntity invoiceEntity);

    List<InvoiceEntity> searchFile(InvoiceEntity invoiceEntity);

    void deleteFile(InvoiceEntity invoiceEntity);

    void updateFileFlag(@Param("invoiceId") BigInteger invoiceId, @Param("fileType") String fileType, @Param("flag") Integer flag);

    void updateValidationFlag(@Param("invoiceId") BigInteger invoiceId, @Param("validationFlag") Integer validationFlag);
}
