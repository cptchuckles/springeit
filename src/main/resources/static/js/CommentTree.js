import { html, useState, useEffect, register } from "./deps.js"
import CringeComment from "./CringeComment.js";
import CommentForm from "./CommentForm.js";

function CommentTree({ cringeId, currentUserId }) {
    const [rootComments, setRootComments] = useState([]);

    const addRootComment = (comment) => {
        const newComments = [comment, ...rootComments];
        document.location.hash = `comment-${comment.id}`;
        setRootComments(newComments);
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
                const totalComments = [];
                for (let i = comments.length; i-- > 0;) {
                    const comment = comments[i];
                    comment.currentUserId = currentUserId;
                    comment.commentId = comment.id;
                    if (comment.user?.id == currentUserId) {
                        comment.canEdit = true;
                    }
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
${commentForm}
<div>
    ${rootComments.map(root => html`<${CringeComment} key=${root.id} ...${{...root, commentId: root.id}} />`)}
</div>
`
}

register(CommentTree, "comment-tree", ["cringeId", "currentUserId"]);
