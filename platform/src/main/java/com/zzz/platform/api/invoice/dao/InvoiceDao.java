package com.zzz.platform.api.invoice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzz.platform.api.invoice.domain.ValidateResultVO;
import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author: zhaoyue zhang
 * @version: v1.0.0
 * @date: 2024/6/24
 */
@Mapper
@Component
public interface InvoiceDao extends BaseMapper<InvoiceEntity> {



}
