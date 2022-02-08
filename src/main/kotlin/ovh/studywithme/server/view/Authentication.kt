package ovh.studywithme.server.view

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter 
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity

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