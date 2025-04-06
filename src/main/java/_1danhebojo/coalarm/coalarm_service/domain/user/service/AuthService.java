package _1danhebojo.coalarm.coalarm_service.domain.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
	Long getLoginUserId();
	void logout(HttpServletRequest request, HttpServletResponse response) throws IOException;

	void unlinkKakaoAccount();
}
