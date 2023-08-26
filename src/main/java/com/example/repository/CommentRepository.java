package com.example.repository;

import com.example.entity.CommentEntity;
import com.example.mapper.CommentInfoMapperI;
import com.example.mapper.CommentPaginationMapperI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<CommentEntity,String >,
        PagingAndSortingRepository<CommentEntity,String> {
    Optional<CommentEntity>findByIdAndProfileIdAndVisibleTrue(String commentId, Integer profileId);
   @Transactional
   @Modifying
    @Query("update CommentEntity set visible=false where id=:commentId and profileId=:prtId")
    int deleteComment(@Param("commentId") String commentId, @Param("prtId") Integer prtId);
    @Transactional
    @Modifying
    @Query("update CommentEntity set visible=false where replyId=:replyId ")
    int deleteReplyComment(@Param("replyId") String replyId);
    Page<CommentEntity>findByVisibleTrue(Pageable pageable);
    @Query("select c.id as commentId, c.content as content, c.createdDate as createdDate, c.likeCount as likeCount," +
            " c.dislikeCount as dislikeCount, v.id as videoId, v.title as title, pa.id as previewAttachId," +
            " a.id as attachId, a.duration as duration from CommentEntity as c" +
            " inner join c.video as v inner join v.previewAttach as pa inner join v.attach as a" +
            " where  c.profileId=:prtId and c.visible=true")
    Page<CommentPaginationMapperI>paginationByPrt(@Param("prtId")Integer prtId, Pageable pageable);
    @Query("select c.id as commentId, c.content as content, c.createdDate as createdDate, c.likeCount as likeCount," +
            " c.dislikeCount as dislikeCount, v.id as videoId, v.title as title, pa.id as previewAttachId," +
            " a.id as attachId, a.duration as duration from CommentEntity as c" +
            " inner join c.video as v inner join v.previewAttach as pa inner join v.attach as a" +
            " where c.profileId=:prtId and c.visible=true")
    List<CommentPaginationMapperI> commentList(@Param("prtId")Integer profileId);
    @Query("select c.id as commentId, c.content as content, c.createdDate as createdDate, " +
            "c.likeCount as likeCount,  c.dislikeCount as dislikeCount, p.id as profileId, p.name as name," +
            " p.surname as surname, p.attachId as photoId from CommentEntity as c inner join c.profile as p"+
             " where c.videoId=:videoId and c.visible=true")
    List<CommentInfoMapperI> byVideoId(@Param("videoId") String videoId);
 @Query("select c.id as commentId, c.content as content, c.createdDate as createdDate, " +
         "c.likeCount as likeCount,  c.dislikeCount as dislikeCount, p.id as profileId, p.name as name," +
         " p.surname as surname, p.attachId as photoId from CommentEntity as c inner join c.profile as p"+
         " where c.replyId=:replyId and c.visible=true")
 List<CommentInfoMapperI> getReply(@Param("replyId") String commentId);
}
