package com.ferdev83.clonit.Controllers;

import com.ferdev83.clonit.Services.Dtos.SubredditDto;
import com.ferdev83.clonit.Services.SubredditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@Slf4j
public class SubRedditController {

    @Autowired
    private SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {
        return new ResponseEntity<SubredditDto>(subredditService.save(subredditDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAll() {
        return new ResponseEntity<>(subredditService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(subredditService.getById(id), HttpStatus.OK);
    }
}
