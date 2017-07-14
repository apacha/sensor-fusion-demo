# Sensor fusion demo for Android

[![Build Status](https://www.bitrise.io/app/46b5cf7adea1286f.svg?token=MZUhPFZvIBiaTSEinY9zUQ&branch=master)](https://www.bitrise.io/app/46b5cf7adea1286f)
[![Build Status](https://travis-ci.org/apacha/sensor-fusion-demo.svg?branch=master)](https://travis-ci.org/apacha/sensor-fusion-demo)
[![Documentation Status](https://readthedocs.org/projects/sensor-fusion-demo/badge/?version=latest)](http://sensor-fusion-demo.readthedocs.io/en/latest/?badge=latest)

This application demonstrates the capabilities of various sensors and sensor-fusions. Data from the Gyroscope, Accelerometer and compass are combined in different ways and the result is shown as a cube that can be rotated by rotating the device.


Read the full documentation [here](http://sensor-fusion-demo.readthedocs.io).

The major novelty in this application is the fusion of virtual sensors: **Improved Orientation Sensor 1** and **Improved Orientation Sensor 2** fuse the Android Rotation Vector with the virtual Gyroscope sensor to achieve a pose estimation with a previously unknown stability and precision.

Apart from these two sensors, the following sensors are available for comparison:

- Improved Orientation Sensor 1 (Sensor fusion of Android Rotation Vector and Calibrated Gyroscope - less stable but more accurate)
- Improved Orientation Sensor 2 (Sensor fusion of Android Rotation Vector and Calibrated Gyroscope - more stable but less accurate)
- Android Rotation Vector (Kalman filter fusion of Accelerometer + Gyroscope + Compass)
- Calibrated Gyroscope (Separate result of Kalman filter fusion of Accelerometer + Gyroscope + Compass)
- Gravity + Compass
- Accelerometer + Compass

This application was developed for demonstrating the sensor fusion approach developed for my [Master Thesis "Sensor fusion for robust outdoor Augmented Reality tracking on mobile devices"](https://alexanderpacha.files.wordpress.com/2017/05/masterthesis-pacha.pdf) at the [Human Interface Technology Laboratory New Zealand](http://www.hitlabnz.org).

## Build and Install

This project is an Gradle-based Android Studio project. It is also published in the [Google Play Store](https://play.google.com/store/apps/details?id=org.hitlabnz.sensor_fusion_demo), if you just want to try it out. 

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

## License

Released under the MIT license.

Copyright, 2017, by [Alexander Pacha](http://alexanderpacha.com) and the [Human Technology Laboratory New Zealand](http://www.hitlabnz.org).

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

This application also uses parts from the the Android Open Source Project, licensed under the [Apache License, Version 2.0]( http://www.apache.org/licenses/LICENSE-2.0).

## Data privacy statement

This application does not store or transmit any data.
