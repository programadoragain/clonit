package com.ferdev83.clonit.Entities;

import jakarta.persistence.*;
import lombok.Builder;
import org.antlr.v4.runtime.misc.NotNull;

@Builder
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;
    private VoteType voteType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    public Integer getVoteType() {
        return voteType.getDirection();
    }
}
