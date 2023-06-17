package com.ferdev83.clonit.Repositories;

import com.ferdev83.clonit.Entities.Comment;
import com.ferdev83.clonit.Entities.Post;
import com.ferdev83.clonit.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
    List<Comment> findAllByUser(User user);
    List<Comment> findByPost(Post post);
}
