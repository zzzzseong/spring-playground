package me.jisung.springplayground.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiConst {

    public static final String RESPONSE_RESULT_SUCCESS  = "success";
    public static final String RESPONSE_RESULT_FAIL     = "fail";

    public static final String RESPONSE_CODE_SUCCESS    = "S20000";

    /* pagination */
    public static final int DEFAULT_PAGE_NUMBER         = 1;
    public static final int DEFAULT_PAGE_SIZE           = 10;
    public static final String DEFAULT_SORT_BY          = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION   = "ASC";

    public static final int MIN_PAGE_NUMBER             = 1;
    public static final int MAX_PAGE_NUMBER             = 100;
    public static final int MIN_PAGE_SIZE               = 1;
    public static final int MAX_PAGE_SIZE               = 1000;

}
