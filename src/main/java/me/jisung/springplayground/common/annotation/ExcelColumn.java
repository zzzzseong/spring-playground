package me.jisung.springplayground.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 엑셀파일 다운로드시 필드와 엑셀파일 컬럼을 매핑하기 위한 어노테이션
 * @see me.jisung.springplayground.common.util.ExcelUtil
 * @see me.jisung.springplayground.product.entity.ProductEntity
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {

    String headerName() default "";

}