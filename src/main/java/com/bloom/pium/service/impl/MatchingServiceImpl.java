package com.bloom.pium.service.impl;


import com.bloom.pium.data.dto.MatchingDto;
import com.bloom.pium.data.dto.MatchingResponseDto;
import com.bloom.pium.data.entity.Board;
import com.bloom.pium.data.entity.BoardMatching;
import com.bloom.pium.data.entity.Matching;
import com.bloom.pium.data.repository.BoardRepository;
import com.bloom.pium.data.repository.MatchingRepository;
import com.bloom.pium.service.MatchingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import java.util.List;
import java.util.Optional;


@Service
public class MatchingServiceImpl implements MatchingService {
    private final Logger LOGGER = LoggerFactory.getLogger(MatchingServiceImpl.class);

    private final MatchingRepository matchingRepository;

    private final BoardRepository boardRepository;

    @Autowired
    public MatchingServiceImpl(MatchingRepository matchingRepository, BoardRepository boardRepository){
        this.matchingRepository = matchingRepository;
        this.boardRepository = boardRepository;
    }


    @Override
    public MatchingResponseDto saveMatching(MatchingDto matchingDto) {
        Matching matching = new Matching();

        matching.setTitle(matchingDto.getTitle());
        matching.setComment(matchingDto.getComment());

        Matching savedMatching = matchingRepository.save(matching);

        MatchingResponseDto matchingResponseDto = new MatchingResponseDto();
        matchingResponseDto.setMatchingId(savedMatching.getMatchingId());
        matchingResponseDto.setTitle(savedMatching.getTitle());
        matchingResponseDto.setComment(savedMatching.getComment());

        matchingResponseDto.setUserId(savedMatching.getUserId());
        matchingResponseDto.setBoardId(savedMatching.getBoardId());

        return matchingResponseDto;
    }

    @Override
    public List<Matching> getMatchingList() {
        return matchingRepository.findAll();
    }

    @Override
    public void toggleParticipation(Long matchingId) {
        // 매칭의 참가 여부 토글 로직 구현
        Matching matching = matchingRepository.findById(matchingId).orElse(null);
        if (matching != null) {
            matching.setParticipate(!matching.isParticipate());
            matchingRepository.save(matching);
        }
    }


    // 마감 관련 (미해결)
    @Override
    public void toggleDeadline(Long matchingId) {
        Optional<Matching> matchingOptional = matchingRepository.findById(matchingId);
        matchingOptional.ifPresent(matching -> {
            if (matching.getBoardId() != null) { // boardId가 null이 아닌 경우에만 처리
                BoardMatching board = matching.getBoardId();
                board.setDeadline(!board.isDeadline());
                boardRepository.save(board); // board 엔티티를 저장하여 변경사항을 반영
            }
        });
    }



    @Override
    public void deleteMatching(Long matchingId) {
        matchingRepository.deleteById(matchingId);
    }


}
