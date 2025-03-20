package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tickers")
public class Ticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp; // TIMESTAMPTZ 지원

    @Column(name = "exchange", nullable = false)
    private String exchange; // 거래소 (e.g. 바이낸스, 업비트)

    @Column(name = "symbol", nullable = false)
    private String symbol; // 거래쌍 (e.g. BTC/KRW, BTC/USDT)

    @Column(name = "open", nullable = false, precision = 20, scale = 8)
    private BigDecimal open; // 시가

    @Column(name = "high", nullable = false, precision = 20, scale = 8)
    private BigDecimal high; // 고가

    @Column(name = "low", nullable = false, precision = 20, scale = 8)
    private BigDecimal low; // 저가

    @Column(name = "close", nullable = false, precision = 20, scale = 8)
    private BigDecimal close; // 종가

    @Column(name = "last", nullable = false, precision = 20, scale = 8)
    private BigDecimal last; // 최근 거래 가격

    @Column(name = "previous_close", nullable = false, precision = 20, scale = 8)
    private BigDecimal previousClose; // 전일 종가

    @Column(name = "change", nullable = false, precision = 20, scale = 8)
    private BigDecimal change; // 가격 변동 (종가 - 전일 종가)

    @Column(name = "percentage", nullable = false, precision = 10, scale = 8)
    private BigDecimal percentage; // 가격 변동률 (change / 전일 종가 * 100)

    @Column(name = "base_volume", nullable = false, precision = 20, scale = 8)
    private BigDecimal base_volume; // 기록통화 거래량

    @Column(name = "quote_volume", nullable = false, precision = 20, scale = 8)
    private BigDecimal quote_volume; // 상대통화 거래량
}
