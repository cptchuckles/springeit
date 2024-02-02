import { html, useState, register } from "./deps.js";
import CommentForm from "./CommentForm.js";

function CommentEditLinks(props) {
    const { comment, addReply, canEdit, showEditForm, deleteComment } = props;

    const [showForm, setShowForm] = useState(false);
    const form = new CommentForm({
        parentComment: comment,
        cringeId: comment.cringeId,
        addReply: addReply,
        closeForm: () => setShowForm(false)
    });

    const showReplyForm = (ev) => {
        ev.preventDefault();
        setShowForm(true);
    }

    return showForm ? form : html`
<span className="d-flex flex-row gap-2 justify-content-end" style=${{fontSize: "0.6em"}}>
    <a style=${{cursor: "pointer"}} onClick=${showReplyForm} className="link-dark fw-bold">Reply</a>
    ${ canEdit && html`
        <a style=${{cursor: "pointer"}} onClick=${showEditForm} className="link-dark fw-bold">Edit</a>
        <a style=${{cursor: "pointer"}} onClick=${deleteComment} className="link-dark fw-bold">Delete</a>
    `}
</span>
`}

function CringeComment(props) {
    let {
        commentId, cringeId, canEdit,
        user, username,
        parentCommentId, parentCommentUsername,
    } = props;

    cringeId = Number(cringeId);

    username ??= user?.username ?? "anon";

    const [userId, setUserId] = useState(Number(props.userId ?? props.user?.id) || null);
    const [content, setContent] = useState(props.content ?? "");
    const [replies, setReplies] = useState(props.replies ?? []);
    const [rating, setRating] = useState(Number(props.totalRating));
    const [votedUp, setVotedUp] = useState(props.votedUp === "true");
    const [votedDown, setVotedDown] = useState(props.votedDown === "true");

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
    const links = new CommentEditLinks({ comment, addReply, canEdit, showEditForm, deleteComment });

    const editForm = new CommentForm({
        ...comment,
        isEditForm: true,
        closeForm: closeEditForm,
        updateComment: (newContent) => setContent(newContent),
    });

    return html`
<div id=${`comment-${commentId}`} className="comment border border-3 border-info rounded-2 my-2 p-2 d-flex flex-row gap-2">
    ${ userId && html`
    <div className="d-flex flex-column gap-1 align-items-center">
        <button className="btn ${votedUp ? "btn-info" : "btn-clear"} rounded-pill fw-bold" onClick=${rateUp}>↑</button>
        <strong>${rating > 0 && "+"}${rating}</strong>
        <button className="btn ${votedDown ? "btn-info" : "btn-clear"} rounded-pill fw-bold" onClick=${rateDown}>↓</button>
    </div>
    `}
    <div className="col d-flex flex-column">
        <strong style=${{fontSize: ".8em"}} className="mb-2">
            ${userId ? html`<a href="/users/${userId}">${username}</a>` : html`<em>deleted comment</em>`}
            ${ parentCommentId && html` replied to ${parentCommentUsername ? html`<a href="#comment-${parentCommentId}">${parentCommentUsername}</a>` : "deleted comment"}` }
        </strong>
        ${ userId && (editing ? editForm : html`
        <p style=${{wordWrap: "break-word", whiteSpace: "pre-line", flex: 1}}>${content}</p>
        ${links}
        `)}
    </div>
</div>
    ${replies?.length > 0 && html`
    <div className="border-start border-5" style=${{marginLeft: "1.25rem", paddingLeft: "1.25rem", borderBottomLeftRadius: "2em"}}>
        ${replies.map(reply => new CringeComment({...reply}))}
    </div>
    `}
`}

register(CringeComment, "cringe-comment", [
    "commentId",
    "cringeId",
    "content",
    "canEdit",
    "totalRating",
    "userId",
    "username",
    "parentCommentId",
    "parentCommentUsername",
    "votedUp",
    "votedDown",
    "replies",
]);

export default CringeComment;
