package com.thevirtugroup.postitnote.exceptions;

public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException(Long id) {
        super(String.format("Note not found with id : %s", id));
    }
}
