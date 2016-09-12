# zayzn Release Preparation Tool - Community Edition

## Description

This tool is intended to save you some time during the final preparation steps before uploading a video to YouTube,
provided the resources are organized in a hierarchical manner.

zayzn Release Preparation Tool - Community Edition is free (as in free beer) and open source software.

**Features and use cases:**

* Perfect for separating tags and description elements of a series from those of an episode.
* Don't type the same lines and tags over and over again.
* Include channel wide tags and description elements.
* Shows how many characters were used and if their number exceeds the limit.
* Have all the files and info you need for the upload immediately available at one location.

Place the tool in and the run script from the top folder of the hierarchy and paste the full path name into the console
window.

Example (Windows):

    C:\Users\UserName\YouTube\MyVlog
                                \SoInteresting
                                \UnusualExperience
                             \MyGamePlay
                                \PlanetInvasion
                                    \Episode01
                                        \Episode01.mp4
                                        \Episode01.png
                                        \description.txt
                                        \tags.txt
                                    (...)
                                \tags.txt
                             \products\ <- where prepared releases are stored 
                             \PrepareRelease.jar
                             \prepare-release.bat <- execute this!
                             \description.txt
                             \tags.txt

The script will ask you for the directory of the video you want to release. This tool will then scan this directory for

* the video file and  
* the thumbnail file.

The video and thumbnail files must have the same name as the directory. The video file must a .mp4-file and the
thumbnail file must be a .png-file. This tool does not check the integrity of these files, it only copies them.

The tool will also check for this directory and all parent directories, up to the directory the tool is installed, for

* a textfile containing the tags (*tags.txt*)
* a textfile containing the description (*description.txt*)

and combines them into one file. If the number of characters exceeds YouTubes limit, a warning will be shown. After the
the product creation has finished, the folder will be opened for your convenience on supported platforms.

## Support

This application is supported on any Microsoft Windows, Mac OS and Linux operating system which meet the system
requirements.

[Support and feature requests can be submitted here: https://github.com/zayzn/prepare-release/issues](https://github.com/zayzn/prepare-release/issues)

## System requirements and environment parameters

* [Java Runtime Environment Version 1.8](https://www.java.com/download/)

## Installation

* [Download the latest release from here: https://github.com/zayzn/prepare-release/releases](https://github.com/zayzn/prepare-release/releases)
* Extract the archive to the root of the structure like explained above.

