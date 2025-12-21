package com.example.core.network.dto

class TrackResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()
