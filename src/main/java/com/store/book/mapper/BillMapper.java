package com.store.book.mapper;

import com.store.book.dao.entity.Bill;
import com.store.book.dao.entity.Cart;
import com.store.book.dao.entity.Item;
import com.store.book.dao.model.BillModel;
import com.store.book.dao.model.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static com.store.book.constants.CompanyConstants.COMPANY_ADDRESS;
import static com.store.book.constants.CompanyConstants.COMPANY_NAME;

@Mapper(componentModel = "spring")
public abstract class BillMapper {

    @Autowired
    protected ProductMapper productMapper;

    @Mappings({
            @Mapping(target = "totalPrice", source = "cart", qualifiedByName = "calculateTotalPrice"),
            @Mapping(target = "companyName", source = "cart", qualifiedByName = "getCompanyName"),
            @Mapping(target = "companyAddress", source = "cart", qualifiedByName = "getCompanyAddress"),
            @Mapping(target = "productList", source = "cart.item", qualifiedByName = "mapItemToProduct"),
            @Mapping(target = "createdDate", source = "createdAt")
    })
    public abstract BillModel cartToBill(Cart cart);

    public abstract BillModel entityToModel(Bill bill);

    public abstract Bill modelToEntity(BillModel billModel);

    @Named("mapItemToProduct")
    public List<ProductModel> mapItemToProduct(List<Item> item) {
        return productMapper.itemsToProductModel(item);
    }

    @Named("calculateTotalPrice")
    public BigDecimal calculateTotalPrice(Cart cart) {
        return cart.getItem().stream()
                .map(item ->
                        item.getBook().getNewPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Named("getCompanyName")
    public String getCompanyName(Cart cart) {
        return COMPANY_NAME;
    }

    @Named("getCompanyAddress")
    public String getCompanyAddress(Cart cart) {
        return COMPANY_ADDRESS;
    }
}
