package problemsolved.filingsystem.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import problemsolved.filingsystem.entities.User;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    
    private static final String COOKIE_NAME = "token";

    private static final int EXPIRATION =  60 * 60 * 1000;
    
    private String secret;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, String secret) {
        this.authenticationManager = authenticationManager;
        this.secret = secret;
        setFilterProcessesUrl("/users/login");
        
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            User creds = new ObjectMapper()
                    .readValue(req.getInputStream(), User.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        long now = System.currentTimeMillis();
        String token = JWT.create()
                .withSubject(auth.getName())
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + EXPIRATION))
                .withClaim("roles", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(Algorithm.HMAC512(secret.getBytes()));
//        String token = Jwts.builder()
//                    .setSubject(auth.getName())
//                    .claim("authorities", auth.getAuthorities().stream()
//                            .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
//                    .setIssuedAt(new Date(now))
//                    .setExpiration(new Date(now + EXPIRATION))
//                    .signWith(SignatureAlgorithm.HS512, SECRET)
//                    .compact();

        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setMaxAge(EXPIRATION);
        cookie.setPath("/users/api");
        cookie.setHttpOnly(true);
        res.addCookie(cookie);

        Map<String, String> responseObject  = new HashMap<String, String>() {{
            put("token", token);
        }};
        res.getWriter().write(new ObjectMapper().writeValueAsString(responseObject));
        res.getWriter().flush();
    }
}