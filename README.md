# Project 3 - *Instagram*

**Instagram** is a photo sharing app using Parse as its backend.

Time spent: **X** hours spent in total

## User Stories

The following **required** functionality is completed:

- [x] User sees app icon in home screen.
- [x] User can sign up to create a new account using Parse authentication
- [x] User can log in and log out of his or her account
- [x] The current signed in user is persisted across app restarts
- [x] User can take a photo, add a caption, and post it to "Instagram"
- [x] User can view the last 20 posts submitted to "Instagram"
- [x] User can pull to refresh the last 20 posts submitted to "Instagram"
- [x] User can tap a post to view post details, including timestamp and caption.

The following **stretch** features are implemented:

- [x] Style the login page to look like the real Instagram login page.
- [x] Style the feed to look like the real Instagram feed.
- [x] User should switch between different tabs - viewing all posts (feed view), capture (camera and photo gallery view) and profile tabs (posts made) using a Bottom Navigation View.
- [x] User can load more posts once he or she reaches the bottom of the feed using infinite scrolling.
- [x] Show the username and creation time for each post
- [x] After the user submits a new post, show an indeterminate progress bar while the post is being uploaded to Parse
- User Profiles:
  - [x] Allow the logged in user to add a profile photo
  - [x] Display the profile photo with each post
  - [x] Tapping on a post's username or profile photo goes to that user's profile page
  - [x] User Profile shows posts in a grid view
- [x] User can comment on a post and see all comments for each post in the post details screen.
- [x] User can like a post and see number of likes for each post in the post details screen.
- [x] Run your app on your phone and use the camera to take the photo

The following **additional** features are implemented:

- [x] Add user bio with option to update from profile page
- [x] Add ability to take picture from posts fragment
- [x] Add ability to like / comment from posts fragment 
- [x] User can click into post details from user profiles

Please list two areas of the assignment you'd like to **discuss further with your peers** during the next class (examples include better ways to implement something, how to extend your app in certain ways, etc):

1. How to upload photos (from our device's camera roll) onto this app. 
2. How to implement chat function using Parse database

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='1.gif' title='Video 1' width='400' alt='Video 1' />

In Video 1, the user is already logged into an account. The user is able to navigate to the profile page
and log back out to the login page, which is styled to look like Instagram's. Next to each post is the 
poster's profile image and username, along with the creation time to the right.  


The user then creates another account with a new username and password (hence the login error - thanks to Parse authentication)
to sign up. In the home screen, the app icons are visible and the feed is styled to look like Instagram's.
The user then likes a post from the feed and views its post details, which displays the timestamp, caption, like count, and all comments.


Next, the user navigates to the user profile and updates the bio. Clicking the camera icon, the user then takes
a new picture with the camera that replaces the default profile picture. The user then moves back to the home feed
and taps on another user's profile image, which pulls up its profile page. 


<img src='2.gif' title='Video 2' width='400' alt='Video 2' />

Video 2 demonstrates that the current signed in user is persisted across app restarts, and that the 
user (who is signed up in Video 1) can log out and log back in. The user can like / unlike a post, and 
see the number of likes for a post in its details screen. 

<img src='3.gif' title='Video 3' width='400' alt='Video 3' />

In the third video, the current user first navigates to another user's profile, where its posts are displayed
in grid view. The current user then takes a picture by clicking the capture icon on the Bottom Navigation View. 
After snapping a pic, the user adds a caption and posts it to "Instagram". As the post is being uploaded to 
Parse, an indeterminate progress bar appears over the photo. The user can also take a picture using the camera
icon in the home feed screen. 

<img src='4.gif' title='Video 4' width='400' alt='Video 3' />

For Video 4, the user can pull to refresh to view new posts submitted to Instagram by other users. The user then
scrolls to the bottom of the feed where more posts are loaded using infinite scrolling (20 posts are loaded with
each query). Finally, the user then clicks into another user's profile, and is able to view post details by clicking
on post images in the grid.  
  

GIF created with [Kap](http://getkap.co/).

## Credits

List an 3rd party libraries, icons, graphics, or other assets you used in your app.

- [Android Async Http Client](http://loopj.com/android-async-http/) - networking library


## Notes

Describe any challenges encountered while building the app.

## License

    Copyright [2020] [Jacqueline He]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.