package _1danhebojo.coalarm.coalarm_service.domain.dashboard.service;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.ResponseKimchiPremium;

import java.util.List;

public interface KimchiPremiumService {
    List<ResponseKimchiPremium> getKimchiPremiums(int offset, int limit);
    void calculateAndSaveKimchiPremium();
}
