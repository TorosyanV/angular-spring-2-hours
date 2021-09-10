package com.thevirtugroup.postitnote.service;

import com.thevirtugroup.postitnote.data.entity.NoteEntity;
import com.thevirtugroup.postitnote.data.repository.NoteRepository;
import com.thevirtugroup.postitnote.data.repository.UserRepository;
import com.thevirtugroup.postitnote.dto.NoteCreateDto;
import com.thevirtugroup.postitnote.dto.NoteDto;
import com.thevirtugroup.postitnote.dto.NoteUpdateDto;
import com.thevirtugroup.postitnote.exceptions.NoteNotFoundException;
import com.thevirtugroup.postitnote.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private static final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<NoteDto> getAllByUserId(Long userId) {
        return noteRepository.findAllByUserId(userId).stream().map(n -> {
            NoteDto noteDto = new NoteDto();
            noteDto.setId(n.getId());
            noteDto.setName(n.getName());
            noteDto.setText(n.getText());
            noteDto.setCreatedDate(n.getCreatedDate());
            noteDto.setUpdatedDate(n.getUpdatedDate());
            return noteDto;
        }).collect(Collectors.toList());
    }

    @Override
    public NoteDto create(NoteCreateDto note, Long userId) {
        User user = Optional.ofNullable(userRepository.findById(userId)).orElseThrow(() -> new IllegalArgumentException("User id not found " + userId));
        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setName(note.getName());
        noteEntity.setText(note.getText());
        noteEntity.setUser(user);
        NoteEntity created = noteRepository.saveOrUpdate(noteEntity);
        logger.debug("note created {}", created);
        return toDto(created);
    }

    @Override
    public NoteDto update(NoteUpdateDto note, Long userId) {
        NoteEntity noteEntity = noteRepository.findByIdAndUserId(note.getId(), userId).orElseThrow(() -> new NoteNotFoundException(note.getId()));
        NoteEntity updated = noteRepository.saveOrUpdate(noteEntity);
        logger.debug("note updated {}", updated);
        return toDto(updated);
    }

    private NoteDto toDto(NoteEntity entity) {
        NoteDto noteDto = new NoteDto();
        noteDto.setId(entity.getId());
        noteDto.setName(entity.getName());
        noteDto.setText(entity.getText());
        noteDto.setCreatedDate(entity.getCreatedDate());
        noteDto.setUpdatedDate(entity.getUpdatedDate());
        return noteDto;
    }
}
