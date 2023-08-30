package com.example.repository;

import com.example.entity.CommentLikeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends CrudRepository<CommentLikeEntity,String > {
    Optional<CommentLikeEntity>findByProfileIdAndCommentId(Integer profileId,String commentId);

    List<CommentLikeEntity>findByProfileId(Integer prtId);
}
