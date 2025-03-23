package _1danhebojo.coalarm.coalarm_service.domain.dashboard.service;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.CoinDTO;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.CoinEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.jpa.CoinJpaRepository;
import _1danhebojo.coalarm.coalarm_service.global.api.ApiException;
import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import _1danhebojo.coalarm.coalarm_service.global.api.OffsetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService {

    private final CoinJpaRepository coinJpaRepository;

    @Override
    public List<CoinDTO> getAllCoins() {
        try {
            List<CoinEntity> coins = coinJpaRepository.findAll();
            return coins.stream()
                    .map(CoinDTO::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ApiException(AppHttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public OffsetResponse<CoinDTO> getCoinsWithPaging(Integer offset, Integer limit) {
        try {
            if (offset < 0) {
                throw new ApiException(AppHttpStatus.INVALID_OFFSET);
            }

            if (limit <= 0) {
                throw new ApiException(AppHttpStatus.INVALID_LIMIT);
            }

            int page = offset / limit;
            Page<CoinEntity> coinsPage = coinJpaRepository.findAll(PageRequest.of(page, limit));

            List<CoinDTO> coinDTOs = coinsPage.getContent().stream()
                    .map(CoinDTO::new)
                    .collect(Collectors.toList());

            return OffsetResponse.of(
                    coinDTOs,
                    offset,
                    limit,
                    coinsPage.getTotalElements()
            );
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(AppHttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CoinDTO getCoinById(Long coinId) {
        if (coinId == null || coinId <= 0) {
            throw new ApiException(AppHttpStatus.INVALID_COIN_ID);
        }

        CoinEntity coinEntity = coinJpaRepository.findByCoinId(coinId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_COIN));

        return new CoinDTO(coinEntity);
    }
}