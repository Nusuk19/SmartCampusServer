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
import java.util.stream.Collectors;

/**
 * ✅ ВИПРАВЛЕНО: Controller з використанням TagResponse DTO
 */
@RestController
@RequestMapping("/api/tags")
@CrossOrigin(origins = "*")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * GET /api/tags/my - Отримати мої теги
     * ✅ ВИПРАВЛЕНО: Повертає List<TagResponse> замість List<NfcTag>
     */
    @GetMapping("/my")
    public ResponseEntity<List<TagResponse>> getMyTags(
            @AuthenticationPrincipal CurrentUser currentUser) {

        List<NfcTag> tags = tagService.getUserTags(currentUser.getId());

        // Конвертуємо Entity → DTO
        List<TagResponse> response = tags.stream()
                .map(TagResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/tags/virtual - Створити віртуальний тег
     * ✅ ВИПРАВЛЕНО: Повертає TagResponse
     */
    @PostMapping("/virtual")
    public ResponseEntity<TagResponse> createVirtualTag(
            @AuthenticationPrincipal CurrentUser currentUser,
            @RequestBody CreateVirtualTagRequest request) {
        try {
            NfcTag tag = tagService.createVirtualTag(
                    currentUser.getId(),
                    request.getTagUid(),
                    request.getName()
            );

            // Конвертуємо Entity → DTO
            TagResponse response = TagResponse.fromEntity(tag);

            return ResponseEntity.ok(response);
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
     * ✅ ВИПРАВЛЕНО: Повертає TagResponse
     */
    @PatchMapping("/{id}")
    public ResponseEntity<TagResponse> renameTag(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable Long id,
            @RequestBody RenameTagRequest request) {
        try {
            NfcTag tag = tagService.renameTag(id, currentUser.getId(), request.getName());

            // Конвертуємо Entity → DTO
            TagResponse response = TagResponse.fromEntity(tag);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}