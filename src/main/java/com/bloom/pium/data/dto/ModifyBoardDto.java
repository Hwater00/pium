package com.bloom.pium.data.dto;

public class ModifyBoardDto {

    private Long boardId;
    private String title;
    private String content;

    public ModifyBoardDto(Long boardId, String title, String content) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
    }
    public ModifyBoardDto() {

    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
