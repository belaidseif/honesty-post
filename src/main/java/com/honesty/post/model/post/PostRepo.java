package com.honesty.post.model.post;

import com.honesty.post.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepo extends JpaRepository<Post, UUID>{

    List<Post> findByLocation(String location);
    Optional<Post> findByIdAndIsIgnoredFalse(UUID postUid);

    @Query(value = "select * from post where location = ?1 and is_ignored = false limit(?2)", nativeQuery = true)
    List<Post> findByLocationAndIsIgnoredFalseLimitedTo(String location, int limit);

    @Query(value = "\n" +
            "select * from (select CAST(id as varchar) id, content, created_at createdAt, CAST(user_id as varchar) userId\n" +
            "from post \n" +
            "where id = ?1) as pure_tab\n" +
            ",\n" +
            "(select count(*) as hahaCount  from react\n" +
            "where post_id = ?1\n" +
            "and react = 'HAHA') as haha_tab\n" +
            ",\n" +
            "(select  count(*) as burkCount  from react\n" +
            "where post_id = ?1\n" +
            "and react = 'BURK')as burk_tab\n" +
            ",\n" +
            "(select  count(*) as loveCount  from react\n" +
            "where post_id = ?1\n" +
            "and react = 'LOVE')as love_tab\n" +
            ",\n" +
            "(select  count(*) as sadCount  from react\n" +
            "where post_id = ?1\n" +
            "and react = 'SAD')as sad_tab\n" +
            ",\n" +
            "(select  count(*) as angryCount  from react\n" +
            "where post_id = ?1\n" +
            "and react = 'ANGRY')as angry_tab\n" +
            ",\n" +
            "(select count(*) commentCount\n" +
            "from post p , comment c\n" +
            "where p.id = ?1\n" +
            "and p.id=c.post_id) as comment_count_tab\n" +
            "left join\n" +
            "(select content as mostCommentReacted, CAST(user_id as varchar) mostCommentReactedUser\n" +
            "from comment\n" +
            "where id in (select c.id  from\n" +
            "comment c, react_comment r\n" +
            "where c.post_id = ?1\n" +
            "and c.id = r.comment_id\n" +
            "group by c.id\n" +
            "order by count(*) desc\n" +
            "limit 1)) as most_comment_tab on 1=1" ,nativeQuery = true)
    Optional<CustomPost> findCustomPostById(UUID uuid);
}
