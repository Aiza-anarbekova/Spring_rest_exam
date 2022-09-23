package com.example.spring_rest_exam.security.jwt;

import com.example.spring_rest_exam.model.User;
import com.example.spring_rest_exam.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtUtils;
    private final UserRepository authInfoRepository;

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    public JwtTokenFilter(JwtTokenUtil jwtUtils, UserRepository authInfoRepository) {
        this.jwtUtils = jwtUtils;
        this.authInfoRepository = authInfoRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Optional<String> optionalJwt = getJwtFromRequest(request);

        optionalJwt.ifPresent(token -> {


            String email = jwtUtils.validateJWTToken(token);

            User authInfo = authInfoRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException(String.format("email(%s) not found", email)));

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    authInfo,
                    null,
                    authInfo.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        });

        filterChain.doFilter(request, response);
    }

    private Optional<String> getJwtFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader(AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return Optional.of(bearerToken.substring(BEARER.length()));
        }

        return Optional.empty();
    }

}
//    private final UserServiceImpl userService;
//    private final JwtTokenUtil jwtTokenUtil;
//
//    public JwtTokenFilter(UserServiceImpl userService, JwtTokenUtil jwtTokenUtil) {
//        this.userService = userService;
//        this.jwtTokenUtil = jwtTokenUtil;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        final String tokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        String username = null;
//        String jwt = null;
//        if (tokenHeader !=null && tokenHeader.startsWith("Bearer ")){
//            jwt = tokenHeader.substring(7);
//            username = jwtTokenUtil.getUserNameFromToken(jwt);
//            if (username != null && SecurityContextHolder.getContext().getAuthentication()==null){
//                UserDetails userDetails = this.userService.loadUserByUsername(username);
//                if (jwtTokenUtil.validateToken(jwt,userDetails)){
//                    UsernamePasswordAuthenticationToken token =
//                            new UsernamePasswordAuthenticationToken
//                            (userDetails,null,userDetails.getAuthorities());
//                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(token);
//                }
//
//            }
//
//
//        }
//        filterChain.doFilter(request,response);
//    }
//}
