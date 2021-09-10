package com.thevirtugroup.postitnote.data.repository;

import com.thevirtugroup.postitnote.data.entity.NoteEntity;

import java.util.List;
import java.util.Optional;


public interface NoteRepository {

    Optional<NoteEntity> findByIdAndUserId(Long id, Long userId);

    List<NoteEntity> findAll();

    List<NoteEntity> findAllByUserId(Long userId);

    NoteEntity saveOrUpdate(NoteEntity noteEntity);
}
