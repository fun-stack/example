module.exports = {
  content: ["target/scala-*/webapp-*/*.js"],
  plugins: [
    require('daisyui'),
  ],
  daisyui: {
    themes: [
      'light',
      'dark'
    ]
  },
};
