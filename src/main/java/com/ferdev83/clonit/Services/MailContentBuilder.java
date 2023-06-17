package com.ferdev83.clonit.Services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Builder
public class MailContentBuilder {
    @Autowired
    private TemplateEngine templateEngine;

    public String build(String message) {
        Context context= new Context();
        context.setVariable("message", message);
        return templateEngine.process("mailTemplate", context);
    }
}
