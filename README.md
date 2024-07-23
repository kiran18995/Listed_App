# Listed Task

## Description

Create UI components from the response you shall be getting from the given API URL and populate the
data mentioned below accordingly from the given UI template
1) Display greeting from the local time
2) Display the name from the API
3) Create a chart from the given API response
4) Add a Tab [Top links & Recent links] and create a list view to display the data you shall be
getting from the API response

## Getting Started
Directly through this link for APK: [APK](https://github.com/kiran18995/Github_Search/releases)

### Displaying Greetings from Local Time
<img src="https://github.com/kiran18995/listed_task/assets/48232762/7fe6394b-0153-4dea-8eb5-f884115697cc" alt="My Image" width="200" height="400">

I have added greetings that will show from local time.

### Displaying Name from API
<img src="https://github.com/kiran18995/listed_task/assets/48232762/7fe6394b-0153-4dea-8eb5-f884115697cc" alt="My Image" width="200" height="400">

The name which is showing here is hardcoded because the name is not available in the given API.

### Creating a Chart from API Response

The chart used here is "mpandroidchart." find below the link for the library.

Link: https://github.com/PhilJay/MPAndroidChart

### Adding Tabs and ListView
<img src="https://github.com/kiran18995/listed_task/assets/48232762/3a16c1bd-04d3-4b1c-b295-c0c0b369c9f6" alt="My Image" width="200" height="400">
<img src="https://github.com/kiran18995/listed_task/assets/48232762/63efb34f-17a4-40f6-95bd-b49197162bc0" alt="My Image" width="200" height="400">

I have used a recycler view and two text views for the tabs as it is showing only 5 lists. If the user wants more user will click on view all links, so I made minimal complexity.
```diff
- Added copy-to-clipboard functionality in this
- Added splash screen
- Added loading
- added WhatsApp message for "Talk with us"

## Key Components of Codebase
The application is built following the MVVM Architecture and the Android Jetpack components in consideration.
Uses Kotlin Extension functions & Scoping functions to keep the code legible, clean, and null safe and make the codebase follow the separation of concerns.
Uses SVG and PNG to handle the scaling automatically.
Uses Retrofit for Making Calls to the backend server
Uses Kotlin coroutines for the asynchronous operations
Uses Glide to fetch and display images
and every dimension or image and its size references from provided Figma file only.
