package _1danhebojo.coalarm.coalarm_service.domain.user.util;

import java.util.List;
import java.util.Random;

public class NicknameGenerator {
    private static final List<String> ADJECTIVES = List.of(
            "떡상각", "코인왕", "차트러", "매매신", "스마트한", "감각적", "가즈아", "올인러", "단타왕", "분석가", "홀딩러", "강한손", "불장러"
    );

    private static final List<String> ANIMALS = List.of(
            "코알라", "라이언", "토끼", "강아지", "고양이", "부엉이", "펭귄", "곰",
            "늑대", "다람쥐", "호랑이", "사자", "판다", "여우", "수달", "너구리",
            "원숭이", "햄스터", "올빼미", "고슴도치", "말", "사슴", "하이에나"
    );

    private static final Random RANDOM = new Random();

    public static String generateNickname() {
        String adjective = ADJECTIVES.get(RANDOM.nextInt(ADJECTIVES.size()));
        String animal = ANIMALS.get(RANDOM.nextInt(ANIMALS.size()));
        return adjective + " " + animal;
    }
}