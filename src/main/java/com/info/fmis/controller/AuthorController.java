package com.info.fmis.controller;


import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.info.fmis.model.Author;
import com.info.fmis.model.Post;
import com.info.fmis.repository.PostDao;

import java.util.List;

@Controller
public class AuthorController {

    private final PostDao postDao;

    public AuthorController(PostDao postDao) {
        this.postDao = postDao;
    }

    @SchemaMapping
    public List<Post> posts(Author author) {
        return postDao.getAuthorPosts(author.getId());
    }
}