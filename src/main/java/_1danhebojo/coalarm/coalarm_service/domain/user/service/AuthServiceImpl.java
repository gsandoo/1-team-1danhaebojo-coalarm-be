package _1danhebojo.coalarm.coalarm_service.domain.user.service;

import _1danhebojo.coalarm.coalarm_service.global.oauth.CoalarmOAuth2User;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Builder
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

	@Override
	public Long getLoginUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null) {
            return null;
        }

        // 로그인 O -> UsernamePasswordAuthenticationToken
        // 로그인 X -> AnonymousAuthenticationToken
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) {
            return null;
        }

        // 로그인 O -> SingKUserDetails
        // 로그인 X -> anonymousUser
        Object details = authentication.getPrincipal();
        if (!(details instanceof CoalarmOAuth2User)) {
            return null;
        }

		return ((CoalarmOAuth2User) details).getId();
	}
}
