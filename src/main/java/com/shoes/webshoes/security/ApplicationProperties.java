/**
 * 
 */
package com.shoes.webshoes.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class ApplicationProperties {

	@Value("${base.url}")
	private String baseUrl;

	@Value("${base.url.frontend}")
	private String baseUrlFe;

	@Value("${spring.datasource.driver.classname}")
	private String databaseDriver;

	@Value("${spring.datasource.url}")
	private String databaseUrl;

	@Value("${spring.datasource.username}")
	private String databaseUsername;

	@Value("${spring.datasource.password}")
	private String databasePassword;

	@Value("${hibernate.dialect}")
	private String hibernateDialect;

	@Value("${hibernate.show_sql}")
	private Boolean hibernateShowSql;

	@Value("${hibernate.format_sql}")
	private Boolean hibernateFormatSql;

	@Value("${jwt.secret}")
	private String jwtSecret;


	@Value("${password.account.google}")
	private String passwordAccountGoogle;

	@Value("${vnpay.secretKey}")
	private String vnpaySecretKey;

	@Value("${vnpay.vnp_TmnCode}")
	private String vnpTmnCode;

	@Value("${vnpay.vnp_PayUrl}")
	private String vnpPayUrl;

	@Value("${vnpay.vnp_ApiUrl}")
	private String vnpApiUrl;

	@Value("${spring.mail.host}")
	private String springMailHost;

	@Value("${spring.mail.port}")
	private String springMailPort;

	@Value("${spring.mail.username}")
	private String springMailUsername;

	@Value("${spring.mail.password}")
	private String springMailPassword;

	@Value("${spring.mail.properties.mail.smtp.auth}")
	private String springMailPropertiesMailSmtpAuth;

	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private String springMailPropertiesMailSmtpStarttlsEnable;

}