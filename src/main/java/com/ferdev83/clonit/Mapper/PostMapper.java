package com.ferdev83.clonit.Mapper;

import com.ferdev83.clonit.Services.Dtos.PostRequest;
import com.ferdev83.clonit.Services.Dtos.PostResponse;
import com.ferdev83.clonit.Entities.*;
import com.ferdev83.clonit.Repositories.CommentRepository;
import com.ferdev83.clonit.Repositories.VoteRepository;
import com.ferdev83.clonit.Services.AuthenticationService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.marlonlom.utilities.timeago.TimeAgo;

import java.util.Optional;

import static com.ferdev83.clonit.Entities.VoteType.DOWNVOTE;
import static com.ferdev83.clonit.Entities.VoteType.UPVOTE;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    public abstract Post mapToEntity(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authenticationService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                            authenticationService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }

}