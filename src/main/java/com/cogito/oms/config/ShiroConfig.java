package com.cogito.oms.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.apache.shiro.authc.AbstractAuthenticator;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.cogito.oms.data.model.User;
import com.cogito.oms.security.InlineRealm;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import nz.net.ultraq.thymeleaf.LayoutDialect;

@Configuration
public class ShiroConfig {
	@Bean
	public FilterRegistrationBean characterEncodingFilter() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);

		return filterRegistration;
	}

	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter() {
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		shiroFilter.setLoginUrl("/login");
		shiroFilter.setSuccessUrl("/index");
		shiroFilter.setUnauthorizedUrl("/forbidden");
		Map<String, String> filterChainDefinitionMapping = new HashMap<String, String>();

		filterChainDefinitionMapping.put("/logout", "logout");
		filterChainDefinitionMapping.put("/patternfly/**", "anon");
		filterChainDefinitionMapping.put("/css/**", "anon");
		filterChainDefinitionMapping.put("/images/**", "anon");
		filterChainDefinitionMapping.put("/fonts/**", "anon");
		filterChainDefinitionMapping.put("/js/**", "anon");
		filterChainDefinitionMapping.put("/**", "authc");
		shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMapping);
		shiroFilter.setSecurityManager(securityManager());

		Map<String, Filter> filters = new HashMap<String, Filter>();
		filters.put("anon", new AnonymousFilter());
		filters.put("authc", new FormAuthenticationFilter());
		filters.put("logout", new LogoutFilter());
		// filters.put("roles", new RolesAuthorizationFilter());
		// filters.put("user", new UserFilter());
		shiroFilter.setFilters(filters);
		return shiroFilter;
	}
	
	
	@Bean
	public PasswordService defaultPasswordService() {
		DefaultPasswordService defaultPasswordService = new DefaultPasswordService();
		return defaultPasswordService;
	}

	@Bean(name = "sessionManager")
	public DefaultWebSessionManager defaultWebSessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setGlobalSessionTimeout(15 * 60 * 1000);
		// sessionManager.setSessionIdCookieEnabled(true);
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionDAO(new EnterpriseCacheSessionDAO());

		return sessionManager;
	}

	@Bean(name = "cacheManager")
	public CacheManager cacheManager() {
		EhCacheManager cache = new EhCacheManager();
		return cache;
	}

	@Bean(name = "securityManager")
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(realm());
		DefaultWebSessionManager defaultWebSessionManager = defaultWebSessionManager();
		securityManager.setSessionManager(defaultWebSessionManager);
		securityManager.setCacheManager(cacheManager());
		securityManager.setSessionManager(defaultWebSessionManager());
		if (securityManager.getAuthenticator() instanceof AbstractAuthenticator) {
			AbstractAuthenticator aa = (AbstractAuthenticator) securityManager.getAuthenticator();
			aa.getAuthenticationListeners().add(new AuthenticationListener() {

				@Override
				public void onSuccess(AuthenticationToken arg0, AuthenticationInfo arg1) {
					SimpleAuthenticationInfo sai = (SimpleAuthenticationInfo) arg1;
					SessionDAO sessionDAO = defaultWebSessionManager.getSessionDAO();
					Collection<Session> activeSessions = sessionDAO.getActiveSessions();
					PrincipalCollection principals = sai.getPrincipals();
					if (principals == null || principals.isEmpty())
						return;
					User u = (User) principals.getPrimaryPrincipal();
					for (Session s : activeSessions) {
						SimplePrincipalCollection pc = (SimplePrincipalCollection) s.getAttribute(
								"org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY");
						if (pc == null || pc.isEmpty())
							continue;
						User userInSession = (User) pc.getPrimaryPrincipal();
						if (u.equals(userInSession)) {
							System.out.println("Found similar ....");
							Subject subject = new Subject.Builder(securityManager).session(s).buildSubject();
							subject.logout();
						}
					}
				}

				@Override
				public void onLogout(PrincipalCollection arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFailure(AuthenticationToken arg0, AuthenticationException arg1) {
					// TODO Auto-generated method stub

				}
			});
		}
		return securityManager;

	}

	@Bean(name = "realm")
	@DependsOn("lifecycleBeanPostProcessor")
	public AuthorizingRealm realm() {
		AuthorizingRealm realm = new InlineRealm();
		CredentialsMatcher matcher = new PasswordMatcher();
//		matcher.setHashAlgorithmName("SHA-256");
//		matcher.setStoredCredentialsHexEncoded(true);
		realm.setCredentialsMatcher(matcher);
		realm.setCacheManager(new MemoryConstrainedCacheManager());
		return realm;
	}

	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	@Autowired
	public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver);
		// Enable shiro dialect for thymeleaf
		engine.addDialect(new ShiroDialect());
		engine.addDialect(new LayoutDialect());
		return engine;
	}

}
