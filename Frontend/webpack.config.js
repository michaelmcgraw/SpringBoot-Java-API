const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
  optimization: {
    usedExports: true
  },
  entry: {
    examplePage: path.resolve(__dirname, 'src', 'pages', 'examplePage.js',),
    home : path.resolve(__dirname, 'src', 'pages', 'home.js'),
    createTask : path.resolve(__dirname, 'src', 'pages', 'createTask.js'),
    failedTasks : path.resolve(__dirname, 'src', 'pages', 'failedTasks.js')
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
  },
  devServer: {
    https: false,
    port: 8080,
    open: true,
    //proxy is what tells your frontend where to find the backend and what requests to send there
    //if you  notice in the example we are sending all requests that start with /example to
    //http://localhost:5001 which is where the backend is, when sent to the backend it will become
    //http://localhost:5001/exemple/...
    //for example if you sent the request /example/bob to the backend, it will be converted into
    //http://localhost:5001/example/bob and sent to the backend that way.
    //uncomment the following proxy section to make the example work
    proxy: [
          {
            context: [
              '/example',
            ],
            target: 'http://localhost:5001'
          }
        ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/index.html',
      filename: 'index.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/plannerView.html',
      filename: 'plannerView.html',
      inject: false
    }),
    new  HtmlWebpackPlugin({
      template: './src/taskResponse.html',
      filename: 'taskResponse.html',
        inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/taskFailResponse.html',
      filename: 'taskFailResponse.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/penaltyOutput.html',
      filename: 'penaltyOutput.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/home.html',
      filename: 'home.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/createTask.html',
      filename: 'createTask.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
     template: './src/failedTasks.html',
     filename: 'failedTasks.html',
     inject: false
    }),

    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve("dist/css")
        },
        {
          from: path.resolve('src/api'),
          to: path.resolve("dist/api")
        }
      ]
    }),
    new CleanWebpackPlugin()
  ]
}
