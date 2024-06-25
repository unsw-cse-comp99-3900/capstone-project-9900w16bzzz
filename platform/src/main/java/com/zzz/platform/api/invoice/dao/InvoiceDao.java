package com.zzz.platform.api.invoice.dao;

import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InvoiceDao {

    InvoiceEntity getFileById(Long userId);

    InvoiceEntity getFileByName(String fileName);

    void addFile(InvoiceEntity invoiceEntity);

    List<InvoiceEntity> searchFile(InvoiceEntity invoiceEntity);

    void deleteFile(InvoiceEntity invoiceEntity);

}
