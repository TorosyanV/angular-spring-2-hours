package com.thevirtugroup.postitnote.service;

import com.thevirtugroup.postitnote.dto.NoteCreateDto;
import com.thevirtugroup.postitnote.dto.NoteDto;
import com.thevirtugroup.postitnote.dto.NoteUpdateDto;

import java.util.List;
import java.util.Optional;

public interface NoteService {
//    Optional<NoteDto> getById(Long id);
//
//    List<NoteDto> getAll();

    List<NoteDto> getAllByUserId(Long userId);

    NoteDto create(NoteCreateDto note, Long userId);

    NoteDto update(NoteUpdateDto note, Long userId);
}
