package com.smartcampus.repository;

import com.smartcampus.model.NfcTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 🆕 Repository для NFC тегів
 */
@Repository
public interface NfcTagRepository extends JpaRepository<NfcTag, Long> {

    /**
     * Знайти тег за UID
     */
    Optional<NfcTag> findByTagUid(String tagUid);

    /**
     * Знайти всі активні теги користувача
     */
    List<NfcTag> findByUserIdAndActive(Long userId, boolean active);

    /**
     * Знайти всі теги користувача (включно з деактивованими)
     */
    List<NfcTag> findByUserId(Long userId);

    /**
     * Перевірити чи UID вже існує
     */
    boolean existsByTagUid(String tagUid);
}
