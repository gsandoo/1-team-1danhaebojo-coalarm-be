package _1danhebojo.coalarm.coalarm_service.domain.dashboard.service;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.ResponseKimchiPremium;
import _1danhebojo.coalarm.coalarm_service.global.api.OffsetResponse;

import java.util.List;

public interface KimchiPremiumService {
    OffsetResponse<ResponseKimchiPremium> getKimchiPremiums(int offset, int limit);
    void calculateAndSaveKimchiPremium();
}
