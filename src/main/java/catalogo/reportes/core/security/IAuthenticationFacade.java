package catalogo.reportes.core.security;

import catalogo.reportes.reportesExcel.core.resources.dto.UsuarioPrincipal;
import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
    UsuarioPrincipal getPrincipalAuth();
}
