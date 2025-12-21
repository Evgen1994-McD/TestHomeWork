package com.example.home.data

import com.example.core.network.NetworkClient
import com.example.core.network.dto.somebody.SomeBodyResponse
import com.example.core.network.dto.somebody.SomebodySearchRequest
import com.example.core.network.dto.track.TrackResponse
import com.example.core.network.dto.track.TrackSearchRequest
import com.example.feature_home.data.mapper.SomeBodyMapper.toDomain
import com.example.feature_home.data.mapper.TrackMapper.toDomain
import com.example.feature_home.domain.model.SomeBody
import com.example.feature_home.domain.model.Track
import com.example.home.domain.HomeRepository

class HomeRepositoryImpl(
    private val trackNetworkClient: NetworkClient,
    private val somebodyNetworkClient: NetworkClient
):HomeRepository {

    override suspend fun searchTracks(expression:String):List<Track>{
        val response = trackNetworkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode){
            200-> {
                with(response as TrackResponse){
                    val data = results.map { it->
                        it.toDomain()
                    }
                    return data
                }
            }
            else -> return emptyList()

        }
    }


    override suspend fun searchSomeBody(expression:String):List<SomeBody>{
        val response = somebodyNetworkClient.doRequest(SomebodySearchRequest(expression))
        when (response.resultCode){
            200-> {
                with(response as SomeBodyResponse){
                    val data = results.map { it->
                        it.toDomain()
                    }
                    return data
                }
            }
            else -> return emptyList()

        }
    }

}
