package com.ferdev83.clonit.Controllers;

import com.ferdev83.clonit.Dtos.CommentDto;
import com.ferdev83.clonit.Services.CommentService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentDto commentDto) {
        commentService.save(commentDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentDto>> getAllCommentByPost(@RequestParam Long postId) {
         return new ResponseEntity<List<CommentDto>>(commentService.getAllCommentByPost(postId), HttpStatus.OK);
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<CommentDto>> getAllCommentByUser(@RequestParam String username) {
        return new ResponseEntity<List<CommentDto>>(commentService.getAllCommentByUser(username), HttpStatus.OK);
    }
}
