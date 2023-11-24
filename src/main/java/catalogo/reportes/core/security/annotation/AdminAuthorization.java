package catalogo.reportes.core.security.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@PreAuthorize("hasRole(T(rondanet.catalogo.core.enums.Role).SYSTEM_ADMIN)")
public @interface AdminAuthorization {

}
