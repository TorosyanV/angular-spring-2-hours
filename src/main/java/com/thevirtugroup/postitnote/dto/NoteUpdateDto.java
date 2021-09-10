package com.thevirtugroup.postitnote.dto;

public class NoteUpdateDto extends NoteCreateDto {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
