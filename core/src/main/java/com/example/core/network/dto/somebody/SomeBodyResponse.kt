package com.example.core.network.dto.somebody

import com.example.core.network.dto.Response

class SomeBodyResponse(
    val resultCount: Int,
    val results: List<SomeBodyDto>
) : Response()
