package com.sebqvcoding.springboot.note.database.repository

import com.sebqvcoding.springboot.note.database.model.Note
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface NotesRepository: MongoRepository<Note, ObjectId> {
    fun findByOwnerId(ownerId: ObjectId):List<Note>
}