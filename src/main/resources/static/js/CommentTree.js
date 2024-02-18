import { html, useState, useEffect, register } from "./deps.js"
import CringeComment from "./CringeComment.js";
import CommentForm from "./CommentForm.js";

function CommentTree({ cringeId, currentUserId, currentUserAdmin }) {
    const [rootComments, setRootComments] = useState([]);

    const addRootComment = (comment) => {
        const newComments = [comment, ...rootComments];
        document.location.hash = `comment-${comment.id}`;
        setRootComments(newComments);
    }

    useEffect(() => {
        fetch(`/api/cringe/${cringeId}/comments`)
        .then(res => {
            if (!res.ok) {
                throw new Error("can't get comments");
            }
            return res.json();
        })
        .then(comments => {
            const totalComments = [];
            for (let i = comments.length; i-- > 0;) {
                const comment = comments[i];
                comment.currentUserId = currentUserId;
                comment.currentUserAdmin = currentUserAdmin == "true";
                comment.commentId = comment.id;
                totalComments.push(comment);
            }
            setRootComments(totalComments);
        })
        .catch(e => console.error(e));
    }, []);

    useEffect(() => {
        const hash = document.location.hash;
        document.location.hash = "";
        document.location.hash = hash;
    }, [rootComments]);

    return html`
        <${CommentForm} ...${{ cringeId, addRootComment }} />
        <div>
            ${rootComments.map(comment => html`
                <${CringeComment}
                    key=${comment.id}
                    commentId=${comment.id}
                    ...${comment}
                />`
            )}
        </div>
    `
}

register(CommentTree, "comment-tree", ["cringeId", "currentUserId", "currentUserAdmin"]);
