package com.techgoons.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class I18nController {

    @Autowired
    MessageSource messageSource;

    @GetMapping("/")
    public String index(Locale locale) {
        return messageSource.getMessage("welcome", null, locale);
    }


}
