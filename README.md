# Java Chaikins Algorithm Project

## Table of Contents

- [Description](#description)
- [Installation](#installation)
- [Usage](#usage)
- [License](#license)
- [Contributing](#contributing)
- [Authors](#authors)

## Description

This is a Java project that implements the Chaikins algorithm by creating a step-by-step animation of the process using a canvas. The project is implemented using JavaFX.

The application contains a canvas that allows users to add control points by clicking on the canvas.

When the user presses the `Enter` key, the application will start the Chaikins algorithm animation.

The animation will run through 7 steps of the Chaikins algorithm, with each step taking 0.5 seconds.

After that, the animation will restart from the beginning (step 1) and continue until the user presses the `Shift` or `Esc` key.

## Installation

To install the project, clone the repository to your local machine.

```bash
git clone https://github.com/danglam88/Jaikin.git
```

Make sure you have Java and JavaFX installed and configured properly on your machine.

You can download Java [here](https://www.oracle.com/java/technologies/javase-downloads.html).

You can download JavaFX [here](https://gluonhq.com/products/javafx/).

After downloading JavaFX, you need to note down the path to the `lib` folder inside the downloaded JavaFX SDK folder. Let's call this path `$PATH_TO_FX_LIB`.

## Usage

To run the project, navigate to src/main/java from the project root directory, then build the project (using javac) and run the output (using java).

```bash
javac --module-path $PATH_TO_FX_LIB --add-modules javafx.controls com/jaikin/ChaikinsAlgorithm.java
```

```bash
java --module-path $PATH_TO_FX_LIB --add-modules javafx.controls com.jaikin.ChaikinsAlgorithm
```

This will run the application. Here are the steps to use the application:

- Click on the canvas to add a new control point (and clear the reminder message if any).
- You can drag and drop a control point to change its position (before or after starting the animation).
- Press the `Enter` key when there are no control points on the canvas will display a reminder message.
- Press the `Enter` key when there is only one control point on the canvas will display only that control point.
- Press the `Enter` key when there are two control points on the canvas will display a straight line connecting those two control points.
- Press the `Enter` key when there are three or more control points on the canvas will start the Chaikins algorithm animation.
- Press the `Shift` key to clear the canvas to add new control points again.
- Press the `Esc` key to exit the application.

## License

This project is licensed under the MIT License. See the LICENSE file for more details.

## Contributing

Contributions are welcome. Please open an issue or submit a pull request if you would like to help improving the project.

## Authors

- [Danglam](https://github.com/danglam88)
- [JacobP](https://github.com/Jacobpes)
