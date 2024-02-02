import { html, useState, register } from "./deps.js";

function CommentEditLinks(props) {
    const { comment } = props;
    return html`

`
}

function CringeComment(props) {
    let {
        commentId,
        content, canEdit,
        userId, username,
        parentCommentId, parentCommentUsername,
    } = props;

    const showVote = (did) => did ? "btn-info" : "btn-clear";

    const [rating, setRating] = useState(Number(props.rating));
    const [votedUp, setVotedUp] = useState(props.votedUp === "true");
    const [votedDown, setVotedDown] = useState(props.votedDown === "true");

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

    return html`
<div className="border border-3 border-info rounded-2 my-2 p-2 d-flex flex-row gap-2">
    <div className="d-flex flex-column gap-1 align-items-center">
        <button className="btn ${votedUp ? "btn-info" : "btn-clear"} rounded-pill fw-bold" onClick=${rateUp}>↑</button>
        <strong>${rating > 0 && "+"}${rating}</strong>
        <button className="btn ${votedDown ? "btn-info" : "btn-clear"} rounded-pill fw-bold" onClick=${rateDown}>↓</button>
    </div>
    <div className="col">
        <h6>
            <a href="/users/${userId}">${username}</a>
            ${ parentCommentId && `replied to <a href="#comment-${parentCommentId}">${parentCommentUsername}</a>` }
        </h6>
        <p style=${{wordWrap: "break-word", whiteSpace: "pre-line"}}>${content}</p>
    </div>
</div>
`
}

register(CringeComment, "cringe-comment", [
    "commentId",
    "content",
    "canEdit",
    "rating",
    "userId",
    "username",
    "parentCommentId",
    "parentCommentUsername",
    "votedUp",
    "votedDown",
]);

export default CringeComment;
