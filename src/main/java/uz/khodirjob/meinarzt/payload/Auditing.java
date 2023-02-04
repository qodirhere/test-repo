package uz.khodirjob.meinarzt.payload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.khodirjob.meinarzt.entity.User;
import uz.khodirjob.meinarzt.repository.UserRepository;
import uz.khodirjob.meinarzt.security.UserPrincipal;

import java.util.Optional;

public class Auditing implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
                authentication.isAuthenticated() &&
                !authentication.getPrincipal().equals("anonymousUser")) {
            UserPrincipal currentUser = (UserPrincipal) authentication.getPrincipal();

            return Optional.of(currentUser.getId());
        }
        return Optional.empty();
    }
}
