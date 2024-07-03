package com.zzz.platform.utils;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.zzz.platform.common.domain.PageParam;
import com.zzz.platform.common.domain.PageResult;
import com.zzz.platform.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/2
 */
@Slf4j
public class PageUtil {

    /**
     * Conversion to query parameters
     */
    public static Page<?> convert2PageQuery(PageParam pageParam) {
        Page<?> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());

        List<PageParam.SortItem> sortItemList = pageParam.getSortItemList();
        if (CollectionUtils.isEmpty(sortItemList)) {
            return page;
        }

        // Setting up sorted fields and detecting if they contain sql injection
        List<OrderItem> orderItemList = new ArrayList<>();
        for (PageParam.SortItem sortItem : sortItemList) {

            if (StringUtils.isEmpty(sortItem.getColumn())) {
                continue;
            }

            if (SqlInjectionUtils.check(sortItem.getColumn())) {
                log.error("Warning SQL injection: {}", sortItem.getColumn());
                throw new BusinessException("There is a risk of SQL injection, please contact technical staff!");
            }

            orderItemList.add(new OrderItem(sortItem.getColumn(), sortItem.getIsAsc()));
        }
        page.setOrders(orderItemList);
        return page;
    }

    /**
     * convert to PageResult object
     */
    public static <T, E> PageResult<T> convert2PageResult(Page<?> page, List<E> sourceList, Class<T> targetClazz) {
        return convert2PageResult(page, BeanUtils.copyList(sourceList, targetClazz));
    }

    public static <T, E> PageResult<T> convert2PageResult(Page<E> page, Class<T> targetClazz) {
        List<E> records = page.getRecords();
        List<T> targetRecords = BeanUtils.copyList(records, targetClazz);
        return new PageResult<>(page.getCurrent(),page.getSize(),page.getTotal(),page.getPages(),targetRecords, ObjectUtils.isEmpty(page));
    }

    /**
     * convert to PageResult object
     */
    public static <E> PageResult<E> convert2PageResult(Page<?> page, List<E> sourceList) {
        PageResult<E> pageResult = new PageResult<>();
        pageResult.setPageNum(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotal(page.getTotal());
        pageResult.setPages(page.getPages());
        pageResult.setList(sourceList);
        pageResult.setEmptyFlag(CollectionUtils.isEmpty(sourceList));
        return pageResult;
    }

    /**
     * convert page result
     */
    public static <E, T> PageResult<T> convert2PageResult(PageResult<E> pageResult, Class<T> targetClazz) {
        PageResult<T> newPageResult = new PageResult<>();
        newPageResult.setPageNum(pageResult.getPageNum());
        newPageResult.setPageSize(pageResult.getPageSize());
        newPageResult.setTotal(pageResult.getTotal());
        newPageResult.setPages(pageResult.getPages());
        newPageResult.setEmptyFlag(pageResult.getEmptyFlag());
        newPageResult.setList(BeanUtils.copyList(pageResult.getList(), targetClazz));
        return newPageResult;
    }

    public static <T> PageResult subListPage(Integer pageNum, Integer pageSize, List<T> list) {
        PageResult<T> pageRet = new PageResult<T>();
        //总条数
        int count = list.size();
        int pages = count % pageSize == 0 ? count / pageSize : (count / pageSize + 1);
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(pageNum * pageSize, count);

        if (pageNum > pages) {
            pageRet.setList(Lists.newLinkedList());
            pageRet.setPageNum(pageNum.longValue());
            pageRet.setPages((long) pages);
            pageRet.setTotal((long) count);
            return pageRet;
        }
        List<T> pageList = list.subList(fromIndex, toIndex);
        pageRet.setList(pageList);
        pageRet.setPageNum(pageNum.longValue());
        pageRet.setPages((long) pages);
        pageRet.setTotal((long) count);
        return pageRet;
    }
}
