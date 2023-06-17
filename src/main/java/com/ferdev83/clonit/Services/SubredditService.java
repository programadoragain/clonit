package com.ferdev83.clonit.Services;

import com.ferdev83.clonit.Dtos.SubredditDto;
import com.ferdev83.clonit.Entities.Subreddit;
import com.ferdev83.clonit.Repositories.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    @Autowired
    private SubredditRepository subredditRepository;
    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit subreddit= subredditRepository.save(mapSubredditRequest(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }
    private Subreddit mapSubredditRequest(SubredditDto subredditDto) {
        return Subreddit.builder().name(subredditDto.getName())
                .description(subredditDto.getDescription())
                .build();
    }
    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }
    private SubredditDto mapToDto(Subreddit subreddit) {
        return new SubredditDto(subreddit.getId(), subreddit.getName(), subreddit.getDescription(), subreddit.getPosts().size());
    }

    public SubredditDto getById(Long id) {
        Subreddit subreddit= subredditRepository.findById(id).orElseThrow(() -> new RuntimeException("No subreddit found with id: " + id.toString()));
        return new SubredditDto(subreddit.getId(), subreddit.getName(), subreddit.getDescription(), subreddit.getPosts().size());
    }
}
