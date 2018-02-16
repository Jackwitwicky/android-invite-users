# android-invite-users

This is a simple Android Module that allows a developer to seamlessly intergrate a functioning invitation module
into their already existing android application.

## Getting Started

This module is designed to be as seamless as possible and should work as a plug and play to your existing android application 
requiring minimal modification.

## How it Works

A recycler view displays a list of the user's contacts. A user can then select the contacts they wish to invite which will then

be added to a list located at the top of the view. They can use cancel buttons to remove already selected contacts.

When a user clicks the invite button located on the action bar, you can now use a ``` list ``` of ``` Person ``` objects
to perform the desired functionality. This varies from sending the selected contacts SMS or any other functionality.

### Prerequisites

What things you need to install the software and how to install them

```
Android Studio

A working Android Project

Minimum sdk used :- 16

```

### Installing

Use the following steps to get up and running as fast as possible and get back to building cool content.

#### Gradle

```
//on your app build.gradle file

dependencies {
    ...
    
  implementation 'com.github.davidpizarro:autolabelui:1.0.1'
  implementation 'de.hdodenhof:circleimageview:2.2.0'
}
```
#### Source Code

Activity

```
create an activity and copy the contents of **InviteActivity.java**


or


copy the **InviteActivity.java** file and add it to your package directory.

(Remember to add it to your manifest)

```

Adapter

```
copy the **MyAdapter.java** file to your project changing the package name where applicable

```

Model

```
copy the **Person.java** file to your project. This is used to store details of a particular contact

```

#### Resources

Ensure the following resources have been placed in the correct places

```
drawable

layouts

values (strings, dimens, arrays)

```

## Built With

* [Ubuntu 17.10](https://www.ubuntu.com/desktop/1710) - Operating System Used
* [Android Studio](https://developer.android.com/studio/index.html) - Development Environment
* Love - Key ingredient

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc

