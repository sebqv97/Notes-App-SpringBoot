package com.sebqvcoding.springboot.note.database.mapper

import com.sebqvcoding.springboot.note.controller.core.NoteRequest
import com.sebqvcoding.springboot.note.controller.core.NoteResponse
import com.sebqvcoding.springboot.note.database.model.Note
import org.bson.types.ObjectId
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class NoteMapper {
    fun mapToNote(noteRequest: NoteRequest, ownerId: String): Note {
        return with(noteRequest) {
            Note(
                title = title,
                content = content,
                color = color,
                createdAt = Instant.now(),
                ownerId = ObjectId(ownerId)
            )
        }
    }

    infix fun mapToNotesResponse(note: Note): NoteResponse {
        return with(note) {
            NoteResponse(
                title = title,
                content = content,
                color = color,
                createdAt = createdAt,
                id = id.toHexString()
            )
        }
    }
}