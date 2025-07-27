package com.store.book.mapper;

import com.store.book.dao.entity.Cart;
import com.store.book.dao.model.BillModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.math.BigDecimal;

import static com.store.book.constants.CompanyConstants.COMPANY_ADDRESS;
import static com.store.book.constants.CompanyConstants.COMPANY_NAME;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface BillMapper {

    @Mappings({
            @Mapping(target = "totalPrice", source = "cart", qualifiedByName = "calculateTotalPrice"),
            @Mapping(target = "companyName", source = "cart", qualifiedByName = "getCompanyName"),
            @Mapping(target = "companyAddress", source = "cart", qualifiedByName = "getCompanyAddress")
    })
    BillModel cartToBill(Cart cart);

    @Named("calculateTotalPrice")
    default BigDecimal calculateTotalPrice(Cart cart) {
        return cart.getItem().stream()
                .map(item ->
                        item.getBook().getNewPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Named("getCompanyName")
    default String getCompanyName(Cart cart) {
        return COMPANY_NAME;
    }

    @Named("getCompanyAddress")
    default String getCompanyAddress(Cart cart) {
        return COMPANY_ADDRESS;
    }
}
