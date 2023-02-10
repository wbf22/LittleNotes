package com.example.hibernatepractice.dao.repository;

import com.example.hibernatepractice.dao.model.Settings;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SettingsRepository extends CrudRepository<Settings, Long> {


}
