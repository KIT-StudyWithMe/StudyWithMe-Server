package ovh.studywithme.server.view

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter 
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .anyRequest()
                .authenticated()

        http.oauth2ResourceServer()
                .jwt()
    }
}

//class CustomAccessTokenEnhancer : TokenEnhancer() {

//    @Override
//    override fun enhance(accessToken: OAuth2AccessToken, authentication: OAuth2Authentication) : OAuth2AccessToken {
//        var userAuthentication : Authentication  = authentication.getUserAuthentication()
//        if (userAuthentication != null) {
//            var principal Object = authentication.getUserAuthentication().getPrincipal()
//            if (principal instanceof CustomUserDetails) {
//                var additionalInfo Map<String, Object> = new HashMap<>()
//                additionalInfo.put("userDetails", principal)
//                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo)
//            }
//        }
//        return accessToken;
//    }
//}