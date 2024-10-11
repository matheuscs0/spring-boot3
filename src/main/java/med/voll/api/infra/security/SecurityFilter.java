package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


//fazer o filtro para validar o token passado nas apis agora
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("oi");
        var tokenJWT = recuperarToken(request);

        if(tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT);
            System.out.println(subject);
            var usuario = repository.findByLogin(subject);
            System.out.println(usuario);

            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()); //forçar o spring a se o jwt for valido ele validar o cliente
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request,response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null){
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
