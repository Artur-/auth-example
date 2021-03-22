// prettier.config.js or .prettierrc.js
module.exports = {
  singleQuote: true,
  printWidth: 120,
  overrides: [
    {
      files: '*.java',
      options: {
        tabWidth: 4,
      },
    },
  ],
};
