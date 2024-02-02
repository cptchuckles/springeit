package world.grendel.cringeit.dataobjects;

import java.io.Serializable;

/**
 * CommentDTO
 */
public class CommentDTO implements Serializable {
    private String content;
    private Long parentCommentId;

    public CommentDTO() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
}
