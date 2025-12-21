package com.example.core.network.dto.track

import com.example.core.network.dto.Response

class TrackResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()
