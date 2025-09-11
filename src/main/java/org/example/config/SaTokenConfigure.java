package org.example.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.context.SaTokenContext;
import cn.dev33.satoken.context.SaTokenContextDefaultImpl;
import cn.dev33.satoken.filter.SaServletFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.serializer.impl.SaSerializerTemplateForJdkUseBase64;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class SaTokenConfigure implements WebMvcConfigurer {

    // 添加Sa-Token上下文配置
    @Bean
    public SaTokenContext saTokenContext() {
        return new cn.dev33.satoken.context.SaTokenContextForThreadLocal();
    }
    private final SaTokenProperties saTokenProperties;
    
    // 初始化Sa-Token配置
    @Bean
    public SaTokenConfig saTokenConfig() {
        SaTokenConfig config = new SaTokenConfig();
        config.setTokenName("satoken");
        config.setTimeout(2592000L);
        config.setActiveTimeout(-1L);
        config.setIsConcurrent(true);
        config.setIsShare(true);
        config.setTokenStyle("uuid");
        config.setIsLog(true);
        config.setIsReadHeader(true);
        config.setIsReadBody(false);
        config.setIsReadCookie(false);
        return config;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 创建 SaInterceptor 并配置路径匹配
        registry.addInterceptor(new SaInterceptor(handler -> {
                    SaRouter.match("/**")  // 匹配所有路径
                            .notMatch(saTokenProperties.getWhitelist()) // 排除路径
                            .check(r -> StpUtil.checkLogin()); // 执行登录检查
                }))
                .addPathPatterns("/**")
                .order(Ordered.HIGHEST_PRECEDENCE); // 设置拦截器优先级
    }
    //配置跨域请求
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") //指定跨域地址  .allowedOrigins("http://localhost:3000", "https://yourdomain.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
//    /**
//     * 重写Sa-Token框架内部算法策略
//     */
//    @PostConstruct
//    public void rewiteSaStrategy(){
//        // 重写 Token 生成策略
//        SaStrategy.instance.createToken = (loginId, loginType) -> {
//            return JwtUtil.createJWT(loginId.toString());    // 随机60位长度字符串
//        };
//    }
}

