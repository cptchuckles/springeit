import { html, useState, useEffect, register } from "./deps.js"
import CringeComment from "./CringeComment.js";

function CommentTree({ cringeId }) {
    const [roots, setRoots] = useState([]);
    useEffect(() => {
        fetch(`/api/cringe/${cringeId}/comments`)
            .then(res => {
                if (!res.ok) {
                    throw new Error("can't get comments");
                }
                return res.json();
            })
            .then(comments => {
                for (let i = comments.length; i-- > 0;) {
                    const comment = comments[i];
                    setRoots(otherComments => [comment, ...otherComments]);
                }
            })
            .catch(e => console.error(e));
    }, []);

    return html`
<div>
    ${roots.map(branch => new CringeComment({...branch, commentId: branch.id}))}
</div>
`
}

register(CommentTree, "comment-tree", ["cringeId"]);
