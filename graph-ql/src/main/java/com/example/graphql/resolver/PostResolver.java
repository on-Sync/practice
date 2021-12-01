package com.example.graphql.resolver;

import com.example.graphql.entity.Author;
import com.example.graphql.entity.Post;
import com.example.graphql.repository.AuthorDao;

import graphql.kickstart.tools.GraphQLResolver;

public class PostResolver implements GraphQLResolver<Post> {
    private AuthorDao authorDao;

    public PostResolver(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    public Author getAuthor(Post post) {
        return authorDao.getAuthor(post.getAuthorId()).orElseThrow(RuntimeException::new);
    }
}