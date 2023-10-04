package com.example.expenseit.filter;


import com.example.expenseit.Constants;
import com.example.expenseit.errors.authentication.JwtTokenExpiredException;
import com.example.expenseit.errors.authentication.NoJwtTokenProvidedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String token = request.getHeader("Authorization");
        if(token != null){

            try{
                Claims claims = Jwts.parser().setSigningKey(Constants.SECRET_KEY).parseClaimsJws(token).getBody();
                request.setAttribute("clientId", Integer.parseInt(claims.get("subject").toString()));
            }catch (Exception e){
                throw new JwtTokenExpiredException("Invalid or expired token");
            }
        } else{
            throw new NoJwtTokenProvidedException("Token must be provided");
        }

        filterChain.doFilter(servletRequest,servletResponse);
    }
}
