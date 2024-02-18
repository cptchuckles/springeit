import { html, useState, useEffect } from "./deps.js";
import CommentForm from "./CommentForm.js";
import CommentControlLinks from "./CommentControlLinks.js";

function CringeComment(props) {
    let {
        commentId, cringeId, canEdit,
        user, username, currentUserId, currentUserAdmin,
        parentCommentId, parentCommentUsername,
        takeFocus,
    } = props;

    commentId ??= props.id;
    cringeId = Number(cringeId);

    username ??= user?.username ?? "anon";

    const [userId, setUserId] = useState(Number(props.userId ?? props.user?.id) || null);
    const [content, setContent] = useState(props.content ?? "");
    const [replies, setReplies] = useState(props.replies ?? []);

    const userVoteDelta = props.ratings.filter(r => r.user.id == currentUserId).reduce((acc, r) => acc + r.delta, 0);
    const [rating, setRating] = useState(Number(props.totalRating));
    const [votedUp, setVotedUp] = useState(userVoteDelta > 0);
    const [votedDown, setVotedDown] = useState(userVoteDelta < 0);

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
        .then(result => {
            if (! result.ok) {
                throw new Error(`(${result.status}) ${result.statusText}`);
            }
            return result.json()
        })
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

    const editForm = new CommentForm({
        ...comment,
        isEditForm: true,
        closeForm: closeEditForm,
        updateComment: (newContent) => setContent(newContent),
    });

    useEffect(() => {
        if (takeFocus) {
            document.location.hash = "";
            document.location.hash = `comment-${commentId}`;
        }
    }, []);

    return html`
    <div id=${`comment-${commentId}`} className="comment border border-3 border-info rounded-2 mt-2 p-2 d-flex flex-row gap-2">
        ${userId && html`
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
                ${parentCommentId && html` replied to ${parentCommentUsername ? html`<a href="#comment-${parentCommentId}">${parentCommentUsername}</a>` : "deleted comment"}`}
            </strong>
            ${userId && (
                editing
                    ? editForm
                    : html`
                        <p style=${{wordWrap: "break-word", whiteSpace: "pre-line", flex: 1}}>${content}</p>
                        <${CommentControlLinks} ...${{ comment, canEdit, addReply, showEditForm, deleteComment }} />
                    `
                )
            }
        </div>
    </div>
    ${replies?.length > 0 && html`
        <div class="d-flex flex-row justify-content-stretch" style=${{ paddingLeft: "1.25rem" }}>
            <a href="#comment-${commentId}" className="border-start border-5" style=${{ display: "block", width: "1.25rem", borderBottomLeftRadius: "2em" }}></a>
            <div style=${{ flex: "1" }}>
                ${replies.map(reply => html`
                    <${CringeComment}
                        key=${reply.id}
                        ...${reply}
                        ...${{ currentUserId, currentUserAdmin }}
                        parentCommentId=${props.id}
                        parentCommentUsername=${props.user?.username}
                    />`
                )}
            </div>
        </div>
    `}
`}

export default CringeComment;
