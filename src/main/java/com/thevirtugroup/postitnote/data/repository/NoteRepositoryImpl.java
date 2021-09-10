package com.thevirtugroup.postitnote.data.repository;

import com.thevirtugroup.postitnote.exceptions.NoteNotFoundException;
import com.thevirtugroup.postitnote.data.entity.NoteEntity;
import com.thevirtugroup.postitnote.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class NoteRepositoryImpl implements NoteRepository {


    private static List<NoteEntity> notes = new ArrayList<>();
    private static Long id = 1L;

    public NoteRepositoryImpl() {
        User defaultUser = new User();
        defaultUser.setId(999L);
        defaultUser.setName("Johnny Tango");
        defaultUser.setPassword("password");
        defaultUser.setUsername("user");
        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setUser(defaultUser);
        noteEntity.setCreatedDate(new Date());
        noteEntity.setUpdatedDate(new Date());
        noteEntity.setId(id++);
        noteEntity.setName("Note 1");
        noteEntity.setText("Note 1 Text");
        notes.add(noteEntity);
    }

    @Override
    public Optional<NoteEntity> findByIdAndUserId(Long id, Long userId) {
        return notes.stream().filter(n -> n.getId().equals(id) && n.getUser().getId().equals(userId)).findFirst();
    }

    @Override
    public List<NoteEntity> findAll() {
        return new ArrayList<>(notes);// returning copy of actual values
    }

    @Override
    public List<NoteEntity> findAllByUserId(Long userId) {
        return notes
                .stream()
                .filter(n -> n.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public NoteEntity saveOrUpdate(NoteEntity noteEntity) {
        NoteEntity currentNote;
        if (noteEntity.getId() == null) {
            currentNote = noteEntity;
            noteEntity.setId(id++);
            noteEntity.setCreatedDate(new Date());
            notes.add(noteEntity);
        } else {
            currentNote = this.findByIdAndUserId(noteEntity.getId(), noteEntity.getUser().getId()).orElseThrow(() -> new NoteNotFoundException(id));
            currentNote.setName(noteEntity.getName());
            currentNote.setText(noteEntity.getText());
            currentNote.setUpdatedDate(new Date());
        }
        return currentNote;

    }


}
