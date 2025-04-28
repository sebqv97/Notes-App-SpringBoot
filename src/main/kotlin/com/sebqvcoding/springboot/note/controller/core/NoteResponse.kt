package com.sebqvcoding.springboot.note.controller.core

import java.time.Instant

data class NoteResponse(
    val title: String,
    val content: String,
    val color: Long,
    val createdAt: Instant,
    val id: String
)