package com.waiXiao.common;

import java.io.Serializable;

/**
 * @param :
 * @author :     mym
 * @version :    V1.0
 * @date :       2019/11/29 9:41
 * @describe :   带分页参数的基类
 * @return :
 */
public class BasePageUtil implements Serializable {
    private int pageSize = 20;//每页20条
    private int pageNumber = 1;//当前页
    private String sortOrder;
    private String sortName;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }
}
