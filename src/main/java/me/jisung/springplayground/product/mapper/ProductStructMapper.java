package me.jisung.springplayground.product.mapper;

import me.jisung.springplayground.product.data.ProductDTO;
import me.jisung.springplayground.product.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductStructMapper {

    ProductStructMapper INSTANCE = Mappers.getMapper(ProductStructMapper.class);

    ProductEntity toEntity(ProductDTO productDTO);

    ProductDTO toDTO(ProductEntity productEntity);

}
