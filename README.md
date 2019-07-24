
# Vacuum Fitness App Guide


# 1 Description

Vacuum Fitness is a way to train the abdomen combining breathing techniques with yoga-positions. The App now provides a nice Vacuum Fitness Training based on the users level and available time.

## 1.1 Intended User

People with a link to yoga, pilates or gym they would like to do something to define their belly.

## 1.2 Features

*   User can choose a Random Training or he can prepare a custom Training.
*   User can either use app music or prepare his own playlist to use music from his device.
*   App provides Video Tutorials on how to use the App, about Vacuum Fitness in general and about the single exercises.
*   App provides a widget that motivates him to do a training, he press start in the widget what brings him directly to the PreparationFragment.


# 2 User Interface


## 2.1 MainActivity

![](https://raw.githubusercontent.com/staropramen/VacuumFitness/master/images/main1.png) ![](https://raw.githubusercontent.com/staropramen/VacuumFitness/master/images/main2.png =200x) ![](https://raw.githubusercontent.com/staropramen/VacuumFitness/master/images/settings.png =200x)


*   MainActivity has a Collapsing AppBar with a icon for Settings.
*   A fragment provides the 4 main purposes of the app. Start Training, Customize Training, Customize Music and About.
*   In the Settings Fragment the User can choose his preferred Music, Training and Level. He can disable the Visual or Voice Commands and he can choose if the App should duck the music while a Voice Command is playing.


## 2.2 Training Activity
![](https://raw.githubusercontent.com/staropramen/VacuumFitness/master/images/prep.png =200x) ![](https://raw.githubusercontent.com/staropramen/VacuumFitness/master/images/training.png =200x)

Training Activity starts with the preparation fragment where the user set up his training, after pressing start he comes to the training fragment where his training is happening.
*   User can pause the training, on interruption the training pauses to.
*   User can click the video button to start a youtube video about the exercise, training pauses automatically.
*   User can click the music button to mute the music


## 2.3 Customize Training Activity
![](https://raw.githubusercontent.com/staropramen/VacuumFitness/master/images/cust_t1.png =200x) ![](https://raw.githubusercontent.com/staropramen/VacuumFitness/master/images/cust_t2.png =200x) ![](https://raw.githubusercontent.com/staropramen/VacuumFitness/master/images/cust_t3.png =200x)

Customize Training Activity i the place where customization is happening, depending on users choice he can customize his training.

*   Customize Training starts with a fragment of trainings that he made and a Fab button to add more custom trainings
*   In the next fragment uses sees the details of his training, he can click the fab button to add exercises
*   In the next fragment users has all exercises. Exercises they are already in the training are highlighted.


## 2.4 Customize Music Activity
![](https://raw.githubusercontent.com/staropramen/VacuumFitness/master/images/music1.png =200x) ![](https://raw.githubusercontent.com/staropramen/VacuumFitness/master/images/music2.png =200x) ![](https://raw.githubusercontent.com/staropramen/VacuumFitness/master/images/music3.png =200x)

Customize Music Activity i the place where customization is happening, depending on users choice he can customize his music.


*   In the first fragment users sees playlist and can add new playlists
*   In the second fragment the user see the details of his playlist he can add songs clicking the fab button
*   In the next fragment the user has a list of all music on his device, he can add songs to the playlist. Songs they are already in the playlist are highlighted.


## 2.5 About Section
![](https://raw.githubusercontent.com/staropramen/VacuumFitness/master/images/about2.png =200x) ![](https://raw.githubusercontent.com/staropramen/VacuumFitness/master/images/abput.png =200x)

Actually this is happening in the Main Activity. The About section gives users more informations about the app.
*   The “How to Exercise” button opens a Youtube video that explains how Vacuum Fitness works.
*   The “How to use the App” Button opens a Youtube video that explains how the app works.
*   The “About Us” button opens the Imprint Fragment that show information about the distributor and credits.


## 2.6 App Widget
![](https://raw.githubusercontent.com/staropramen/VacuumFitness/master/images/widget.png =200x)

App provides a widget that motivate the user to do a quick exercise in his breaks. If user clicks “Start” he comes directly to the Prepare Training Screen.



# 3. Developers Guide


## 3.1 Development

App is written solely in the Java Programming Language, layouts in XML.


## 3.2 Data Persistence

All Database operations are made asynchronously, database was implemented using Room.

Preferences: Users preferences are stored in Shared Preferences.

Exercises: App stores all information about the single exercises as objects in the db.

Custom Training: A custom training is a object stored in the db..

Custom Music: Playlist are objects stored in the db.

Widget: 

The texts provide by the widget are stored as Motivator object in the db.

To don’t have all the time same motivation texts in the widget, texts are provided by a json on my github account. Weekly WorkManager will schedule to update the motivation texts in the database.

App provides this Data while using ViewModel and LiveData. To make sure data will be saved asynchronous, app executors or async tasks are used.


## 3.3 Navigation 

Training Activity: On back pressed user comes back to Main Activity. 

Customize Training Activity, Customize Music Activity and Main Activity: Fragments will be added to backstack, user navigates always back to fragment he was before.


## 3.4 Third Party Libraries

The App uses only stable versions of  the following libraries:

*   Butterknife 8.8.1
*   Picasso 2.71828
*   Retrofit 2.3.0
*   Exoplayer 2.8.1


## 3.5 Google Play Services

*   The free version includes google admob 15.0.1
*   App uses Analytics for Firebase 16.0.1


