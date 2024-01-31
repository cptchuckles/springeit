import { h } from "https://esm.sh/preact";
import { useState, useEffect } from "https://esm.sh/preact/hooks";
import register from "https://esm.sh/preact-custom-element";
import htm from "https://esm.sh/htm";

// Initialize HTM with Preact
const html = htm.bind(h);

export { useState, useEffect, register, html };
