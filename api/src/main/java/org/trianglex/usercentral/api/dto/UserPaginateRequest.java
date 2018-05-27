package org.trianglex.usercentral.api.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static org.trianglex.usercentral.api.constant.UasConstant.*;

public class UserPaginateRequest {

    @NotNull(message = PAGE_NO_NOT_NULL)
    @Min(value = 1, message = POSITIVE)
    private Integer pageNo;

    @NotNull(message = PAGE_SIZE_NOT_NULL)
    @Min(value = 1, message = POSITIVE)
    private Integer pageSize;

    @NotBlank(message = FIELDS_NOT_BLANK)
    private String fields;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }
}
