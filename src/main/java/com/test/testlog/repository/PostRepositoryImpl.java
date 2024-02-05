package com.test.testlog.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.test.testlog.domain.Post;
import com.test.testlog.domain.QPost;
import com.test.testlog.request.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl  implements PostRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(int page) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(10).offset((page -1 )* 10L).orderBy(QPost.post.id.desc()).fetch();
    }

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(postSearch.getSize())
                .offset((postSearch.getOffset()))
                .orderBy(QPost.post.id.desc()).fetch();
    }
}
