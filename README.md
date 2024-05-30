# FaceDetectionApp

FaceDetectionApp is an Android application that utilizes CameraX and ML Kit to perform real-time face detection using the device's camera. The detected faces are highlighted with bounding boxes, and users can capture and save these images to their device's gallery.

## Features

- Real-time face detection using CameraX and ML Kit
- Switch between front and back cameras
- Capture and save detected faces to the device's gallery
- Input dialog for saving additional information about the detected face


## Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/EL-MANCY/FaceDetectionApp.git
   cd FaceDetectionApp
   ```

2. Open the project in Android Studio.

3. Build the project to download the necessary dependencies.

## Usage

1. Run the application on an Android device or emulator with a camera.
2. Grant the necessary permissions when prompted.
3. Use the floating action button to switch between the front and back cameras.
4. Detected faces will be highlighted with bounding boxes in real-time.
5. Capture an image of a detected face by clicking the capture button.
6. Input the name and ID for the detected face in the dialog that appears.
7. The image will be saved to the device's gallery, and the information will be stored locally.

## Permissions

The app requires the following permissions:
- Camera
- Write External Storage (for saving images to the gallery)
- Read External Storage (for accessing saved images)

## Code Overview

### MainActivity

The main activity of the app handles the camera initialization, face detection, and user interactions. It uses CameraX for camera operations and ML Kit for face detection.

### FaceDetection

The `FaceDetection` class manages the face detection process using ML Kit's `FaceDetector`. It processes the camera frames and identifies faces in real-time.

### InputDialogFragment

The `InputDialogFragment` class handles the user input dialog for saving additional information about the detected face. It communicates with the `MainActivity` to save the captured image and user input.

### FaceDetectorViewModel

The `FaceDetectorViewModel` class manages the data for the detected faces and handles the saving of this data using Room for local storage.

### Utils

Utility functions for saving the captured bitmap to a temporary file and the device's gallery.

## Dependencies

- [CameraX](https://developer.android.com/training/camerax)
- [ML Kit Face Detection](https://developers.google.com/ml-kit/vision/face-detection)
- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [Room](https://developer.android.com/training/data-storage/room)
- [AndroidX](https://developer.android.com/jetpack/androidx)

## Acknowledgements

- [Google ML Kit](https://developers.google.com/ml-kit)
- [Android Developers](https://developer.android.com/)
```
``` 
