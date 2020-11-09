# SonarRobot
Sonar Robot is a project that aims to explore the surrounding environment with a radar-like screen.

<img width="450" alt="db_sr" src="https://user-images.githubusercontent.com/12733113/98541424-e06caa00-22a8-11eb-8c99-e4ad80199641.jpeg">

## How it works

The robot hardware is set up using a Raspberry Pi 3, and an Android application is used for controlling the mobile robot and monitoring the enivornment

Distances to nearby obstacles are measured using sonar waves, and distance is visually scaled up to a specific precision at the software level 

Multithreading is used to de-couple database interaction from obstacle detection, achieving smooth interaction.

## A demo of the Android application:

![42C1F839-AC68-4E62-B758-079E66C64D77](https://user-images.githubusercontent.com/12733113/98540930-19f0e580-22a8-11eb-9afb-c669a519f03a.gif)

## A snapshot of the database updated in realtime:

<img width="307" alt="db_sr" src="https://user-images.githubusercontent.com/12733113/98541168-6fc58d80-22a8-11eb-8a93-ff4f994fdf8d.png">

## Credits
@omarica @ftavakoli @KaiSoni
