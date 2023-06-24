package com.ferdev83.clonit.Controllers;

import com.ferdev83.clonit.Services.Dtos.VoteDto;
import com.ferdev83.clonit.Services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> vote (@RequestBody VoteDto voteDto) {
        voteService.vote(voteDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
