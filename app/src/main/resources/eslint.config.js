// eslint.config.js
import js from "@eslint/js";

export default [
    js.configs.recommended, // Predefined recommended rules
    {
        files: ["src/**/*.js"], // Target files
        rules: {
            "no-unused-vars": "error", // "off" (0), "warn" (1), or "error" (2)
            "semi": ["error", "always"]
        }
    }
];
