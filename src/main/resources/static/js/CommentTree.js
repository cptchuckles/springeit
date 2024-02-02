import { html, useState, useEffect, register } from "./deps.js"
import CringeComment from "./CringeComment.js";
import CommentForm from "./CommentForm.js";

function CommentTree({ cringeId, currentUserId }) {
    const [rootComments, setRoots] = useState([]);

    const addRootComment = (comment) => {
        setRoots(roots => [comment, ...roots]);
    }

    const commentForm = new CommentForm({ cringeId, addRootComment });

    useEffect(() => {
        fetch(`/api/cringe/${cringeId}/comments`)
            .then(res => {
                if (!res.ok) {
                    throw new Error("can't get comments");
                }
                return res.json();
            })
            .then(comments => {
                for (let i = 0; i < comments.length; i++) {
                    const comment = comments[i];
                    comment.currentUserId = currentUserId;
                    comment.commentId = comment.id;
                    if (comment.user?.id == currentUserId) {
                        comment.canEdit = true;
                    }
                    setRoots(roots => [comment, ...roots]);
                }
            })
            .catch(e => console.error(e));
    }, []);

    return html`
${commentForm}
<div>
    ${rootComments.map(root => new CringeComment({...root, commentId: root.id}))}
</div>
`
}

register(CommentTree, "comment-tree", ["cringeId", "currentUserId"]);
