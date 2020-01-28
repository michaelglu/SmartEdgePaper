# EdgeStorage
## File Descriptions
 - server.js: contains the expressjs routes availible via HTTP requests
 - config folder:contains middleware and helper functions
    - authclient.js: old script used in the demo running on the lab server; used for basic auth token authorization
     - authuser.js: looks up the user by token and makes sure it matches. Middleware fired at every route at which authorization is needed
     - fileManager.js: converts file name to a url that can be used to download the file via http request
     - mongoose.js: mongoose and database setup
     - multer.js: contains multer set up functions for image and renderable uploads
     - weather.js: weather model determination script
 - models folder: contains mongoose models for database
      - augmentedImage.js: model for augmented image
      - renderable.js: model for renderable
      - user.js: model for user; also contains fucntions for token generations and look up
 - routes folder: the file name correspond to HTTP methods used when the routes are fired
      - images folder
          - delete.js: contains functions for deletina an image and deleting an image file
          - get.js: contains functions to get all images or single image by id
          - post.js: contains function to create a new image document in database
      - renderables folder
          - delete.js:  contains functions for deletina an renderable and deleting an model file
          - get.js: contains a route to get renderable by id
          - patch.js: contains a route to update a renderable by adding a new model file
      - user folder
          - delete.js: contains logout function
          - get.js: contains functions to get user's info based on a token, and to get public information about each user
          - post.js: contains function for creating a new user
          - put.js: contains a login function