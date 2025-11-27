package com.smartcampus.service;

import com.smartcampus.model.NfcTag;
import com.smartcampus.repository.NfcTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 🆕 Сервіс управління NFC тегами
 */
@Service
public class TagService {

    @Autowired
    private NfcTagRepository tagRepository;

    /**
     * Отримати всі теги користувача
     */
    public List<NfcTag> getUserTags(Long userId) {
        return tagRepository.findByUserIdAndActive(userId, true);
    }

    /**
     * Створити віртуальний тег
     */
    public NfcTag createVirtualTag(Long userId, String tagUid, String name) {
        // Перевірити чи UID вже існує
        if (tagRepository.findByTagUid(tagUid).isPresent()) {
            throw new IllegalArgumentException("Тег з таким UID вже існує");
        }

        NfcTag tag = new NfcTag();
        tag.setTagUid(tagUid);
        tag.setUserId(userId);
        tag.setTagType("VIRTUAL");
        tag.setName(name);
        tag.setActive(true);
        tag.setCreatedAt(LocalDateTime.now());

        return tagRepository.save(tag);
    }

    /**
     * Видалити (деактивувати) тег
     */
    public void deleteTag(Long tagId, Long userId) {
        NfcTag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new IllegalArgumentException("Тег не знайдено"));

        // Перевірити власника
        if (!tag.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Ви не власник цього тегу");
        }

        // Деактивувати замість видалення (для історії)
        tag.setActive(false);
        tagRepository.save(tag);
    }

    /**
     * Перейменувати тег
     */
    public NfcTag renameTag(Long tagId, Long userId, String newName) {
        NfcTag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new IllegalArgumentException("Тег не знайдено"));

        // Перевірити власника
        if (!tag.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Ви не власник цього тегу");
        }

        tag.setName(newName);
        return tagRepository.save(tag);
    }

    /**
     * Оновити lastUsedAt
     */
    public void updateLastUsed(Long tagId) {
        NfcTag tag = tagRepository.findById(tagId).orElse(null);
        if (tag != null) {
            tag.setLastUsedAt(LocalDateTime.now());
            tagRepository.save(tag);
        }
    }
}