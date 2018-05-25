/**
* @author  Walid Abbassi
*/
package com.opendevup.scolarite.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private NoOpPasswordEncoder noOpPasswordEncoder;

	@Autowired
	private DataSource dataSource;
	
	@Value("${spring.admin.username}")
	private String adminUsername;
	
	@Value("${spring.admin.password}")
	private String adminPassword;
	
	@Value("${spring.user.username}")
	private String userUsername;
	
	@Value("${spring.user.password}")
	private String userPassword;
	
	@Value("${spring.queries.users-query}")
	private String usersQuery;
	
	@Value("${spring.queries.roles-query}")
	private String rolesQuery;
	
	/**
	 * to store the persistent login tokens for a user.
	 * @return
	 */
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl dataBase = new JdbcTokenRepositoryImpl();
		dataBase.setDataSource(dataSource);
		return dataBase;
	}
	
	/**
	 * @return
	 */
	private Filter csrfHeaderFilter() {
		return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request,
					HttpServletResponse response, FilterChain filterChain)
							throws ServletException, IOException {
				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
				if (csrf != null) {
					Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
					String token = csrf.getToken();
					if (cookie == null || token != null && !token.equals(cookie.getValue())) {
						cookie = new Cookie("XSRF-TOKEN", token);
						cookie.setPath("/");
						response.addCookie(cookie);
					}
				}
				filterChain.doFilter(request, response);
			}
		};
	}
	
	/**
	 * add it to the XSRF-TOKEN header with each request
	 * @return
	 */
	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		//JDBC Authentication
		auth.
			jdbcAuthentication()
				.usersByUsernameQuery(usersQuery)
				.authoritiesByUsernameQuery(rolesQuery)
				.dataSource(dataSource)
				.passwordEncoder(bCryptPasswordEncoder);
		//In-Memory Authentication
		//You can also simply prefix {noop} to your passwords in order for the DelegatingPasswordEncoder use the NoOpPasswordEncoder to validate these passwords.
		//.password("{noop}YourPassword")
		auth.inMemoryAuthentication().passwordEncoder(noOpPasswordEncoder).withUser(adminUsername).password(adminPassword).authorities("USER","ADMIN");
		auth.inMemoryAuthentication().passwordEncoder(noOpPasswordEncoder).withUser(userUsername).password(userPassword).authorities("USER");
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//set a prefix for the the content - the default one is ROLE_
		//http.authorizeRequests().antMatchers("/user/*").hasRole("USER");
		//checks the content WITHOUT a prefix, i.e. just the pure content
		//http.authorizeRequests().antMatchers("/admin/**").hasAuthority("ADMIN");
		http.
			authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/registration").permitAll()
				.antMatchers("/admin/**").hasAuthority("ADMIN")
				.antMatchers("/user/**").hasAuthority("USER").anyRequest()
				.authenticated()
				.and()
				.csrf().csrfTokenRepository(csrfTokenRepository()) // persist the CsrfToken in a cookie
				.and()
				.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class) // Allows adding a Filter after one of the known Filter classes.
				.rememberMe().tokenRepository(persistentTokenRepository()) // remember-me authentication saved in database table persistent_logins
                .useSecureCookie(true) //specify if a secure cookie be used Default use value of HttpServletRequest.isSecure() at the time of creation.
                .and()
                .headers().frameOptions().sameOrigin() // include all of the default headers but modify X-Frame-Options to be sameOrigin:
                .and()
				.formLogin()
				.loginPage("/login").failureUrl("/login?error=true")
				.defaultSuccessUrl("/home")
				.usernameParameter("username")
				.passwordParameter("password")
				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/")
				.and()
				.exceptionHandling()
				.and()
				.authorizeRequests().antMatchers("/login/**").anonymous().anyRequest().authenticated();
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.WebSecurity)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web
	       .ignoring()
	       .antMatchers("/resources/**", "/static/**", "/css/**", "/images/**", "/webjars/**");
	}

}