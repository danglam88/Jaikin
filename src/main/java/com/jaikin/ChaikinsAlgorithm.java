package com.jaikin;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ChaikinsAlgorithm extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private Timeline timeline;
    private List<Point> originalPoints;
    private List<Point> points = new ArrayList<>();
    private Point draggedPoint;
    private boolean dragging = false;
    private boolean running = false;
    private boolean showMessage = false;
    private int step = 0;

    // point has x and y position
    private static class Point {
        double x;
        double y;
        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        double getX() {
            return this.x;
        }
        double getY() {
            return this.y;
        }
        void setX(double x) {
            this.x = x;
        }
        void setY(double y) {
            this.y = y;
        }
    }

    // main program
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chaikins Algorithm Animation");
        BorderPane root = new BorderPane();
        // create a canvas window 1000 * 1000 pixels
        canvas = new Canvas(1000, 1000);
        gc = canvas.getGraphicsContext2D();
        clearCanvas();

        canvas.setOnMouseClicked(event -> {
            if (!running) {
                if (showMessage) {
                    showMessage = false;
                    clearCanvas();
                }

                for (Point point: points) {
                    // Calculates the distande using Euclidean distance formula
                    double distance = Math.sqrt(Math.pow(event.getX() - point.getX(), 2) + Math.pow(event.getY() - point.getY(), 2));
                    if (distance <= 5) {
                        dragging = true;
                        draggedPoint = point;
                        break;
                    }
                }

                if (!dragging && draggedPoint == null) {
                    points.add(new Point(event.getX(), event.getY()));
                    drawPoint(event.getX(), event.getY());
                }
            }
        });

        canvas.setOnMouseDragged(event -> {
            if (!running && dragging && draggedPoint != null) {
                clearCanvas();
                draggedPoint.setX(event.getX());
                draggedPoint.setY(event.getY());
                drawPoints();
            }
        });

        canvas.setOnMouseReleased(event -> {
            if (!running) {
                dragging = false;
                draggedPoint = null;
            }
        });

        root.setCenter(canvas);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !running) {
                // if no points are clicked and message doesn't show already.
                if (points.isEmpty() && !showMessage) {
                    showMessage = true;
                    gc.setFill(Color.WHITE);
                    gc.setFont(new Font(20));
                    // display multiline string
                    gc.fillText("""Please add at least one point to this black board
                    (This message will disappear when you add a point)""", 10, 30);
                } else if (!showMessage) {
                    running = true;
                    // if there are 2 dots, simply connect them with a line.
                    if (points.size() == 2) {
                        drawLine(points.get(0), points.get(1));
                        // else store clicked points and animate 7-step animation.
                    } else if (points.size() > 2) {
                        originalPoints = new ArrayList<>(points);
                        startAnimation();
                    }
                }
            } else if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.SHIFT) {
                running = false;
                showMessage = false;
                if (timeline != null) {
                    timeline.stop();
                }
                // close on esc.
                if (event.getCode() == KeyCode.ESCAPE) {
                    primaryStage.close();
                    // clear canvas and points on shift.
                } else if (event.getCode() == KeyCode.SHIFT) {
                    clearCanvas();
                    points = new ArrayList<>();
                    originalPoints = null;
                }
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Draw one point
    private void drawPoint(double x, double y) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1.0);
        gc.strokeOval(x - 5, y - 5, 10, 10);
    }

    // Call drawPoint on all points.
    private void drawPoints() {
        for (Point point: originalPoints) {
            drawPoint(point.getX(), point.getY());
        }
    }

   // Draw one line.
    private void drawLine(Point p1, Point p2) {
        gc.setStroke(Color.WHITE);
        gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    // Call drawLine to connect all points.
    private void drawLines() {
        Point firstPoint = originalPoints.get(0);
        Point lastPoint = originalPoints.get(originalPoints.size() - 1);
        drawLine(firstPoint, points.get(0));
        drawLine(lastPoint, points.get(points.size() - 1));
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            drawLine(p1, p2);
        }
    }

    // Clear canvas by turning all pixels to black.
    private void clearCanvas() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    // Calculate the new points in seven steps with Chaikins algorithm and repeat.
    private void startAnimation() {
        // 0,5 sec delay between each step
        timeline = new Timeline(new KeyFrame(Duration.millis(500), event -> {
            if (step < 7) {
                // Clear canvas for every step.
                clearCanvas();
                // Draw clicked points.
                drawPoints();
                // Connect current points with lines.
                drawLines();

                // Implement Chaikins algorithm by clearing newpoints and set them to 1/4 and 3/4 between all previous points.
                List<Point> newPoints = new ArrayList<>();
                // for all points in previous points
                for (int i = 0; i < points.size() - 1; i++) {
                    Point p1 = points.get(i);
                    Point p2 = points.get(i + 1);
                    // Set x1 and y1 at 1/4 the distance from p1 to p2.
                    double x1 = 0.75 * p1.getX() + 0.25 * p2.getX();
                    double y1 = 0.75 * p1.getY() + 0.25 * p2.getY();
                    // Set x2 and y2 at 3/4 the distance from p1 to p2.
                    double x2 = 0.25 * p1.getX() + 0.75 * p2.getX();
                    double y2 = 0.25 * p1.getY() + 0.75 * p2.getY();
                    // Add the new two points between each of the previous points.
                    newPoints.add(new Point(x1, y1));
                    newPoints.add(new Point(x2, y2));
                }
                // Inherit the newPoints to points for the next step.
                points = newPoints;
                step++;
            } else {
                // Start the animation again by resetting steps and clearing points.
                step = 0;
                points = new ArrayList<>(originalPoints);
            }
        }));
        // Repeat until user stop.
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
