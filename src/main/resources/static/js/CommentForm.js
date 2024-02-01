import { html, useState, register } from "./deps.js";

function CommentForm({parentCommentId, parentUsername, hiddenElement, cringeId}) {
    const [content, setContent] = useState("");

    const closeForm = () => {
        this.getParent().appendChild(hiddenElement);
        this.destroy();
    };

    const placeholder = parentUsername ? `Reply to ${parentUsername}` : "Leave a Comment";

    const submitForm = async (ev) => {
        ev.preventDefault();
        const response = await fetch(`/api/cringe/${cringeId}/comments`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ content }),
        });
        console.log(response.status, await response.json());
    }

    return html`
    <form onSubmit=${submitForm} class="form my-3">
        <textarea
            name="content"
            id="content"
            rows="4"
            class="form-control mb-2"
            placeholder="${placeholder}"
            value=${content}
            onChange=${(ev) => setContent(ev.target.value)}
        ></textarea>
        <input type="submit" value="Submit" class="btn btn-success" />
        ${ parentCommentId && html`
        <button type="button" onClick=${closeForm} class="btn btn-secondary">Cancel</button>
        <input type="hidden" name="parentCommentId" value=${parentCommentId} />
        ` }
    </form>
    `;
}

register(CommentForm, "comment-form", ["cringeId"]);
