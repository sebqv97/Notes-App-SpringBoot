package com.sebqvcoding.springboot.note.controller

import com.sebqvcoding.springboot.note.controller.core.NoteRequest
import com.sebqvcoding.springboot.note.controller.core.NoteResponse
import com.sebqvcoding.springboot.note.database.mapper.NoteMapper
import com.sebqvcoding.springboot.note.database.repository.NotesRepository
import jakarta.validation.Valid
import org.bson.types.ObjectId
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException

@RestController
@RequestMapping("/notes")
class NotesController constructor(
    private val notesRepository: NotesRepository,
    private val noteMapper: NoteMapper
) {

    @PostMapping
    fun saveNote(@RequestBody @Valid body: NoteRequest): NoteResponse {
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        val savedNote = notesRepository.save(noteMapper.mapToNote(body, ownerId))
        return noteMapper.mapToNotesResponse(savedNote)
    }

    @GetMapping
    fun findByOwnerId(): List<NoteResponse> {
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        return notesRepository.findByOwnerId(ownerId = ObjectId(ownerId))
            .map { noteMapper.mapToNotesResponse(it) }
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteById(@PathVariable id: String) {
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        val note = notesRepository.findById(ObjectId(id)).orElseThrow {
            throw IllegalArgumentException("Note not found")
        }
        if (note.ownerId.toHexString() == ownerId)
            notesRepository.deleteById(ObjectId(id))
    }
}
