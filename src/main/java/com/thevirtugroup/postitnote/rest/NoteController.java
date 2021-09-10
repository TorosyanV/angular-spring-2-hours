package com.thevirtugroup.postitnote.rest;

import com.thevirtugroup.postitnote.dto.NoteCreateDto;
import com.thevirtugroup.postitnote.dto.NoteDto;
import com.thevirtugroup.postitnote.dto.NoteUpdateDto;
import com.thevirtugroup.postitnote.model.NoteCreateModel;
import com.thevirtugroup.postitnote.model.NoteModel;
import com.thevirtugroup.postitnote.security.SecurityUserWrapper;
import com.thevirtugroup.postitnote.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public NoteDto create(@RequestBody NoteCreateModel note, @AuthenticationPrincipal SecurityUserWrapper userWrapper) {
        NoteCreateDto noteCreateDto = new NoteCreateDto();
        noteCreateDto.setName(note.getName());
        noteCreateDto.setText(note.getText());
        return noteService.create(noteCreateDto, userWrapper.getId());

    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    public NoteDto update(@PathVariable Long id, NoteCreateModel note, @AuthenticationPrincipal SecurityUserWrapper userWrapper) {
        NoteUpdateDto noteUpdateDto = new NoteUpdateDto();
        noteUpdateDto.setId(id);
        noteUpdateDto.setName(note.getName());
        noteUpdateDto.setText(note.getText());
        return noteService.update(noteUpdateDto, userWrapper.getId());
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<NoteDto> getAll(@AuthenticationPrincipal SecurityUserWrapper userWrapper) {
        return noteService.getAllByUserId(userWrapper.getId());
    }

}
