package com.shoes.webshoes.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
//anotaion này dùng để cho phép phân quyền dựa trên @PreAuthorize
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	// tạo bean call api service
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

	// spring xác thực người dùng
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
		return new Base64PasswordEncoder();
	}

	// cho phép sử dụng AuthenticationManager trong ứng dụng
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.csrf().disable()
				// không xác thực với các request
				.authorizeRequests().antMatchers("/api/v1/authentication/**", "/api-docs/**", "/swagger-ui/**")
				.permitAll()
				// tất cả các request khác cần phải được xác thực
				.anyRequest().authenticated().and()
				// Xác định cách xử lý ngoại lệ xác thực bằng cách sử dụng
				// jwtAuthenticationEntryPoint
				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
				// phiên không được sử dụng để lưu trạng thái người dùng
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//		.antMatchers("/admin/**").hasAuthority("ADMIN")


		httpSecurity.cors();

		// thêm filter để validate token với tất cả các request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

//	cấu hình swagger
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**", "/webjars/**");
	}
}