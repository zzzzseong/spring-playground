package me.jisung.springplayground.common.json;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jisung.springplayground.common.constant.ApiConstant;
import me.jisung.springplayground.common.exception.Api4xxErrorCode;
import me.jisung.springplayground.common.exception.ApiException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Objects;

@Getter
@NoArgsConstructor
public abstract class ApiPageRequest {

    @Min(value = ApiConstant.MIN_PAGE_NUMBER, message = "Invalid page number.")
    @Max(value = ApiConstant.MAX_PAGE_NUMBER, message = "Invalid page number.")
    public Integer page;

    @Min(value = ApiConstant.MIN_PAGE_SIZE, message = "Invalid page size.")
    @Max(value = ApiConstant.MAX_PAGE_SIZE, message = "Invalid page size.")
    public Integer size;

    public Pageable makePageable() {
        if(Objects.isNull(page) || Objects.isNull(size)) {
            Api4xxErrorCode errorCode = Api4xxErrorCode.INVALID_REQUEST_PARAMETER;
            throw ApiException.builder()
                    .httpStatus(errorCode.getHttpStatus())
                    .code(errorCode.getCode())
                    .message(ApiConstant.NOT_NULL_MESSAGE + " ( page || size )")
                    .build();
        }
        return PageRequest.of(page - 1, size);
    }
}
