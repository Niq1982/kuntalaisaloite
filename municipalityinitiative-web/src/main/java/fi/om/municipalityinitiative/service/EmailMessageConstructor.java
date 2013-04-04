package fi.om.municipalityinitiative.service;

import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;

public class EmailMessageConstructor {

    @Resource
    private JavaMailSender javaMailSender;



}
