package com.sebqvcoding.springboot.note.controller

import com.sebqvcoding.springboot.note.controller.core.NoteRequest
import com.sebqvcoding.springboot.note.controller.core.NoteResponse
import com.sebqvcoding.springboot.note.database.mapper.NoteMapper
import com.sebqvcoding.springboot.note.database.repository.NotesRepository
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/notes")
class NotesController constructor(
    private val notesRepository: NotesRepository,
    private val noteMapper: NoteMapper
) {

    @PostMapping
    fun saveNote(@RequestBody body: NoteRequest): NoteResponse {
        val savedNote = notesRepository.save(noteMapper.mapToNote(body))
        return noteMapper.mapToNotesResponse(savedNote)
    }

    @GetMapping
    fun findByOwnerId(
        @RequestParam(name = "ownerId", required = true) ownerId: String
    ): List<NoteResponse> {
        return notesRepository.findByOwnerId(ownerId = ObjectId(ownerId))
            .map { noteMapper.mapToNotesResponse(it) }
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteById(@PathVariable id:String){
        notesRepository.deleteById(ObjectId(id))
    }
}
