package io.sagegrey.backend.services;

import io.sagegrey.backend.dtos.JwtAuthResponse;
import io.sagegrey.backend.dtos.LoginDto;
import io.sagegrey.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletResponse httpServletResponse;
    private final HttpServletRequest httpServletRequest;

    public ResponseEntity<JwtAuthResponse> login(LoginDto loginDto) throws Exception {
        Authentication authentication ;
        String token;

        try{
            Authentication auth =  new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(),loginDto.getPassword());

            authentication = authenticationManager.authenticate(auth);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenProvider.generateToken(authentication);
            httpServletResponse.setHeader("Authorization", token);

        }
        catch (BadCredentialsException ex){
            throw new Exception("incorrect user credentials", ex);

        }
        return ResponseEntity.ok(new JwtAuthResponse(token));
    }
}
