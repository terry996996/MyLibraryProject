// package library.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class CorsConfig implements WebMvcConfigurer {

// @Override
// public void addCorsMappings(CorsRegistry registry) {
// registry.addMapping("/**") // 允許所有路徑
// .allowedOrigins("http://localhost:5173") // 允許前端的來源
// .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允許的方法
// .allowedHeaders("*") // 允許所有標頭
// .allowCredentials(true); // 若前端有帶 cookie/token 就設 true
// }
// }
