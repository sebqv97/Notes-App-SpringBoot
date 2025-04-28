package com.sebqvcoding.springboot.note.controller.core

data class NoteRequest(
    val title: String,
    val content: String,
    val color: Long,
    val id: String?
)
