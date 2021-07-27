module.exports = {
  purge: ["../../../../src/main/resources/index.html", "../../../../src/**/*.scala"],
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {
      width: {
        120: "30rem",
        144: "36rem",
      },
      typography: (theme) => {
        let headerStyle = {
          '@apply uppercase text-center font-thin text-indigo-500': '',
        };
        return {
          DEFAULT: {
            css: {
              h1: {
                ...headerStyle
              },
              h2: {
                ...headerStyle
              },
              // ...
            },
          },
        }},
    },
    gridTemplateColumns: {
      "fill-40": "repeat(auto-fill, 10rem)",
    },
  },
  variants: {
    extend: {},
  },
  plugins: [require("@tailwindcss/forms"), require("@tailwindcss/typography")],
};
