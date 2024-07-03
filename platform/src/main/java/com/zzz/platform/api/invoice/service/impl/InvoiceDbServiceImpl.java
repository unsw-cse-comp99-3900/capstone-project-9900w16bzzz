package com.zzz.platform.api.invoice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzz.platform.api.invoice.dao.InvoiceDao;
import com.zzz.platform.api.invoice.dao.InvoiceFileDao;
import com.zzz.platform.api.invoice.domain.InvoiceDeleteForm;
import com.zzz.platform.api.invoice.domain.InvoiceListVO;
import com.zzz.platform.api.invoice.domain.InvoiceQueryForm;
import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import com.zzz.platform.api.invoice.entity.InvoiceFileEntity;
import com.zzz.platform.api.invoice.service.InvoiceDbService;
import com.zzz.platform.common.code.InvoiceErrorCode;
import com.zzz.platform.common.domain.PageResult;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.utils.PageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/2
 */
@Service
public class InvoiceDbServiceImpl implements InvoiceDbService {

    @Resource
    private InvoiceDao invoiceDao;
    @Autowired
    private InvoiceFileDao invoiceFileDao;

    @Override
    public ResponseDTO<PageResult<InvoiceListVO>> list(InvoiceQueryForm queryForm) {
        Page<InvoiceEntity> entityPage = invoiceDao.selectPage(
                new Page<>(queryForm.getPageNum(), queryForm.getPageSize()),
                new QueryWrapper<InvoiceEntity>().eq(InvoiceDbColumn.USER_ID.getVal(), queryForm.getUserId())
        );

        PageResult<InvoiceListVO> pageResult = PageUtil.convert2PageResult(entityPage, InvoiceListVO.class);
        if (pageResult.getEmptyFlag()) {
            return ResponseDTO.error(InvoiceErrorCode.INVOICE_LIST_QUERY_FAILED);
        }
        return ResponseDTO.ok(pageResult);
    }

    @Override
    public ResponseDTO<String> delete(InvoiceDeleteForm deleteForm) {
        invoiceDao.deleteById(deleteForm.getInvoiceId());
        invoiceFileDao.delete(
                new QueryWrapper<InvoiceFileEntity>().eq(InvoiceDbColumn.INVOICE_ID.getVal(), deleteForm.getInvoiceId())
        );
        return ResponseDTO.ok();
    }

    @Getter
    @AllArgsConstructor
    enum InvoiceDbColumn {
        USER_ID("user_id"),
        INVOICE_ID("invoice_id"),
        FILE_NAME("file_name"),
        CREATE_TIME("create_time"),
        UPDATE_TIME("update_time"),
        ;
        private final String val;
    }
}
