package com.ferdev83.clonit.Services;

import com.ferdev83.clonit.Dtos.PostRequest;
import com.ferdev83.clonit.Dtos.PostResponse;
import com.ferdev83.clonit.Entities.Post;
import com.ferdev83.clonit.Entities.Subreddit;
import com.ferdev83.clonit.Entities.User;
import com.ferdev83.clonit.Mapper.PostMapper;
import com.ferdev83.clonit.Repositories.PostRepository;
import com.ferdev83.clonit.Repositories.SubredditRepository;
import com.ferdev83.clonit.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private SubredditRepository subredditRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostMapper postMapper;

    @Transactional(readOnly = true)
    public void save(PostRequest postRequest) {
        Subreddit subreddit= subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new RuntimeException("Subreddit not exist: " + postRequest.getSubredditName()));
        postRepository.save(postMapper.mapToEntity(postRequest,subreddit,authenticationService.getCurrentUser()));
    }
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post= postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found: " + id.toString()));
        return postMapper.mapToDto(post);
    }
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(post -> postMapper.mapToDto(post))
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostBySubreddit(Long subredditId) {
        Subreddit subreddit= subredditRepository.findById(subredditId)
                .orElseThrow(() -> new RuntimeException("Subreddit not found: " + subredditId.toString()));
        List<Post> post= postRepository.findAllBySubreddit(subreddit);
        return post.stream().map(p -> postMapper.mapToDto(p)).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostByUsername(String username) {
        User user= userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found: " + username));

        return postRepository.findAllByUser(user).stream().map(p -> postMapper.mapToDto(p)).collect(toList());
    }


}
