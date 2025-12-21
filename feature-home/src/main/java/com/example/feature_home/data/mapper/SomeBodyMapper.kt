package com.example.feature_home.data.mapper

import com.example.core.network.dto.somebody.SomeBodyDto
import com.example.core.network.dto.track.TrackDto
import com.example.feature_home.domain.model.SomeBody
import com.example.feature_home.domain.model.Track

object SomeBodyMapper {
    fun SomeBodyDto.toDomain(): SomeBody {
        return SomeBody(
            trackId = this.trackId,
            trackName = this.trackName
        )
    }

    fun SomeBody.toDto(): SomeBodyDto {
        return SomeBodyDto(
            trackId = this.trackId,
            trackName = this.trackName
        )
    }
}