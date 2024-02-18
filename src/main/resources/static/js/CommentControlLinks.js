import { html, useState } from "./deps.js";
import CommentForm from "./CommentForm.js";

export function CommentControlLinks({ comment, canEdit, addReply, showEditForm, deleteComment }) {
    const [isReplying, setIsReplying] = useState(false);
    const [isDeleting, setIsDeleting] = useState(false);

    if (isReplying) {
        return html`
        <${CommentForm}
            parentComment=${comment}
            cringeId=${comment.cringeId}
            addReply=${addReply}
            closeForm=${() => setIsReplying(false)}
        />`;
    }
    else if (isDeleting) {
        return html`
        <div className="bg-warning rounded-pill" style="text-align: center">
            <h4>Delete this comment?</h4>
            <p>
                <button className="btn btn-danger mx-2" onClick=${deleteComment}>Delete</button>
                <button className="btn btn-secondary mx-2" onClick=${() => setIsDeleting(false)}>Cancel</button>
            </p>
        </div>`;
    }
    else {
        return html`
        <span className="d-flex flex-row gap-2 justify-content-end" style=${{ fontSize: "0.8em" }}>
            <a style=${{ cursor: "pointer" }} onClick=${() => setIsReplying(true)} className="link-dark fw-bold">Reply</a>
            ${canEdit && html`
                <a style=${{ cursor: "pointer" }} onClick=${showEditForm} className="link-dark fw-bold">Edit</a>
                <a style=${{ cursor: "pointer" }} onClick=${() => setIsDeleting(true)} className="link-dark fw-bold">Delete</a>
            `}
        </span>`;
    }
}

export default CommentControlLinks;
