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
import com.zzz.platform.common.enumeration.FileType;
import com.zzz.platform.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;

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

    public InvoiceEntity findById(BigInteger invoiceId) {
        return invoiceDao.selectById(invoiceId);
    }

    public String getFileNameById(BigInteger invoiceId) {
        return findById(invoiceId).getFileName();
    }

    @Override
    public void updateFileFlag(BigInteger invoiceId, FileType type, FileStatusFlag flag) {
        String fileType = type.getDesc() + "_" + InvoiceDbColumn.FLAG.getVal();
        invoiceDao.updateFileFlag(invoiceId, fileType, flag.getVal());
    }

    @Override
    public void updateValidationFlag(BigInteger invoiceId, ValidationFlag flag) {
        invoiceDao.updateValidationFlag(invoiceId, flag.getValue());
    }


}
