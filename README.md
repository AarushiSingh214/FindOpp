FindOpp - README Template
===

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description 
FindOpp is an app that allows users to find opportunities for their interests based on the user's location. From computer science to art to any other interest, this app displays opportunities related to the user’s interests and location. 

### App Evaluation
- **Category:** Opportunities
- **Mobile:** Mobile is essential so that users can easily find the opportunities that they are interested in. 
- **Story:** Allows users to find the extracurricular opportunities in the area they are looking for depending on age, interest, and location
- **Market:** Any student who is finding opportunities in their field of interest.
- **Habit:** People would be using this app constantly when trying to find opportunities for themselves whether that's in the summer or year round.
- **Scope:** V1 would be the sign up or login page. V2 would be the page you get taken to depending on if you click sign up or login. V3 would be the home screen/fragment which is a recommended list of all the opportunities in the user’s local area. The user's location is determined by the location they enter when they first register. V4 would be the search screen/fragment which allows the user to search for opportunities outside of their default location and interest. V5 is the profile screen/fragment where the user can see all of their registered information, and this page also displays all of the opportunities that the user liked. V6 would be the screen that opens up when the user clicks on the opportunity. They are given more details about that specific opportunity and they can get directions to that opportunity throgh Google Maps.


## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can login
* User can make a new account and enter their information
* User can see a list of opportunities based off the location they entered on the home screen
* User can search for opportunities outside of their registered information
* User can like opportunities and it will be added to their porfile screen
* User can click on an opportunity and see more information


**Optional Nice-to-have Stories**

* User can see a map and click a get directions button to get the time it takes to travel between the user's lcoation and the opportunities location through Google Maps
* User will see a pop-up window if they try to search for an opportunity without entering a location


### 2. Screen Archetypes

* First screen
   * Login and Signup page
   * User sees buttons to either login or register
   
* Second screen
   * Either the login screen or register screen

* Third Screen
    * After the user logs in or registers, they are taken to teh home screen where they see a list of opportunities that are in the user's location

* Fourth Screen
    * After choosing an opportunity, this takes the user to another screen where they can see more information about that opportunity
    
 * Fift screen
    * If the user clicks on the "Get Directions" button, then they are taken to Google Maps where they can see the time and preview the directions   

* Sixth Screen
    * The search screen allows the user to search for other opportunities

* Seventh Screen
    * After the user clicks on the search button, they are taken to another screen that will display their search results

* Eighth Screen 
    * The profile screen allows the users to see their registered information and the opportunities they have liked

## Digital Wireframes & Mockups

* https://www.figma.com/file/3WsNLnnSsORTW1ArTAq5sD/FINAL-APP-WIREFRAME-2?node-id=0%3A1

## Schema 
Users
| Property | Type | Condition |
| -------- | -------- | -------- |
| objectId | String | unique id for the user(default) |
|updatedAt | Date | date when account of the user is updated(default)|
| createdAt | Date | date when the usermakes an account(default)|
| username | String | username of the user |
|password| String| password of the user's account|
|email | String | user's email|
|year_of_birth| Number| user's birthday to calculate age|
|interests| String | what fields or area the user is interested in|
|location| String | the activity that the user is trying to find an activity in|


Opportunities
| Property | Type | Condition |
| -------- | -------- | -------- |
| objectId | String | unique id for the user(default) |
|updatedAt | Date | date when account of the user is updated(default)|
| createdAt | Date | date when the usermakes an account(default)|
|name | String| the person/organization that mpost about this opportunity|
|point of contact| String| who to contact if interested(email)|
|location| String| where this opportunity takes place|
|age| String| age requirments for opportunity|
|duration| String| how long this opportunity is/ start date and end date|
|description| String| description of the opportunity|
|supplies| String| if anything is needed for this opportunity|
|title| String| title of the opportunity(this is what shows up in user's feeds)|
|cost| String| cost of the opportunity|
|address| String| address of the opportunity|

Likes
| Property | Type | Condition |
| -------- | -------- | -------- |
| objectId | String | unique id for the user(default) |
|updatedAt | Date | date when account of the user is updated(default)|
| createdAt | Date | date when the usermakes an account(default)|
|opportunity| Pointer to Opportunity| contains the objectId of the opportunity that was liked|
|user| Pointer to User| contains the objectId of the user that liked the opportunity|

### Networking
* Recommendations Screen/Home Feed
 -(Read/GET) query all the opportunities that match the user's location
 -(Update/PUT) add favorited opportunities to user profile
 
 * Search Results Screen
 -(Read/GET) query all the opportunities that match the user's search fields


