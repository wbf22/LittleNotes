package com.example.hibernatepractice.dao.repository;

import com.example.hibernatepractice.dao.model.Note;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends CrudRepository<Note, Long> {

    @Query("SELECT s FROM Note s JOIN s.tags t WHERE t = LOWER(:tag)")
    List<Note> retrieveByTag(@Param("tag") String tag);

    @Query("SELECT s FROM Note s WHERE s.fileLocation = :fileLocation")
    List<Note> retrieveByFileLocation(@Param("fileLocation") String fileLocation);

}
