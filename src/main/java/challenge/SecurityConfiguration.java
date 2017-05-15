package challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

/**
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    private challenge.AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * Performs the HTTP Authentication for the end point being accessed.
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/h2-console/*").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().usernameParameter("id").passwordParameter("name")
                .and().httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint);

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    /**
     * Method which performs the user authentication.
     * From the Person Table, takes the ID as the username and the name as the password.
     * Created a dummy authorities query
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select id as username,name as password,'true' as enabled from person where id = ? limit 1")
                .authoritiesByUsernameQuery(
                        "select id as username, 'ROLE_USER' as role from person where id=?");

    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
//
//        authenticationManagerBuilder.inMemoryAuthentication().withUser("khaleel").password("abc").roles("USER");
//
//    }
}