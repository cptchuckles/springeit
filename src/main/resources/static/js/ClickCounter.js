import { html, useState, register } from "./deps.js";

function ClickCounter(props) {
    const [count, setCount] = useState(0);
    const increment = () => setCount(count + 1);
    const decrement = () => setCount(count - 1);

    return html`
        <div className="card p-3">
            <div className="card-heading">
                <h3>Count: ${count}</h3>
            </div>
            <div className="card-body">
                <button className="btn btn-primary" onClick=${increment}>+</button>
                <button className="btn btn-secondary" onClick=${decrement}>-</button>
            </div>
        </div>
    `;
}

register(ClickCounter, "click-counter");
