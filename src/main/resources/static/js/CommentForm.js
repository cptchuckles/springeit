import { html, useState, register } from "./deps.js";

function CommentForm(props) {
    let {parentComment, cringeId, closeForm, addReply} = props;

    const [content, setContent] = useState("");

    if (parentComment?.props) {
        parentComment = parentComment.props;
    }

    const placeholder = parentComment
        ? `Reply to ${parentComment.username}`
        : "Leave a Comment";

    cringeId ??= parentComment?.cringeId ?? console.error("CommentForm instantiated without a cringeId");

    const resetForm = () => {
        setContent("");
    }

    const submitForm = (ev) => {
        ev.preventDefault();
        const payload = { content };
        if (parentComment) {
            payload.parentCommentId = parentComment.commentId;
        }
        fetch(`/api/cringe/${cringeId}/comments`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        })
        .then(result => {
            if (!result.ok) {
                throw new Error("can't poast");
            }
            return result.json()
        })
        .then(data => {
            if (closeForm) {
                closeForm();
            }
            if (addReply) {
                addReply({
                    ...data,
                    commentId: data.id,
                    cringeId: Number(cringeId),
                    parentCommentUsername: parentComment.username,
                    canEdit: true
                });
            }
            resetForm();
        })
        .catch(e => console.error(e));
    }

    const keydownEventHandler = (ev) => {
        if (ev.ctrlKey && ev.key === "Enter") {
            ev.preventDefault();
            ev.target.form.dispatchEvent(new Event("submit"));
        }
        else if (closeForm && ev.key === "Escape") {
            closeForm();
        }
        setContent(ev.target.value);
    }

    return html`
    <form onSubmit=${submitForm} className=${`form ${parentComment ? "" : "my-3"}`}>
        <textarea
            autoFocus=${ parentComment ? true : undefined }
            id=${parentComment ? `reply-${parentComment.commentId}` : undefined }
            name="content"
            rows="4"
            class="form-control mb-2"
            placeholder="${placeholder}"
            value=${content}
            onKeyDown=${keydownEventHandler}
            onChange=${ev => setContent(ev.target.value)}
        ></textarea>
        <p className="d-flex flex-row gap-2">
            <input type="submit" value="Submit" class="btn btn-success" />
            ${ parentComment && html`
            <button type="button" onClick=${closeForm} class="btn btn-secondary">Cancel</button>
            ` }
        </p>
    </form>
    `;
}

register(CommentForm, "comment-form", ["cringeId"]);
export default CommentForm;
