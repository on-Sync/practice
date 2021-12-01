package com.example.graphql.resolver;

import java.util.List;

import com.example.graphql.entity.Author;
import com.example.graphql.entity.Post;
import com.example.graphql.repository.PostDao;

import graphql.kickstart.tools.GraphQLResolver;

public class AuthorResolver implements GraphQLResolver<Author> {
    private PostDao postDao;

    public AuthorResolver(PostDao postDao) {
        this.postDao = postDao;
    }

    public List<Post> getPosts(Author author) {
        return postDao.getAuthorPosts(author.getId());
    }
}