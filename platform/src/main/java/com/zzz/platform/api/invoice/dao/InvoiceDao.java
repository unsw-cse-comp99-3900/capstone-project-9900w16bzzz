package com.zzz.platform.api.invoice.dao;

import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InvoiceDao {

    @Select("SELECT * FROM t_file WHERE user_id = #{userId}")
    InvoiceEntity getFileById(Long userId);

    @Select("SELECT * FROM t_file WHERE file_name = #{fileName}")
    InvoiceEntity getFileByName(String fileName);

    @Insert("INSERT INTO t_file (file_name, user_id, file_content, file_validation) VALUES (#{fileName}, #{userId}, #{fileContent}, 0)")
    void addFile(InvoiceEntity invoiceEntity);
}
