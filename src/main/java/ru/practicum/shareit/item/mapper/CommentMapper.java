package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentMessageDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static CommentResponseDto toDto(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(comment.getId());
        commentResponseDto.setText(comment.getText());
        commentResponseDto.setAuthorName(comment.getAuthor().getName());
        commentResponseDto.setCreated(comment.getCreated());
        return commentResponseDto;
    }

    public static Comment toEntity(CommentMessageDto dto) {
        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setText(dto.getText());
        comment.setCreated(LocalDateTime.now());
        return comment;
    }
}
