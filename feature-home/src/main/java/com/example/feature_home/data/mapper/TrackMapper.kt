package com.example.feature_home.data.mapper

import com.example.core.network.dto.track.TrackDto
import com.example.feature_home.domain.model.Track

object TrackMapper {
    fun TrackDto.toDomain(): Track {
        return Track(
            trackId = this.trackId,
            trackName = this.trackName
        )
    }

    fun Track.toDto(): TrackDto {
        return TrackDto(
            trackId = this.trackId,
            trackName = this.trackName
        )
    }


}