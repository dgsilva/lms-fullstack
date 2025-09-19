package br.com.entrevista.api.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.entrevista.api.entities.Usuario;
import br.com.entrevista.api.servicies.CustomUserDetailsService.CustomUserPrincipal;

public class SecurityUtils {
    
    public static Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal) {
            CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUsuario();
        }
        
        throw new RuntimeException("Usuário não autenticado");
    }
    
    public static Long getUsuarioId() {
        return getUsuarioAutenticado().getId();
    }
}
