package com.smartcampus.controller;

import com.smartcampus.dto.*;
import com.smartcampus.model.NfcTag;
import com.smartcampus.security.CurrentUser;
import com.smartcampus.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 🆕 Controller для управління NFC тегами
 */
@RestController
@RequestMapping("/api/tags")
@CrossOrigin(origins = "*")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * GET /api/tags/my - Отримати мої теги
     */
    @GetMapping("/my")
    public ResponseEntity<List<NfcTag>> getMyTags(@AuthenticationPrincipal CurrentUser currentUser) {
        List<NfcTag> tags = tagService.getUserTags(currentUser.getId());
        return ResponseEntity.ok(tags);
    }

    /**
     * POST /api/tags/virtual - Створити віртуальний тег
     */
    @PostMapping("/virtual")
    public ResponseEntity<NfcTag> createVirtualTag(
            @AuthenticationPrincipal CurrentUser currentUser,
            @RequestBody CreateVirtualTagRequest request) {
        try {
            NfcTag tag = tagService.createVirtualTag(
                    currentUser.getId(),
                    request.getTagUid(),
                    request.getName()
            );
            return ResponseEntity.ok(tag);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE /api/tags/{id} - Видалити тег
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable Long id) {
        try {
            tagService.deleteTag(id, currentUser.getId());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PATCH /api/tags/{id} - Перейменувати тег
     */
    @PatchMapping("/{id}")
    public ResponseEntity<NfcTag> renameTag(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable Long id,
            @RequestBody RenameTagRequest request) {
        try {
            NfcTag tag = tagService.renameTag(id, currentUser.getId(), request.getName());
            return ResponseEntity.ok(tag);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}