package com.ferdev83.clonit.Repositories;

import com.ferdev83.clonit.Entities.Post;
import com.ferdev83.clonit.Entities.User;
import com.ferdev83.clonit.Entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
