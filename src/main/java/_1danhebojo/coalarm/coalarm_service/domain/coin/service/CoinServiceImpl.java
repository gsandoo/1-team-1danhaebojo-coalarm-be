package _1danhebojo.coalarm.coalarm_service.domain.coin.service;

import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.CoinRepository;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.CoinDTO;
import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.jpa.CoinJpaRepository;
import _1danhebojo.coalarm.coalarm_service.global.api.ApiException;
import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import _1danhebojo.coalarm.coalarm_service.global.api.OffsetResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService {

    private final CoinJpaRepository coinJpaRepository;
    private final CoinRepository coinRepository;
    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public List<CoinDTO> getMyAlertCoins(Long userId) {
        return coinRepository.findAlertCoinsByUserId(userId)
                .stream()
                .map(CoinDTO::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public CoinDTO getCoinById(Long coinId) {
        if (coinId == null || coinId <= 0) {
            throw new ApiException(AppHttpStatus.INVALID_COIN_ID);
        }

        CoinEntity coinEntity = coinJpaRepository.findByCoinId(coinId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND_COIN));

        return new CoinDTO(coinEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoinDTO> searchCoinByNameOrSymbol(String term) {
        if (term == null || term.trim().isEmpty()) {
            throw new ApiException(AppHttpStatus.EMPTY_SEARCH_TERM);
        }

        // 검색 수행
        List<CoinEntity> coins = coinJpaRepository.findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(term, term);

        // 검색 결과가 없는 경우 처리
        if (coins.isEmpty()) {
            throw new ApiException(AppHttpStatus.NO_SEARCH_RESULTS);
        }

        // 엔티티 목록을 DTO 목록으로 변환
        return coins.stream()
                .map(CoinDTO::new)
                .collect(Collectors.toList());
    }
}