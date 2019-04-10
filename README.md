# Twister

This is a university project for "Technologies du Web - L3" 
It's a 'Tweeter like' website called 'Twister'
it contains :
- A server-side (Java EE)
- A client-side (ReactJS)

## Programming Languages & Development Tools
### Langage: 
Java EE, SQL,NoSQL, HTML, CSS, JavaScript, ReactJS

### IDE:
Eclipse 
### Servers:
-tomcat v9.0 (added to eclipse)
### SQL database:
-mysql local: wamp/xamp 
-mysql on cloud (remotemysql.com , don't change the generated password when you create your database)
 	
### NoSQL database: 	
-mongoDb 4.0.6 local 
-mongoDb Atlas on cloud 

To use the local or the remote database : open the java file Database.java, and choose the databse in both functions:
getMySQLConnection and getMongoDBConnection 


#### Data visualization on mysql:
phpmyadmin

#### Data visualization on mongoDB:
mongoDB Compass: a GUI that lets you visually, explore your data.

### MapReduce:
not yet

## Installation
First Clone it:
```
 git clone http://github.com/AmineDjeghri/Twister.git
```
-the server-side is Twister/Twister, import the project inside Eclipse and run it in a server (Tomcat v9.0)
-the client-side is Twister/twister-react, run it with npm (You need to be inside twister-react):
	```cd twister-react```
	```npm install```
	```npm start```

#### You can change the actual configuration of the server-side inside the file: 
Twister/Twister/src/db/DBStatic.java
#### Or keep the actual configuration which is:
-PHPMyAdmin 3.1.7: port:80 , username='root', password='root' with MySQL port 3306  and the Database name 'twister_bd'
-MongoDB: port 27017 , database name is 'twister_BD'


### Testing:
Postman (read the Testing guide)

## Important :

The required services to code are in Ennoncés/listeServices.pdf 

We changed the name of the services so they will look like Tweeter and added also some other services

Here all the services :

- CreateUser (like inscription)
- Login
- Logout
- AddTwist (like tweet , previous naming: addMessage/addComment)
- Like (liking a tweet)
- Comment (to comment a tweet)
- RemoveTwist (previous naming: RemoveMessage)
- ListTwists (previous naming: ListMessage)
- Follow (preview naming: AddFriend)
- ListFollowers (previous naming: ListFriends)
- ListFollowing 
- Unfollow (previous naming: RemoveFriend)
