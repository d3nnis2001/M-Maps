package com.gpse.basis.security;

import com.gpse.basis.security.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter { //<1>

    private static final int TEN_DAYS_IN_MILLIS = 864_000_000;

    private final AuthenticationManager authenticationManager;

    private final SecurityConstants securityConstants;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, SecurityConstants securityConstants) { //<2>
        this.authenticationManager = authenticationManager;
        this.securityConstants = securityConstants;
        setFilterProcessesUrl(this.securityConstants.getAuthLoginUrl());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) { //<3>
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) { //<4>
        UserDetails user = (UserDetails) authentication.getPrincipal();

        List<String> roles = user.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        byte[] signingKey = securityConstants.getJwtSecret().getBytes();
        String token = Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(signingKey), Jwts.SIG.HS512)
            .header().add("typ", securityConstants.getTokenType())
            .and()
            .issuer(securityConstants.getTokenIssuer())
            .audience().add(securityConstants.getTokenAudience())
            .and()
            .subject(user.getUsername())
            .expiration(new Date(System.currentTimeMillis() + TEN_DAYS_IN_MILLIS)) // + 10 Tage
            .claim("rol", roles)
            .compact();

        response.addHeader(securityConstants.getTokenHeader(), securityConstants.getTokenPrefix() + token);
    }
}
