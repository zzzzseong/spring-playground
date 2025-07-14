package me.jisung.springplayground.product.mapper;

import me.jisung.springplayground.product.data.ProductRequest;
import me.jisung.springplayground.product.data.ProductResponse;
import me.jisung.springplayground.product.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductStructMapper {

    ProductStructMapper INSTANCE = Mappers.getMapper(ProductStructMapper.class);

    ProductEntity toEntity(ProductRequest productRequest);

    ProductRequest toDTO(ProductEntity productEntity);

    ProductResponse toResponse(ProductEntity productEntity);

}
