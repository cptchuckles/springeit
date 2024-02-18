import { html, useState, register } from "./deps.js";

class UnauthorizedException extends Error {
    constructor(msg) {
        super(msg);
    }
}

function CommentForm(props) {
    let {
        cringeId,
        parentComment,
        closeForm,
        addReply,
        updateComment,
        isEditForm,
        addRootComment
    } = props;

    const [content, setContent] = useState(props.content ?? "");

    parentComment = parentComment?.props ?? parentComment;

    const placeholder = parentComment
        ? `Reply to ${parentComment.username}`
        : "Leave a Comment";

    const buttonTitle = closeForm ?
        (isEditForm ? "Save" : "Add Reply")
        : "Add Comment";

    const buttonColor = isEditForm ? "btn-primary" : "btn-success";

    const resetForm = () => setContent("");

    const submitForm = (ev) => {
        ev.preventDefault();
        const payload = { content };
        if (parentComment) {
            payload.parentCommentId = parentComment.commentId;
        }

        const uri = isEditForm
            ? `/api/comments/${props.commentId}`
            : `/api/cringe/${cringeId}/comments`;

        fetch(uri, {
            method: isEditForm ? "PUT" : "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        })
        .then(result => {
            if (!result.ok) {
                if (result.status === 403) {
                    throw new UnauthorizedException("Unauthorized");
                }
                throw new Error("can't poast");
            }
            return result.json()
        })
        .then(data => {
            resetForm();
            if (addReply) {
                addReply({
                    ...data,
                    commentId: data.id,
                    cringeId: Number(cringeId),
                    parentCommentUsername: parentComment.username,
                    canEdit: true,
                    takeFocus: true,
                });
            }
            else if (addRootComment) {
                addRootComment({
                    ...data,
                    commentId: data.id,
                    cringeId: Number(cringeId),
                    canEdit: true,
                    takeFocus: true,
                });
            }
            else if (updateComment) {
                updateComment(data.content);
                setContent(data.content);
            }
            if (closeForm) {
                closeForm();
            }
        })
        .catch(e => {
            console.error(e);
            if (e instanceof UnauthorizedException) {
                window.location.pathname = "/logout";
            }
        });
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
            autoFocus=${parentComment ? true : undefined}
            id=${parentComment ? `reply-${parentComment.commentId}` : undefined}
            name="content"
            rows=${Math.max(4, content.split('\n').length)}
            className="form-control mb-2 border ${closeForm ? "border-1 border-dark" : "border-5 border-primary"}"
            placeholder=${placeholder}
            value=${content}
            onKeyDown=${keydownEventHandler}
            onChange=${ev => setContent(ev.target.value)}
        ></textarea>
        <p className="d-flex flex-row gap-2">
            <input type="submit" value=${buttonTitle} class="btn ${buttonColor}" />
            ${ closeForm && html`
            <button type="button" onClick=${closeForm} class="btn btn-secondary">Cancel</button>
            ` }
        </p>
    </form>
    `;
}

register(CommentForm, "comment-form", ["cringeId"]);
export default CommentForm;
