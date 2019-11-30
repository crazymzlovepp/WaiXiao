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
    private int limit = 20;//每页20条
    private int page = 1;//当前页
    private String sortOrder;
    private String sortName;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
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
