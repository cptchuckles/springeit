import { html, useState, useEffect } from "./deps.js";
import CommentForm from "./CommentForm.js";

function CommentControlLinks(props) {
    const { comment, addReply, canEdit, showEditForm, deleteComment } = props;

    const [showForm, setShowForm] = useState(false);
    const [isDeleting, setIsDeleting] = useState(false);

    const showReplyForm = () => setShowForm(true);
    const showDeletePrompt = () => setIsDeleting(true);
    const hideDeletePrompt = () => setIsDeleting(false);

    const form = new CommentForm({
        parentComment: comment,
        cringeId: comment.cringeId,
        addReply: addReply,
        closeForm: () => setShowForm(false)
    });

    const deletePrompt = html`
    <div style="text-align: center">
        <h4>Delete this comment?</h4>
        <p>
            <button className="btn btn-danger mx-2" onClick=${deleteComment}>Delete</button>
            <button className="btn btn-secondary mx-2" onClick=${hideDeletePrompt}>Cancel</button>
        </p>
    </div>
    `;

    const links = html`
    <span className="d-flex flex-row gap-2 justify-content-end" style=${{fontSize: "0.8em"}}>
        <a style=${{cursor: "pointer"}} onClick=${showReplyForm} className="link-dark fw-bold">Reply</a>
        ${ canEdit && html`
            <a style=${{cursor: "pointer"}} onClick=${showEditForm} className="link-dark fw-bold">Edit</a>
            <a style=${{cursor: "pointer"}} onClick=${showDeletePrompt} className="link-dark fw-bold">Delete</a>
        `}
    </span>
    `;

    if (showForm) {
        return form;
    }
    else if (isDeleting) {
        return deletePrompt;
    }
    else {
        return links;
    }
}

function CringeComment(props) {
    let {
        commentId, cringeId, currentUserId, currentUserAdmin, canEdit,
        user, username,
        parentCommentId, parentCommentUsername,
        takeFocus,
    } = props;

    commentId ??= props.id;
    cringeId = Number(cringeId);

    username ??= user?.username ?? "anon";

    const userVoteDelta = () => {
        return props.ratings.filter(r => r.user.id == currentUserId).reduce((acc, r) => acc + r.delta, 0);
    }

    const [userId, setUserId] = useState(Number(props.userId ?? props.user?.id) || null);
    const [content, setContent] = useState(props.content ?? "");
    const [replies, setReplies] = useState(props.replies ?? []);
    const [rating, setRating] = useState(Number(props.totalRating));
    const [votedUp, setVotedUp] = useState(userVoteDelta() > 0);
    const [votedDown, setVotedDown] = useState(userVoteDelta() < 0);

    canEdit ||= ((currentUserId == user?.id) || currentUserAdmin)
    const [editing, setEditing] = useState(false);
    const showEditForm = () => setEditing(true);
    const closeEditForm = () => setEditing(false);

    const rate = (delta, success) => {
        fetch(`/api/comments/${commentId}/rate`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({delta})
        })
        .then(result => result.json())
        .then(success)
        .catch(e => console.log("comment rating failed:", e));
    }

    const rateUp = () => rate(1, total => {
        setRating(total);
        setVotedUp(up => !up);
        setVotedDown(false);
    });
    const rateDown = () => rate(-1, total => {
        setRating(total);
        setVotedUp(false);
        setVotedDown(down => !down);
    });

    const addReply = (reply) => setReplies(oldReplies => [...oldReplies, reply]);

    const deleteComment = () => {
        fetch(`/api/comments/${commentId}`, {
            method: "DELETE"
        }).then(res => {
            if (!res.ok) {
                throw new Error("comment wasn't deleted");
            }
            setUserId(null);
            setContent("");
        }).catch(e => console.error(e));
    }

    const comment = {...props, commentId, user, userId, cringeId, username};
    const commentControlLinks = new CommentControlLinks({ comment, addReply, canEdit, showEditForm, deleteComment });

    const editForm = new CommentForm({
        ...comment,
        isEditForm: true,
        closeForm: closeEditForm,
        updateComment: (newContent) => setContent(newContent),
    });

    useEffect(() => {
        if (takeFocus) {
            document.location.hash = `comment-${props.id}`;
        }
    }, []);

    return html`
    <div id=${`comment-${commentId}`} className="comment border border-3 border-info rounded-2 mt-2 p-2 d-flex flex-row gap-2">
        ${ userId && html`
        <div className="d-flex flex-column gap-1 align-items-center">
            <button className="btn ${votedUp ? "btn-info" : "btn-clear"} rounded-pill fw-bold" onClick=${rateUp}>
                <span class="vote vote-up"></span>
            </button>
            <strong>${rating > 0 && "+"}${rating}</strong>
            <button className="btn ${votedDown ? "btn-info" : "btn-clear"} rounded-pill fw-bold" onClick=${rateDown}>
                <span class="vote vote-down"></span>
            </button>
        </div>
        `}
        <div className="col d-flex flex-column">
            <strong style=${{fontSize: ".8em"}} className="mb-2">
                ${userId ? html`<a href="/users/${userId}">${username}</a>` : html`<em>deleted comment</em>`}
                ${ parentCommentId && html` replied to ${parentCommentUsername ? html`<a href="#comment-${parentCommentId}">${parentCommentUsername}</a>` : "deleted comment"}` }
            </strong>
            ${ userId && (
                editing
                    ? editForm
                    : html`
                        <p style=${{wordWrap: "break-word", whiteSpace: "pre-line", flex: 1}}>${content}</p>
                        ${commentControlLinks}
                    `
                )
            }
        </div>
    </div>
    ${replies?.length > 0 && html`
        <div class="d-flex flex-row justify-content-stretch" style=${{paddingLeft: "1.25rem"}}>
            <a href="#comment-${commentId}" className="border-start border-5" style=${{display: "block", width: "1.25rem", borderBottomLeftRadius: "2em"}}></a>
            <div style=${{flex: "1"}}>
                ${replies.map(reply => html`
                    <${CringeComment}
                        key=${reply.id}
                        ...${reply}
                        ...${{currentUserId, currentUserAdmin}}
                        parentCommentId=${props.id}
                        parentCommentUsername=${props.user?.username}
                    />`
                )}
            </div>
        </div>
    `}
`}

export default CringeComment;
