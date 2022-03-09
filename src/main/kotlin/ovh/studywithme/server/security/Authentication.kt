package ovh.studywithme.server.security

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ComponentScan
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter 
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.Authentication
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.hibernate.mapping.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.BufferedReader
import javax.servlet.FilterChain
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Web security configuration
 *
 * @constructor Create empty Web security configuration
 */
@Configuration
@EnableWebSecurity
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {

    private val authEnabled: Boolean = false

    override fun configure(http: HttpSecurity) {
        if(authEnabled){
            http.authorizeRequests()
                .anyRequest()
                .authenticated()

            http.oauth2ResourceServer()
                .jwt()
        } else {
            http.csrf().disable()
                .authorizeRequests().anyRequest()
                .anonymous()
                .and()
                .httpBasic().disable();
        }
    }
}

class AuthenticationFilter() : OncePerRequestFilter() {
    //companion object {
    //    private fun String.extractToken() = startsWith("Bearer ")
                //.maybe { split(" ").last() }
    //}
    val logger = LoggerFactory.getLogger(this.javaClass);

    override fun doFilterInternal(request:HttpServletRequest, response:HttpServletResponse, filterChain:FilterChain) {
        //val bearer: String? = request.getHeader("Bearer")
        //val content: String = request.getParameter("Url")
        //logger.warn("Got Request with token: " + bearer + content)
        
        //val token = Option.fx {
        //    val (header) = request.getHeader(Headers.AUTHORIZATION).toOption()
        //    val (jwt) = header.extractToken()
        //    val (token) = verifier.verify(jwt).toOption()
        //    SecurityContextHolder.getContext().authentication = token
        //}

        //continue as usual
        filterChain.doFilter(request, response);
    }
}

@SpringBootApplication
class ServerInterceptor : HandlerInterceptor {

    val logger = LoggerFactory.getLogger(this.javaClass);

    override fun preHandle(request:HttpServletRequest, response:HttpServletResponse, handler:Any):Boolean {
        logger.info("--- Received Request: ---")
        val requestCacheWrapperObject:HttpServletRequest =  ContentCachingRequestWrapper(request)

        //val auth:String? = requestCacheWrapperObject.getHeader("Authorization")
        //if(auth != null && !auth.isEmpty()) {
        //    logger.info(auth)
        //}
        
        //val body:String? = getBody(requestCacheWrapperObject)
        //if(body != null && !body.isEmpty()) {
        //    logger.info(body)
        //}
        return true
    }

    override fun afterCompletion(request:HttpServletRequest, response:HttpServletResponse, handler:Any, ex:Exception?) {
        val responseCacheWrapperObject:HttpServletResponse =  ContentCachingResponseWrapper(response)
        //val body:String = getBody(responseCacheWrapperObject)
        //if(!body.isEmpty()) {
        //    logger.info("Responded with" + body)
        //}
        logger.info("=== Completed Request ===")
    }

    private fun getBody(request: HttpServletRequest): String = request
    .inputStream.bufferedReader().use { it.readText() }
}

@SpringBootApplication
class AppConfig(): WebMvcConfigurer {
	
	@Autowired
	lateinit var logInterceptor: ServerInterceptor

	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(logInterceptor);
	}
}