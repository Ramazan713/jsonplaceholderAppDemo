package com.example.myjsonplaceholderapp.data.repo

import com.example.myjsonplaceholderapp.data.local.service.PostDataSource
import com.example.myjsonplaceholderapp.data.mapper.toPost
import com.example.myjsonplaceholderapp.data.mapper.toPostEntity
import com.example.myjsonplaceholderapp.data.remote.KtorApi
import com.example.myjsonplaceholderapp.domain.models.Post
import com.example.myjsonplaceholderapp.domain.repo.PostRepo

class PostRepoImpl constructor(
    private val ktorApi: KtorApi,
    private val postDataSource: PostDataSource
): PostRepo {

    override suspend fun getPostsByUserId(userId: Int, refresh: Boolean): List<Post> {
        var postEntities = postDataSource.getPostsByUserId(userId)

        if(postEntities.isEmpty() || refresh){
            val postEntitiesFromDto = ktorApi.getPosts(userId).map { it.toPostEntity() }
            postDataSource.insertPosts(postEntitiesFromDto)

            postEntities = postDataSource.getPostsByUserId(userId)
        }
        return postEntities.map { it.toPost() }
    }
}