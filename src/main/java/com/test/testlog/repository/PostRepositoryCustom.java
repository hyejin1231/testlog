package com.test.testlog.repository;

import com.test.testlog.domain.Post;
import com.test.testlog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(int page);

    List<Post> getList(PostSearch postSearch);
}
