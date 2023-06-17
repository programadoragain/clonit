package com.ferdev83.clonit.Services;

import com.ferdev83.clonit.Dtos.CommentDto;
import com.ferdev83.clonit.Entities.Comment;
import com.ferdev83.clonit.Entities.Post;
import com.ferdev83.clonit.Entities.User;
import com.ferdev83.clonit.Mapper.CommentMapper;
import com.ferdev83.clonit.Repositories.CommentRepository;
import com.ferdev83.clonit.Repositories.PostRepository;
import com.ferdev83.clonit.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private MailContentBuilder mailContentBuilder;
    @Autowired
    private MailService mailService;
    public void save(CommentDto commentDto) {
        Post post= postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post ID not found: " + commentDto.getPostId()));
        User user= authenticationService.getCurrentUser();

        Comment comment= commentMapper.mapToEntity(commentDto, post, user);

        commentRepository.save(comment);

        String message= mailContentBuilder.build(commentDto.getUsername() + "has comment post: " + comment.getPost().getPostName());
        mailService.sendCommentNotification(message, post.getUser());
    }

    public List<CommentDto> getAllCommentByPost(Long postId) {
        Post post= postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id:" + postId));

        return commentRepository.findAllByPost(post).stream().map(c -> commentMapper.mapToDto(c)).collect(Collectors.toList());
    }

    public List<CommentDto> getAllCommentByUser(String username) {
        User user= userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username:" + username));

        return commentRepository.findAllByUser(user).stream().map(c -> commentMapper.mapToDto(c)).collect(Collectors.toList());
    }
}
