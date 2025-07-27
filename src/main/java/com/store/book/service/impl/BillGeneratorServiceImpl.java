package com.store.book.service.impl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.store.book.dao.BillRepository;
import com.store.book.dao.entity.Bill;
import com.store.book.dao.entity.Cart;
import com.store.book.dao.model.BillModel;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.BillMapper;
import com.store.book.service.BillGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class BillGeneratorServiceImpl implements BillGeneratorService {

    private final MessageSource messageSource;
    private final TemplateEngine templateEngine;
    private final BillMapper billMapper;
    private final BillRepository billRepository;

    @SneakyThrows
    public byte[] generateBillPdf(BillModel model) {
        Context context = new Context();
        context.setVariable("companyName", model.getCompanyName());
        context.setVariable("createdDate", model.getCreatedDate());
        context.setVariable("productList", model.getProductList());
        context.setVariable("totalPrice", model.getTotalPrice());
        context.setVariable("companyAddress", model.getCompanyAddress());
        Bill entity = billMapper.modelToEntity(model);
        billRepository.save(entity);
        String htmlContent = templateEngine.process("bill", context);
        return generateFromHtml(htmlContent);
    }

    @Override
    public void save(Cart cart) {
        BillModel model = billMapper.cartToBill(cart);
        Bill entity = billMapper.modelToEntity(model);
        billRepository.save(entity);
    }

    @SneakyThrows
    private byte[] generateFromHtml(String html) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, null);
        builder.toStream(outputStream);
        builder.run();
        return outputStream.toByteArray();
    }

    @Override
    public byte[] getBillPdf(Long id) {
        Locale locale = LocaleContextHolder.getLocale();
        Bill bill = billRepository.findById(id).orElseThrow(() -> new NotFoundException(
                messageSource.getMessage("NotFoundErrorMessage", null, locale)));
        BillModel model = billMapper.entityToModel(bill);
        return generateBillPdf(model);
    }
}
