package com.sebqvcoding.springboot.note.controller.core

import jakarta.validation.constraints.NotBlank

data class NoteRequest(
    @field: NotBlank(message = "Title cannot be blank")
    val title: String,
    val content: String,
    val color: Long,
    val id: String?
)
