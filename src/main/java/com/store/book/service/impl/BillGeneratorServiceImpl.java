package com.store.book.service.impl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.store.book.dao.entity.Cart;
import com.store.book.dao.model.BillModel;
import com.store.book.service.BillGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;

@RequiredArgsConstructor
@Service
public class BillGeneratorServiceImpl implements BillGeneratorService {

    private final TemplateEngine templateEngine;

    @Override
    @SneakyThrows
    public byte[] generateBillPdf(BillModel bill) {
        Context context = new Context();
        context.setVariable("companyName", bill.getCompanyName());
        context.setVariable("createdDate", bill.getCreatedDate());
        context.setVariable("productList", bill.getProductList());
        context.setVariable("totalPrice", bill.getTotalPrice());
        context.setVariable("companyAddress", bill.getCompanyAddress());

        String htmlContent = templateEngine.process("bill", context);
        return generateFromHtml(htmlContent);
    }

    @Override
    public void save(Cart cart) {

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
}
