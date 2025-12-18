# CMSC495
# Smart Elevator System

A Java-based smart elevator simulation built with Spring Boot and a simple HTML/JavaScript frontend. This project simulates elevator requests and responses, logs trips, calculates efficiency, and displays metrics via a real-time dashboard.

## Features

### Core Functionality

- Smart elevator assignment using direction, distance, and capacity
- Elevator state tracking (floor, direction, load)
- Logs each trip with smart vs. traditional timing
- Calculates time saved on each trip
- REST API with Spring Boot
- In-memory H2 database (with support for SQLite/PostgreSQL)
- Admin dashboard with real-time metrics using Chart.js

### Frontend

- Simulates elevator call buttons
- Displays assigned elevator and ETA
- Cancel request option
- Admin dashboard with total time saved and performance charts

## Project Structure

![image](https://github.com/user-attachments/assets/52c61c5c-a3df-4972-8f35-a3d80bb151a4)


To work with our project using Git, the first thing you want to do is make sure you’re on the correct branch. If you’re working on the frontend, you should be on feature-frontend; if you’re working on the backend, switch to feature-backend. You can check your current branch using git branch. If you're not on the right one, use git checkout branch-name to switch. Once you're on the right branch, and you've made changes to your code, you'll need to stage those changes using git add . (or specify specific files like git add index.html script.js). After that, commit your changes with a message that explains what you did using git commit -m "Your message here". If it's your first time committing on a new computer or account, Git may ask you to configure your name and email — you can do this with git config --global user.name "Your Name" and git config --global user.email "you@example.com".

Once committed, push your changes to GitHub using git push origin your-branch-name. If you need to bring in the latest updates from GitHub, use git pull origin your-branch-name to update your local branch. When your feature is complete and tested, and you’re ready to integrate it with others, switch to the dev branch using git checkout dev, pull the latest with git pull origin dev, and then merge your feature into it using git merge feature-frontend (or feature-backend, depending on your role). Finally, push the updated dev branch with git push origin dev.

## Getting Started

### Prerequisites
- Java 17+
- Maven
- Git

---

### 🔨 Running the Backend

# Clone the repo
git clone https://github.com/yourusername/CMSC495.git
cd CMSC495/backend

# Build and run
mvn clean install
mvn spring-boot:run

## App Startup

Once the application is running, access it at:

- **Backend Root:** [http://localhost:8080](http://localhost:8080)
- **User Interface:** [http://localhost:8080/index.html](http://localhost:8080/index.html)
- **Admin Dashboard:** [http://localhost:8080/admin.html](http://localhost:8080/admin.html)

---

## Admin Dashboard

The admin dashboard displays:

- Smart elevator duration vs. traditional duration  
- Time saved per trip  
- Total time saved  
- Real-time line chart powered by Chart.js  
- Dynamic data fetched via (http://localhost:8080/api/trips)

---

## API Endpoints

| Method | Endpoint                                | Description                      |
|--------|-----------------------------------------|----------------------------------|
| GET    | `/api/elevators`                        | Get all elevators                |
| POST   | `/api/elevators`                        | Create a new elevator            |
| GET    | `/api/elevators/{id}`                   | Get a specific elevator          |
| POST   | `/api/elevators/{id}/move/{floor}`      | Move elevator to a floor         |
| POST   | `/api/elevators/{id}/load/{numPeople}`  | Load passengers                  |
| POST   | `/api/elevators/{id}/unload/{numPeople}`| Unload passengers                |
| DELETE | `/api/elevators/{id}`                   | Delete elevator                  |
| GET    | `/api/trips`                            | Get all trips                    |
| POST   | `/api/trips`                            | Log a trip (includes time saved) |
| GET    | `/api/trips/{id}`                       | Get a specific trip              |
| DELETE | `/api/trips/{id}`                       | Delete a trip                    |

---

## Tech Stack

- Java 17  
- Spring Boot  
- Maven  
- H2 / SQLite / PostgreSQL  
- HTML / CSS / JavaScript  
- Chart.js  

---

Git Command Recap (When to Use What):

git branch – check what branch you’re currently on

git checkout branch-name – switch to the branch you need to work on

git pull origin branch-name – get the latest version of the branch from GitHub

git add . – stage all changed files

git commit -m "your message" – commit changes with a clear message

git push origin branch-name – upload your changes to GitHub

git merge other-branch – bring changes from one branch into the current one (usually merging into dev)
