package com.example.domain.mappers

interface Mapper<FROM,TO> {
    fun map(from:FROM): TO
}